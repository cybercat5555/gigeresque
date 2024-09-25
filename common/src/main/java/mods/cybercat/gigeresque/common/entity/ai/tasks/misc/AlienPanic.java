package mods.cybercat.gigeresque.common.entity.ai.tasks.misc;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class AlienPanic extends Behavior<PathfinderMob> {
    private static final Predicate<PathfinderMob> DEFAULT_SHOULD_PANIC_PREDICATE = pathfinderMob -> pathfinderMob.getLastHurtByMob() != null || pathfinderMob.isFreezing() || pathfinderMob.isOnFire();
    private final float speedMultiplier;
    private final Predicate<PathfinderMob> shouldPanic;

    public AlienPanic(float f) {
        this(f, DEFAULT_SHOULD_PANIC_PREDICATE);
    }

    public AlienPanic(float f, Predicate<PathfinderMob> predicate) {
        super(ImmutableMap.of(MemoryModuleType.IS_PANICKING, MemoryStatus.REGISTERED, MemoryModuleType.HURT_BY, MemoryStatus.VALUE_PRESENT), 20, 40);
        this.speedMultiplier = f;
        this.shouldPanic = predicate;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel serverLevel, @NotNull PathfinderMob pathfinderMob) {
        return this.shouldPanic.test(pathfinderMob) && !pathfinderMob.isAggressive();
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel serverLevel, @NotNull PathfinderMob pathfinderMob, long l) {
        return true;
    }

    @Override
    protected void start(@NotNull ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
        pathfinderMob.getBrain().setMemory(MemoryModuleType.IS_PANICKING, true);
        pathfinderMob.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    @Override
    protected void stop(@NotNull ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
        Brain<?> brain = pathfinderMob.getBrain();
        brain.eraseMemory(MemoryModuleType.IS_PANICKING);
    }

    @Override
    protected void tick(@NotNull ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
        var attacker = pathfinderMob.getLastHurtByMob();  // Get the entity that hit this mob
        if (attacker != null && pathfinderMob.getNavigation().isDone()) {
            // Calculate the direction away from the attacker
            var mobPos = pathfinderMob.position();
            var attackerPos = attacker.position();
            var runAwayDirection = mobPos.subtract(attackerPos).normalize().scale(30.0); // Scale to desired run distance

            var panicPos = mobPos.add(runAwayDirection);  // Position away from the attacker

            // Set the panic position as the target
            pathfinderMob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(panicPos, this.speedMultiplier, 0));
        }
    }
}