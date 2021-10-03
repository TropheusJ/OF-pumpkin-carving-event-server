package io.github.tropheusj.backend.manager;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PumpkinPlot {
	public int index;
	public BlockPos voteButtonPos;
	public BlockPos pumpkin;
	public ServerWorld world;
	public String playerId;

	public PumpkinPlot(ServerWorld world, int index, BlockPos pumpkin, String playerId) {
		this.world = world;
		this.index = index;
		this.pumpkin = pumpkin;
		this.voteButtonPos = pumpkin.offset(Direction.NORTH, 5).offset(Direction.DOWN, 2);
		this.playerId = playerId;
	}

	private PumpkinPlot() {
	}

	public void build() {
		world.setBlockState(pumpkin, Blocks.JACK_O_LANTERN.getDefaultState());
	}

	public static PumpkinPlot fromNbt(NbtCompound nbt, ServerWorld world) {
		PumpkinPlot plot = new PumpkinPlot();
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
