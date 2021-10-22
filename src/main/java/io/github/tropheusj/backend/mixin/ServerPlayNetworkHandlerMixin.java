package io.github.tropheusj.backend.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Redirect(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"))
	private void onPlayerMove(Logger instance, String s, Object o, Object o2, Object o3, Object o4) {
	}
}
