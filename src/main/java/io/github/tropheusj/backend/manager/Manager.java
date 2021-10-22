package io.github.tropheusj.backend.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.github.tropheusj.backend.Constants;

import io.github.tropheusj.backend.Utils;

import org.jetbrains.annotations.Nullable;

import com.mojang.authlib.GameProfile;

import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;

public class Manager extends PersistentState {
	public final ServerWorld world;
	public Collection<PumpkinPlot> plots = Collections.emptyList();
	public final Map<String, PumpkinPlot> playersToPlots = new HashMap<>();
	public BlockPos lastPumpkin = Constants.FIRST_PUMPKIN;
	public ServerPlayerEntity dummyPlayer;
	public boolean round1 = false;
	public boolean round2 = false;
	public boolean gameOver = false;

	public Manager(ServerWorld toManage, @Nullable NbtCompound nbt) {
		this.world = toManage;
		// one of Ella's alts, we need a dummy player to hold the server wide claims
		dummyPlayer = new ManagerFakePlayer(
				new FakePlayerBuilder(Utils.id("manager_player")),
				world.getServer(),
				world,
				new GameProfile(UUID.fromString("c0a17033-6f53-4492-8987-eaa147317202"), "DeesseLouve"));
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
		plot = new PumpkinPlot(this, world, playersToPlots.size(), lastPumpkin.offset(Direction.EAST, 10), player);
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
		round1 = nbt.getBoolean("Round1");
		round2 = nbt.getBoolean("Round2");
		int plotCount = nbt.getInt("PumpkinPlotCount");
		for (int i = 0; i < plotCount; i++) {
			PumpkinPlot plot = PumpkinPlot.fromNbt(nbt.getCompound("PumpkinPlot" + i), world, this);
			addPlot(plot);
		}
		lastPumpkin = plots != null && plots.size() > 0 ? ((PumpkinPlot) plots.toArray()[plots.size() - 1]).pumpkin : Constants.FIRST_PUMPKIN;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putBoolean("Round1", round1);
		nbt.putBoolean("Round2", round2);
		nbt.putInt("PumpkinPlotCount", playersToPlots.size());
		List<PumpkinPlot> plots = playersToPlots.values().stream().toList();
		for (int i = 0; i < playersToPlots.size(); i++) {
			nbt.put("PumpkinPlot" + i, plots.get(i).toNbt());
		}
		return nbt;
	}
}
