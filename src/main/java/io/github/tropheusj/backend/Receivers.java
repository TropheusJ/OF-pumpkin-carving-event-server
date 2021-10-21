package io.github.tropheusj.backend;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class Receivers {
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(Constants.COMMENTATOR_TP, (server, player, handler, buf, responseSender) ->
				Utils.tpPlayer(player, Constants.COMMENTATOR_ROOM));

		ServerPlayNetworking.registerGlobalReceiver(Constants.PUMPKINS_TP, (server, player, handler, buf, responseSender) ->
				Utils.tpPlayer(player, Constants.CAMERA_PUMPKINS));

		ServerPlayNetworking.registerGlobalReceiver(Constants.MANSION_TP, (server, player, handler, buf, responseSender) ->
				Utils.tpPlayer(player, Constants.CAMERA_MANSION));
	}
}
