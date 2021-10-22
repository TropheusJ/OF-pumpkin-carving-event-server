package io.github.tropheusj.backend;

import net.fabricmc.api.ModInitializer;

public class Backend implements ModInitializer {
	public static final String ID = "backend";

	@Override
	public void onInitialize() {
		Commands.init();
		Receivers.init();
	}
}
