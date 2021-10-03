package io.github.tropheusj.backend.mixin;

import io.github.tropheusj.backend.manager.Manager;
import io.github.tropheusj.backend.manager.ServerWorldExtensions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements ServerWorldExtensions {
	@Shadow
	public abstract PersistentStateManager getPersistentStateManager();

	@Unique
	private Manager manager;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void constructor(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long seed, List<Spawner> spawners, boolean shouldTickTime, CallbackInfo ci) {
		manager = getPersistentStateManager().getOrCreate(
				nbt -> new Manager((ServerWorld) (Object) this, nbt),
				() -> new Manager((ServerWorld) (Object) this, null),
				"manager");
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci)  {
		manager.tick();
	}

	@Override
	public Manager getManager() {
		return manager;
	}
}
