package io.github.tropheusj.backend;

import com.mojang.brigadier.context.CommandContext;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.tropheusj.backend.manager.Manager;
import io.github.tropheusj.backend.manager.PumpkinPlot;
import io.github.tropheusj.backend.manager.ServerWorldExtensions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Random;

import static net.minecraft.server.command.CommandManager.*;

public class Backend implements ModInitializer {
	public static final String ID = "backend";

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(literal("pumpkin").executes(Backend::pumpkinCommand));
			dispatcher.register(literal("view").executes(Backend::viewCommand));
			dispatcher.register(literal("clearPlots").executes(Backend::clearPlotsCommand));
		});
	}

	public static int pumpkinCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerWorld world = context.getSource().getWorld();
		Manager manager = getManager(world);
		ServerPlayerEntity player = context.getSource().getPlayer();
		Pair<PumpkinPlot, Boolean> pair = manager.getOrCreatePlot(player);
		PumpkinPlot plot = pair.getLeft();
		if (pair.getRight()) {
			plot.build();
		}
		StructurePlacementData structurePlacement = new StructurePlacementData().setPosition(plot.pumpkin).setRotation(BlockRotation.NONE);
		Random random = new Random();

		player.teleport(world, plot.pumpkin.getX() + 0.5, plot.pumpkin.getY() + 1, plot.pumpkin.getZ() + 0.5, 180, 0);
		world.getStructureManager().getStructureOrBlank(Backend.id("test")).place(world, plot.pumpkin,  plot.pumpkin, structurePlacement, random, 1);
		return 1;
	}

	public static int viewCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		context.getSource().getPlayer().teleport(Manager.SPAWN.getX(), Manager.SPAWN.getY(), Manager.SPAWN.getZ());
		return 1;
	}

	public static int clearPlotsCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Manager manager = getManager(context.getSource().getWorld());
		manager.plots.clear();
		manager.playersToPlots.clear();
		manager.lastPumpkin = Manager.FIRST_PUMPKIN;
		manager.markDirty();
		return 1;
	}

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	public static Manager getManager(ServerWorld world) {
		return ((ServerWorldExtensions) world).getManager();
	}
}
