package mods.cybercat.gigeresque.common.entity.impl.misc;

import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AquaEggEntity extends Entity implements Growable, GeoAnimatable {

    private static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(AquaEggEntity.class, EntityDataSerializers.FLOAT);
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public AquaEggEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setYRot(this.random.nextFloat() * 360.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && this.isAlive())
            grow(this, 1 * getGrowthMultiplier());
        /*
          JFC floating is a bitch
         */
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        var vec3 = this.getDeltaMovement();
        var y = vec3.y + (vec3.y < 0.05999999865889549 ? 5.0E-4F : 0.0F);
        if ((this.isInWater() && this.getFluidHeight(FluidTags.WATER) > 0.10000000149011612) || (this.isInLava() && this.getFluidHeight(FluidTags.LAVA) > 0.10000000149011612)) {
            this.setDeltaMovement(vec3.x * 0.9900000095367432, y, vec3.z * 0.9900000095367432);
        } else this.applyGravity();
        if (this.level().isClientSide) {
            this.noPhysics = false;
        } else {
            this.noPhysics = !this.level().noCollision(this, this.getBoundingBox().deflate(1.0E-7));
            if (this.noPhysics) {
                this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 1.5, this.getZ());
            }
        }
        if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > 9.999999747378752E-6 || (this.tickCount + this.getId()) % 4 == 0) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            var f = 0.98F;
            if (this.onGround()) {
                f = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
            }

            this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.78, f));
            if (this.onGround()) {
                Vec3 vec31 = this.getDeltaMovement();
                if (vec31.y < 0.0) {
                    this.setDeltaMovement(vec31.multiply(1.0, -0.1, 1.0));
                }
            }
        }
    }

    @Override
    public @NotNull BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public float getMaxGrowth() {
        return Constants.TPD / 2.0f;
    }

    @Override
    public LivingEntity growInto() {
        return GigEntities.FACEHUGGER.get().create(level());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(GROWTH, 0.0f);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        this.setGrowth(compound.getFloat("growth"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.putFloat("growth", this.getGrowth());
    }

    @Override
    public float getGrowth() {
        return entityData.get(GROWTH);
    }

    @Override
    public void setGrowth(float growth) {
        entityData.set(GROWTH, growth);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    public float getSpin(float partialTicks) {
        return (this.tickCount + partialTicks) / 20.0F + this.random.nextFloat() * (float) Math.PI * 2.0F;
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 180.0F - this.getSpin(0.5F) / (float) (Math.PI * 2) * 360.0F;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return this.tickCount;
    }
}
