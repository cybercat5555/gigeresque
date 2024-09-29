package mods.cybercat.gigeresque.common.entity.impl.misc;

import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mods.cybercat.gigeresque.Constants;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * TODO: Add new aniamtions when compeleted.
 * TODO: Animate Model to time with tracker item timing
 */
public class HologramEntity extends Entity implements GeoEntity {

    public static final EntityDataAccessor<Integer> DISTANCE_STATE = SynchedEntityData.defineId(HologramEntity.class,
            EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DISTANCE_FROM_STRUCTURE = SynchedEntityData.defineId(HologramEntity.class,
            EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public HologramEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public int getDistanceState() {
        return this.entityData.get(DISTANCE_STATE);
    }

    public void setDistanceState(int distanceState) {
        this.entityData.set(DISTANCE_STATE, distanceState);
    }

    public int getDistanceFromStructure() {
        return this.entityData.get(DISTANCE_FROM_STRUCTURE);
    }

    public void setDistanceFromStructure(int distanceState) {
        this.entityData.set(DISTANCE_FROM_STRUCTURE, distanceState);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(DISTANCE_STATE, 0);
        builder.define(DISTANCE_FROM_STRUCTURE, 0);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        this.setDistanceState(compound.getInt("distance_state"));
        this.setDistanceFromStructure(compound.getInt("distance_from_structure"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.putInt("distance_state", this.getDistanceState());
        compound.putInt("distance_from_structure", this.getDistanceFromStructure());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public boolean ignoreExplosion(@NotNull Explosion explosion) {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public void tick() {
        // Ensures it's always at the center of the block
        if (tickCount == 1)
            this.moveTo(this.blockPosition().offset(0, 0, 0), this.getYRot(), this.getXRot());
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        super.tick();
        if (!this.level().isClientSide() && this.tickCount >= 250) this.kill();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 0, event -> {
            if (this.getDistanceState() == 2)
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("middle"));
            if (this.getDistanceState() == 3)
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("close"));
            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("far_away"));
        }).setParticleKeyframeHandler(event -> {
                    if (this.level().isClientSide && event.getKeyframeData().getEffect().matches("smoke")) {
                            double d2 = this.getX() + (this.random.nextDouble()) * this.getBbWidth() * 0.5D;
                            double f2 = this.getZ() + (this.random.nextDouble()) * this.getBbWidth() * 0.5D;
                            this.level().addParticle(ParticleTypes.FLASH, true, d2, this.getY(0.5), f2, 0, 0, 0);
                        }
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
