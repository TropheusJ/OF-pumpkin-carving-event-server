package io.github.tropheusj.backend.mixin.flan;

import io.github.flemmli97.flan.claim.ClaimStorage;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClaimStorage.class)
public class ClaimStorageMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendMessage(Lnet/minecraft/text/Text;Z)V"), method = "createClaim")
	public void display(ServerPlayerEntity instance, Text message, boolean actionBar) {
	}
}
