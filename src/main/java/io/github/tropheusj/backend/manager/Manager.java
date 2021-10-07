package io.github.tropheusj.backend.manager;

import com.mojang.authlib.GameProfile;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Manager extends PersistentState {
	public static final BlockPos SPAWN = new BlockPos(-5, 70, -5);
	public static final BlockPos FIRST_PUMPKIN = new BlockPos(0, 70, 0);
	public final ServerWorld world;
	public Collection<PumpkinPlot> plots;
	public final Map<String, PumpkinPlot> playersToPlots = new HashMap<>();
	public BlockPos lastPumpkin = FIRST_PUMPKIN;
	public ServerPlayerEntity dummyPlayer;

	public Manager(ServerWorld toManage, @Nullable NbtCompound nbt) {
		this.world = toManage;
		// one of Ella's alts, we need a dummy player to hold the server wide claims
		dummyPlayer = new ServerPlayerEntity(world.getServer(), world, new GameProfile(UUID.fromString("c0a17033-6f53-4492-8987-eaa147317202"), "DeesseLouve"));
		if (nbt != null) {
			readNbt(nbt);
		}
	}

	public void tick() {

	}

	// boolean - true if plot was just created
	public Pair<PumpkinPlot, Boolean> getOrCreatePlot(ServerPlayerEntity player) {
		String playerId = player.getUuidAsString();
		PumpkinPlot plot = playersToPlots.get(playerId);
		if (plot != null) return new Pair<>(plot, false);
		plot = new PumpkinPlot(this, world, playersToPlots.size(), lastPumpkin.offset(Direction.WEST, 7), playerId);
		addPlot(plot);
		return new Pair<>(plot, true);
	}

	private void addPlot(PumpkinPlot plot) {
		markDirty();
		playersToPlots.put(plot.playerId, plot);
		plots = playersToPlots.values();
		lastPumpkin = plot.pumpkin;
	}

	public void readNbt(NbtCompound nbt) {
		int plotCount = nbt.getInt("PumpkinPlotCount");
		for (int i = 0; i < plotCount; i++) {
			PumpkinPlot plot = PumpkinPlot.fromNbt(nbt.getCompound("PumpkinPlot" + i), world, this);
			addPlot(plot);
		}
		lastPumpkin = ((PumpkinPlot) plots.toArray()[plots.size() - 1]).pumpkin;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("PumpkinPlotCount", playersToPlots.size());
		List<PumpkinPlot> plots = playersToPlots.values().stream().toList();
		for (int i = 0; i < playersToPlots.size(); i++) {
			nbt.put("PumpkinPlot" + i, plots.get(i).toNbt());
		}
		return nbt;
	}
}
