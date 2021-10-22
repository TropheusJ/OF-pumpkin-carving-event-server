package io.github.tropheusj.backend;

import io.github.tropheusj.backend.manager.Manager;
import io.github.tropheusj.backend.manager.ServerWorldExtensions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Utils {
	private Utils() {
		throw new RuntimeException("You just lost the game");
	}

	public static MutableText purpleMessage(String text) {
		return new LiteralText(text).formatted(Formatting.DARK_PURPLE);
	}

	public static MutableText goldMessage(String text) {
		return new LiteralText(text).formatted(Formatting.GOLD);
	}

	public static void tpPlayer(ServerPlayerEntity player, TPTarget target) {
		player.teleport(player.getServerWorld(), target.x(), target.y(), target.z(), target.yaw(), target.pitch());
	}

	public static Identifier id(String path) {
		return new Identifier(Backend.ID, path);
	}

	public static Manager getManager(ServerWorld world) {
		return ((ServerWorldExtensions) world).getManager();
	}

	public record TPTarget(float x, float y, float z, float yaw, float pitch) {
	}
}
