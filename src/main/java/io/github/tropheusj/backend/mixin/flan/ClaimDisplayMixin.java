package io.github.tropheusj.backend.mixin.flan;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.flemmli97.flan.player.ClaimDisplay;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;

@Mixin(value = ClaimDisplay.class, remap = false)
public class ClaimDisplayMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"), method = "display", remap = false)
	public void display(ServerPlayNetworkHandler instance, Packet<?> packet) {
	}
}
