package io.github.tropheusj.backend.mixin.flan;

import io.github.flemmli97.flan.claim.ClaimStorage;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ClaimStorage.class, remap = false)
public class ClaimStorageMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendMessage(Lnet/minecraft/text/Text;Z)V"), method = "createClaim", remap = false)
	public void display(ServerPlayerEntity instance, Text message, boolean actionBar) {
	}
}
