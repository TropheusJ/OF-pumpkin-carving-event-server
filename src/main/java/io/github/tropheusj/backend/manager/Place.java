package io.github.tropheusj.backend.manager;

import io.github.tropheusj.backend.Constants;
import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.server.world.ServerWorld;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

import org.jetbrains.annotations.Nullable;

import static io.github.tropheusj.backend.Utils.purpleMessage;

public enum Place {
	FIRST,
	SECOND,
	THIRD

	;

	@Nullable
	public ServerPlayerEntity player;

	public void setPlayer(ServerPlayerEntity player) {
		this.player = player;
	}

	public void init() throws IllegalStateException {
		if (player == null) {
			throw new IllegalStateException("Player for place [" + (ordinal() + 1) + "] is null!");
		}
		int x = Constants.FIRST_ROUND_2_PUMPKIN.getX();
		int y = Constants.FIRST_ROUND_2_PUMPKIN.getY();
		int z = Constants.FIRST_ROUND_2_PUMPKIN.getZ() - (7 * (ordinal() + 1)) + 7;
		player.teleport((ServerWorld) player.world, x + 0.5, y, z + 0.5, -90, 0);
		player.changeGameMode(GameMode.CREATIVE);
		player.sendMessage(purpleMessage("You're in the top 3."), false);
		player.sendMessage(purpleMessage("You have full freedom over your pumpkin."), false);
		player.sendMessage(purpleMessage("Go ham."), false);
		player.sendMessage(purpleMessage("Tip: use the Bits and Chisels mod!").formatted(Formatting.ITALIC), false);
		MutableText text = new LiteralText("Sabotage / griefing will result in an instant ban!").formatted(Formatting.RED, Formatting.BOLD, Formatting.ITALIC);
		player.sendMessage(text, false);
	}

	// 1 - 3
	public static Place forIndex(int i) {
		return values()[i - 1];
	}

	public static boolean isInPlace(ServerPlayerEntity player) {
		return FIRST.player == player || SECOND.player == player || THIRD.player == player;
	}
}
