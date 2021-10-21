package io.github.tropheusj.backend;

import static io.github.tropheusj.backend.Utils.goldMessage;
import static io.github.tropheusj.backend.Utils.purpleMessage;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.flemmli97.flan.claim.Claim;
import io.github.flemmli97.flan.claim.ClaimStorage;
import io.github.flemmli97.flan.player.EnumEditMode;
import io.github.tropheusj.backend.manager.Manager;
import io.github.tropheusj.backend.manager.Place;
import io.github.tropheusj.backend.manager.PumpkinPlot;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.GameMode;

import java.util.List;

public class Commands {
	public static int pumpkinCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerWorld world = context.getSource().getWorld();
		Manager manager = Backend.getManager(world);
		if (!manager.round1) {
			throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralText("")), new LiteralText("It is not currently round 1."));
		}
		ServerPlayerEntity player = context.getSource().getPlayer();
		Pair<PumpkinPlot, Boolean> pair = manager.getOrCreatePlot(player);
		PumpkinPlot plot = pair.getLeft();
		if (pair.getRight()) {
			plot.build();
			player.sendMessage(purpleMessage("Your pumpkin plot has been created."), false);
		}
//		player.getInventory().insertStack(new ItemStack(Registry.ITEM.get(new Identifier("carvepump", "netherite_carver"))));
//		player.changeGameMode(GameMode.SURVIVAL);
		player.teleport(world, plot.pumpkin.getX() + 0.5, plot.pumpkin.getY() - 1, plot.pumpkin.getZ() + 1.5, 0, 0);
		player.sendMessage(goldMessage("Welcome to your pumpkin plot!"), false);
		player.sendMessage(goldMessage("Use /mansion to leave."), false);
		return 1;
	}

	public static int mansionCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();
//		player.getInventory().remove(
//				itemStack -> Registry.ITEM.getId(itemStack.getItem()).toString().equals("carvepump:netherite_carver"),
//				1,
//				player.playerScreenHandler.getCraftingInput()
//		);
//		player.changeGameMode(GameMode.ADVENTURE);
		Utils.tpPlayer(player, Constants.SPAWN);
		player.sendMessage(goldMessage("Welcome to the mansion!"), false);
		player.sendMessage(goldMessage("Use /pumpkin to return to your plot."), false);
		return 1;
	}

	public static int clearPlotsCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Manager manager = Backend.getManager(context.getSource().getWorld());
		ClaimStorage storage = ClaimStorage.get((ServerWorld) manager.dummyPlayer.world);
		for (Claim claim : storage.allClaimsFromPlayer(manager.dummyPlayer.getUuid())) {
			storage.deleteClaim(claim, true, EnumEditMode.DEFAULT, (ServerWorld) manager.dummyPlayer.world);
		}
 		for (PumpkinPlot plot : manager.plots) {
			ClaimStorage playerStorage = ClaimStorage.get((ServerWorld) manager.dummyPlayer.world);
			Claim claim  = playerStorage.getClaimAt(plot.pumpkin);
			if (claim != null) {
				playerStorage.deleteClaim(claim, true, EnumEditMode.DEFAULT, (ServerWorld) manager.dummyPlayer.world);
			}
		}

		for (Place place : Place.values()) {
			place.setPlayer(null);
		}

		manager.plots.clear();
		manager.playersToPlots.clear();
		manager.lastPumpkin = Constants.FIRST_PUMPKIN;
		manager.markDirty();
		context.getSource().sendFeedback(new LiteralText("Plots cleared."), true);
		return 1;
	}

	public static int round1Command(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerWorld world = context.getSource().getWorld();
		Manager manager = Backend.getManager(world);
		if (manager.round1) {
			context.getSource().sendFeedback(new LiteralText("Round 1 has already begun.").formatted(Formatting.RED), false);
			return 1;
		}
		Backend.getManager(world).round1 = true;
		world.getPlayers().forEach(player -> {
			player.sendMessage(goldMessage("Round 1 has begun!"), false);
			player.sendMessage(goldMessage("Use /pumpkin to get started!"), false);
		});
		return 1;
	}

	public static int round2Command(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		boolean canPlace = true;
		for (Place place : Place.values()) {
			if (place.player == null) {
				canPlace = false;
				break;
			}
		}
		if (!canPlace) {
			throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralText("")), new LiteralText("A place has a null player! Set them again!"));
		}

		Manager manager = Backend.getManager(context.getSource().getWorld());
		manager.round2 = true;
		manager.round1 = false;
		List<ServerPlayerEntity> players = context.getSource().getWorld().getPlayers();

		players.forEach(player -> {
			player.sendMessage(goldMessage("Round 2 has begun!"), false);
			player.sendMessage(goldMessage("The top 3 players now get a second"), false);
			player.sendMessage(goldMessage("chance to prove themselves. They"), false);
			player.sendMessage(goldMessage("get full control over their"), false);
			player.sendMessage(goldMessage("pumpkins, within reason."), false);
		});

		for (Place place : Place.values()) {
			place.init();
		}

		players.forEach(player -> {
			String playerId = player.getUuidAsString();
			for (String id : Constants.TP_BLACKLIST) {
				if (playerId.equals(id)) return;
			}
			if (Place.isInPlace(player)) return;
			player.changeGameMode(GameMode.ADVENTURE);
//			player.getInventory().remove(
//				itemStack -> Registry.ITEM.getId(itemStack.getItem()).toString().equals("carvepump:netherite_carver"),
//				1,
//				player.playerScreenHandler.getCraftingInput()
//			);
			Utils.tpPlayer(player, Constants.ROUND_2_SPAWN);
		});

		return 1;
	}

	public static int setPlaceCommand(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
		int placeIndex = IntegerArgumentType.getInteger(ctx, "place");
		if (placeIndex < 1 || placeIndex > 3) throw new CommandSyntaxException(new SimpleCommandExceptionType(new LiteralText("")), new LiteralText("Place must be between 1 and 3!"));
		ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
		Place place = Place.forIndex(placeIndex);
		place.setPlayer(player);
		ctx.getSource().sendFeedback(new LiteralText(String.format("Player [%s] placed in [%s] place", player.getName().asString(), place.ordinal() + 1)), false);
		return 1;
	}

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(literal("pumpkin").executes(Commands::pumpkinCommand));
			dispatcher.register(literal("mansion").executes(Commands::mansionCommand));
			dispatcher.register(literal("clearPlots")
					.requires(source -> source.hasPermissionLevel(2))
					.executes(Commands::clearPlotsCommand));
			dispatcher.register(literal("round")
					.requires(source -> source.hasPermissionLevel(2))
					.then(literal("1").executes(Commands::round1Command))
					.then(literal("2").executes(Commands::round2Command)));
			dispatcher.register(literal("setPlace")
					.requires(source -> source.hasPermissionLevel(2))
					.then(argument("place", IntegerArgumentType.integer())
							.then(argument("player", EntityArgumentType.player())
									.executes(Commands::setPlaceCommand))));
		});
	}
}
