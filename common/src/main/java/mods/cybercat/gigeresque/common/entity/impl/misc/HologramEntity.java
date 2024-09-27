package mods.cybercat.gigeresque.common.entity.impl.misc;

import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.api.common.helper.CommonUtils;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.tickCount >= 100)
                this.kill();
            // Sim Gravity Credit to Boston for this
            this.setDeltaMovement(0, this.getDeltaMovement().y - 0.03999999910593033D, 0);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(0, this.getDeltaMovement().y * 0.9800000190734863D, 0);
        }
        var isInsideWaterBlock = level().isWaterAt(blockPosition());
        if (this.tickCount >= 14)
            CommonUtils.spawnLightSource(this, isInsideWaterBlock);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 0, event -> {
            if (this.getDistanceState() == 2)
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("middle"));
            if (this.getDistanceState() == 3)
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("close"));
            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("far_away"));
        }).setSoundKeyframeHandler(event -> {
            if (this.level().isClientSide && event.getKeyframeData().getSound().matches("step"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_BLOCK_STEP,
                            SoundSource.HOSTILE, 0.5F, 1.0F, true);})
                .setParticleKeyframeHandler(event -> {
                    if (this.level().isClientSide && event.getKeyframeData().getEffect().matches("smoke")) {
                            double d2 = this.getX() + (this.random.nextDouble()) * this.getBbWidth() * 0.5D;
                            double f2 = this.getZ() + (this.random.nextDouble()) * this.getBbWidth() * 0.5D;
                            this.level().addParticle(ParticleTypes.SMOKE, true, d2, this.getY(0.5), f2, 0, 0, 0);
                        }
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
