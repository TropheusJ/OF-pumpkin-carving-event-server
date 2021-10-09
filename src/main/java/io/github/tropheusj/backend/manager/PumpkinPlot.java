package io.github.tropheusj.backend.manager;

import io.github.flemmli97.flan.claim.ClaimStorage;
import io.github.flemmli97.flan.event.ItemInteractEvents;
import io.github.flemmli97.flan.player.PlayerClaimData;
import io.github.tropheusj.backend.Backend;
import io.github.tropheusj.backend.mixin.ArmorStandEntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class PumpkinPlot {
	public int index;
	public Manager manager;
	public BlockPos pumpkin;
	public ServerWorld world;
	public String playerId;
	private ServerPlayerEntity player;
	public StructureManager structureManager;
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public Optional<ArmorStandEntity> armorStand = Optional.empty();

	public PumpkinPlot(Manager manager, ServerWorld world, int index, BlockPos pumpkin, ServerPlayerEntity player) {
		this.manager = manager;
		this.world = world;
		this.index = index;
		this.pumpkin = pumpkin;
		this.player = player;
		this.playerId = player.getUuidAsString();
		this.structureManager = world.getStructureManager();
		List<ArmorStandEntity> armorStands = world.getEntitiesByClass(ArmorStandEntity.class, new Box(pumpkin.up()), entity -> true);
		if (armorStands.size() > 0) armorStand = Optional.of(armorStands.get(0));
	}

	private PumpkinPlot() {
	}

	public void build() {
		// armor stand for voting datapack
		ArmorStandEntity armorStand = new ArmorStandEntity(world, pumpkin.getX() + 0.5, pumpkin.getY() + 1, pumpkin.getZ() + 0.5);
		armorStand.setInvisible(true);
		((ArmorStandEntityAccessor) armorStand).invokeSetMarker(true);
		world.spawnEntity(armorStand);
		this.armorStand = Optional.of(armorStand);
		// player claim creation
		ClaimStorage storage = ClaimStorage.get(getPlayer().getServerWorld());
		storage.createClaim(pumpkin.add(-1, -1, -1), pumpkin.add(1, 1, 1), getPlayer());
		// server claim creation
//		for (Pair<BlockPos,BlockPos> positions : getClaimPositions(pumpkin)) {
//			data.setEditingCorner(positions.getLeft());
//			storage.createClaim(data.editingCorner(), positions.getRight(), manager.dummyPlayer);
//		}
		// structure placing
		BlockPos corner = BlockPos.ORIGIN.west(22);
		StructurePlacementData structurePlacement = new StructurePlacementData().setRotation(BlockRotation.CLOCKWISE_90).setPosition(corner);
		Random random = new Random();
		structureManager.getStructureOrBlank(Backend.id("pumpkin_plot")).place(world, corner, corner, structurePlacement, random, Block.NOTIFY_LISTENERS);
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

	public static List<Pair<BlockPos, BlockPos>> getClaimPositions(BlockPos pumpkin) {
		BlockPos northWestUp = pumpkin.west(8).north(5).up();
		return null;
	}
}
