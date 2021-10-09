package io.github.tropheusj.backend.manager;

import com.mojang.authlib.GameProfile;

import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.UUID;

public class ManagerFakePlayer extends FakeServerPlayer {
	public ManagerFakePlayer(FakePlayerBuilder builder, MinecraftServer server, ServerWorld world, GameProfile profile) {
		super(builder, server, world, profile);
	}

	@Override
	public void sendMessage(Text message, MessageType type, UUID sender) {
		System.out.println(message.asString());
		System.out.println(message.getString());
		super.sendMessage(message, type, sender);
	}
}
