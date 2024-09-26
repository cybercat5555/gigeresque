package mods.cybercat.gigeresque.common.entity.impl.misc;

import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.api.common.helper.CommonUtils;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
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
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.tickCount >= 200)
                this.kill();
            // Sim Gravity Credit to Boston for this
            this.setDeltaMovement(0, this.getDeltaMovement().y - 0.03999999910593033D, 0);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(0, this.getDeltaMovement().y * 0.9800000190734863D, 0);
        }
        var isInsideWaterBlock = level().isWaterAt(blockPosition());
        CommonUtils.spawnLightSource(this, isInsideWaterBlock);
        this.setGlowingTag(true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            // TODO: Set aniamtions for each state with the state 1 being the default
            if (this.getDistanceState() == 2)
                return PlayState.CONTINUE;
            if (this.getDistanceState() == 3)
                return PlayState.CONTINUE;
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
