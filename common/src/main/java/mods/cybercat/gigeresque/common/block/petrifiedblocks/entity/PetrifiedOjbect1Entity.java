package mods.cybercat.gigeresque.common.block.petrifiedblocks.entity;

import mod.azure.azurelib.common.api.common.animatable.GeoBlockEntity;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.PetrifiedObjectBlock;
import mods.cybercat.gigeresque.common.block.storage.StorageProperties;
import mods.cybercat.gigeresque.common.block.storage.StorageStates;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PetrifiedOjbect1Entity extends BlockEntity implements GeoBlockEntity {

    public static final EnumProperty<StorageStates> CHEST_STATE = StorageProperties.STORAGE_STATE;
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public PetrifiedOjbect1Entity(BlockPos pos, BlockState state) {
        super(GigEntities.PETRIFIED_OBJECT_1.get(), pos, state);
    }

    public StorageStates getChestState() {
        return this.getBlockState().getValue(PetrifiedOjbect1Entity.CHEST_STATE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, event -> event.setAndContinue(RawAnimation.begin().thenPlayAndHold("petrified"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PetrifiedOjbect1Entity blockEntity) {
        if (blockEntity.level != null && (level.getRandom().nextInt(0, 200) == 0)) {
            int i = state.getValue(PetrifiedObjectBlock.HATCH);
            if (i < level.getRandom().nextInt(2, 25) && state.getValue(CHEST_STATE) == StorageStates.CLOSED) {
                level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.3f, 0.9f + level.getRandom().nextFloat() * 0.2f);
                level.setBlock(pos, state.setValue(PetrifiedObjectBlock.HATCH, i + 1).setValue(CHEST_STATE, StorageStates.CLOSED), 2);
            } else if (i >= 24 && state.getValue(CHEST_STATE) == StorageStates.CLOSED) {
                level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()), pos.getX() + (level.getRandom().nextDouble()),
                        pos.getY() + 0.5D * (level.getRandom().nextDouble()),
                        pos.getZ() + (level.getRandom().nextDouble()),0,0,0);
                level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.3f, 0.9f + level.getRandom().nextFloat() * 0.2f);
                level.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, StorageStates.OPENED).setValue(PetrifiedObjectBlock.HATCH, 24));
                var aquaticChestbursterEntity = GigEntities.AQUATIC_CHESTBURSTER.get().create(level);
                if (aquaticChestbursterEntity != null) {
                    aquaticChestbursterEntity.moveTo(pos.getX() + 0.3 + 0, (double) pos.getY() + 1, pos.getZ() + 0.3,
                            0.0f, 0.0f);
                    level.addFreshEntity(aquaticChestbursterEntity);
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }
}
