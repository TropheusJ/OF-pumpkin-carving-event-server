package io.github.tropheusj.backend;

import io.github.tropheusj.backend.manager.Manager;
import io.github.tropheusj.backend.manager.ServerWorldExtensions;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class Backend implements ModInitializer {
	public static final String ID = "backend";

	@Override
	public void onInitialize() {
		Commands.init();
		Receivers.init();
	}

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	public static Manager getManager(ServerWorld world) {
		return ((ServerWorldExtensions) world).getManager();
	}
}
