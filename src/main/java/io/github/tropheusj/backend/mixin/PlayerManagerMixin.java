package io.github.tropheusj.backend.mixin;

import io.github.tropheusj.backend.Utils;
import io.github.tropheusj.backend.manager.Manager;
import io.github.tropheusj.backend.manager.Place;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;

import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(at = @At("RETURN"), method = "onPlayerConnect")
	private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		player.sendMessage(Utils.purpleMessage("Welcome to the server!"), false);
		Manager manager = Utils.getManager(player.getServerWorld());
		if (manager.round1) {
			player.sendMessage(Utils.purpleMessage("Round 1 has already begun! Use /pumpkin to get started!"), false);
		} else if (manager.round2) {
			player.sendMessage(Utils.purpleMessage("Round 2 has already begun! Who is the best carver?"), false);
		} else {
			player.sendMessage(Utils.purpleMessage("Round 1 will be starting soon!"), false);
		}
		for (Place place : Place.values()) {
			if (place.playerId != null) {
				if (place.player == null) {
					if (place.playerId.equals(player.getUuidAsString())) {
						place.setPlayer(player);
					}
				}
			}
		}
	}
}
