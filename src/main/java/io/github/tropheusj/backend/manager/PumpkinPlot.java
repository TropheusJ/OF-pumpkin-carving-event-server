package io.github.tropheusj.backend.manager;

import io.github.flemmli97.flan.event.ItemInteractEvents;
import io.github.tropheusj.backend.mixin.ArmorStandEntityAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PumpkinPlot {
	public int index;
	public Manager manager;
	public BlockPos voteButtonPos;
	public BlockPos pumpkin;
	public BlockPos southEastDown;
	public BlockPos northWestUp;
	public ServerWorld world;
	public String playerId;

	public PumpkinPlot(Manager manager, ServerWorld world, int index, BlockPos pumpkin, String playerId) {
		this.world = world;
		this.index = index;
		this.pumpkin = pumpkin;
		this.voteButtonPos = pumpkin.offset(Direction.NORTH, 5).offset(Direction.DOWN, 2);
		this.playerId = playerId;
	}

	private PumpkinPlot() {
	}

	public void build() {
		ArmorStandEntity armorStand = new ArmorStandEntity(world, pumpkin.getX() + 0.5, pumpkin.getY() + 1, pumpkin.getZ() + 0.5);
		armorStand.setInvisible(true);
		((ArmorStandEntityAccessor) armorStand).invokeSetMarker(true);
		world.spawnEntity(armorStand);
		ItemInteractEvents.claimLandHandling(manager.dummyPlayer, pumpkin.add(-1, -1, -1));
		ItemInteractEvents.claimLandHandling(manager.dummyPlayer, pumpkin.add(1, 1, 1));
		world.setBlockState(pumpkin, Blocks.JACK_O_LANTERN.getDefaultState());
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
}
