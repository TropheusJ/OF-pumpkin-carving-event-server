package io.github.tropheusj.backend.manager;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import io.github.flemmli97.flan.claim.ClaimStorage;
import io.github.tropheusj.backend.Backend;
import io.github.tropheusj.backend.Constants;
import io.github.tropheusj.backend.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PumpkinPlot {
	public int index;
	public Manager manager;
	public BlockPos pumpkin;
	public ServerWorld world;
	public String playerId;
	private ServerPlayerEntity player;
	public StructureManager structureManager;

	public PumpkinPlot(Manager manager, ServerWorld world, int index, BlockPos pumpkin, ServerPlayerEntity player) {
		this.manager = manager;
		this.world = world;
		this.index = index;
		this.pumpkin = pumpkin;
		this.player = player;
		this.playerId = player.getUuidAsString();
		this.structureManager = world.getStructureManager();
	}

	private PumpkinPlot() {
	}

	public void build() {
		// claim creation
		createServerClaims();
		ClaimStorage.get(getPlayer().getServerWorld()).createClaim(pumpkin, pumpkin, getPlayer());
		// structure placing
		BlockPos corner = pumpkin.north(6).east(6).down(2);
		StructurePlacementData structurePlacement = new StructurePlacementData().setRotation(BlockRotation.CLOCKWISE_90);
		structureManager.getStructureOrBlank(Utils.id("pumpkin_plot")).place(world, corner, corner, structurePlacement, world.random, Block.NOTIFY_LISTENERS);
		BlockPos headPos = pumpkin.south(3).east(3).down();
		world.getBlockEntity(headPos, BlockEntityType.SKULL).ifPresent(skull -> skull.setOwner(getSkullOwner()));
		world.setBlockState(pumpkin.down(3), Blocks.BEDROCK.getDefaultState());
		world.setBlockState(pumpkin.down(3).south(), Blocks.BEDROCK.getDefaultState());

		int chance = world.random.nextInt(101);
		Block decorPumpkin;
		if (chance < 4) decorPumpkin = Blocks.MELON;
		else if (chance < 30) decorPumpkin = Blocks.PUMPKIN;
		else if (chance < 60) decorPumpkin = Blocks.CARVED_PUMPKIN;
		else decorPumpkin = Blocks.JACK_O_LANTERN;
		world.setBlockState(pumpkin.south(10), decorPumpkin.getDefaultState());
	}

	public ServerPlayerEntity getPlayer() {
		if (player == null) {
			player = (ServerPlayerEntity) world.getPlayerByUuid(UUID.fromString(playerId));
		}
		return player;
	}

	public static PumpkinPlot fromNbt(NbtCompound nbt, ServerWorld world, Manager manager) {
		PumpkinPlot plot = new PumpkinPlot();
		plot.manager = manager;
		plot.world = world;
		plot.index = nbt.getInt("Index");
		int pumpkinX = nbt.getInt("PumpkinX");
		int pumpkinY = nbt.getInt("PumpkinY");
		int pumpkinZ = nbt.getInt("PumpkinZ");
		plot.pumpkin = new BlockPos(pumpkinX, pumpkinY, pumpkinZ);
		plot.playerId = nbt.getString("PlayerId");
		plot.structureManager = world.getStructureManager();
		return plot;
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("Index", index);
		nbt.putInt("PumpkinX", pumpkin.getX());
		nbt.putInt("PumpkinY", pumpkin.getY());
		nbt.putInt("PumpkinZ", pumpkin.getZ());
		nbt.putString("PlayerId", playerId);

		return nbt;
	}

	public void createServerClaims() {
		ServerPlayerEntity player = manager.dummyPlayer;
		ClaimStorage storage = ClaimStorage.get((ServerWorld) player.world);
		// rectangle north of pumpkin
		BlockPos northCorner1 = pumpkin.west(3).north(6).down(2);
		BlockPos northCorner2 = pumpkin.east(6).north().down(2);
		storage.createClaim(northCorner1, northCorner2, player);
		// rectangle east of pumpkin
		BlockPos eastCorner1 = northCorner2.south();
		BlockPos eastCorner2 = pumpkin.south(14).east().down(2);
		storage.createClaim(eastCorner1, eastCorner2, player);
		// rectangle south of pumpkin
		BlockPos southCorner1 = eastCorner2.west();
		BlockPos southCorner2 = pumpkin.west(3).south(2).down(2);
		storage.createClaim(southCorner1, southCorner2, player);
		// rectangle west of pumpkin
		BlockPos westCorner1 = pumpkin.west().down(2);
		BlockPos westCorner2 = westCorner1.west(2).south();
		storage.createClaim(westCorner1, westCorner2, player);
	}

	public GameProfile getSkullOwner() {
		String[] profile = Constants.SKULL_PROFILES[world.random.nextInt(Constants.SKULL_PROFILES.length)];
		return new GameProfile(UUID.fromString(profile[1]), profile[0]);
	}
}
