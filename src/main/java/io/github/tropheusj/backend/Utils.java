package io.github.tropheusj.backend;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

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

	public record TPTarget(int x, int y, int z, float yaw, float pitch) {
	}
}
