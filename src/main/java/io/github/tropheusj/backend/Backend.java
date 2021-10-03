package io.github.tropheusj.backend;

import io.github.tropheusj.backend.manager.Manager;
import io.github.tropheusj.backend.manager.PumpkinPlot;
import io.github.tropheusj.backend.manager.ServerWorldExtensions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import static net.minecraft.server.command.CommandManager.*;

public class Backend implements ModInitializer {
	public static final String ID = "backend";

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(literal("pumpkin").executes(context -> {
				ServerWorld world = context.getSource().getWorld();
				Manager manager = getManager(world);
				ServerPlayerEntity player = context.getSource().getPlayer();
				Pair<PumpkinPlot, Boolean> pair = manager.getOrCreatePlot(player);
				PumpkinPlot plot = pair.getLeft();
				if (pair.getRight()) {
					plot.build();
				}
				player.teleport(world, plot.pumpkin.getX() + 0.5, plot.pumpkin.getY() + 1, plot.pumpkin.getZ() + 0.5, 180, 0);
				return 1;
			}));

			dispatcher.register(literal("view").executes(context -> {
				context.getSource().getPlayer().teleport(Manager.SPAWN.getX(), Manager.SPAWN.getY(), Manager.SPAWN.getZ());
				return 1;
			}));

			dispatcher.register(literal("clearPlots").executes(context -> {
				Manager manager = getManager(context.getSource().getWorld());
				manager.plots.clear();
				manager.playersToPlots.clear();
				manager.lastPumpkin = Manager.FIRST_PUMPKIN;
				return 1;
			}));
		});
	}

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	public static Manager getManager(ServerWorld world) {
		return ((ServerWorldExtensions) world).getManager();
	}
}
