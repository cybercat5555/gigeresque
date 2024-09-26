package mods.cybercat.gigeresque.common.item;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.common.platform.Services;
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UnnamedItem extends Item {

    public UnnamedItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        var itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (level instanceof ServerLevel serverlevel) {
            var blockpos = serverlevel.findNearestMapStructure(GigTags.GIG_EXPLORER_MAPS,
                    player.blockPosition(), 100, false);
            if (blockpos != null) {
                var dx = player.getX() - blockpos.getX();
                var dz = player.getZ() - blockpos.getZ();
                var horizontalDistance = Math.sqrt(dx * dx + dz * dz);

                int distanceCategory;
                if (horizontalDistance  <= 50) {
                    distanceCategory = 3; // Close (within 50 blocks)
                } else if (horizontalDistance  <= 75) {
                    distanceCategory = 2; // Mid-range (50 - 75 blocks)
                } else {
                    distanceCategory = 1; // Far (greater than 75 blocks)
                }
                if (Services.PLATFORM.isDevelopmentEnvironment())
                    AzureLib.LOGGER.info("Distance Category: {}", distanceCategory);
                var viewVector = player.getViewVector(1.0F); // Gets the player's look direction
                var spawnX = player.getX() + viewVector.x * 5;
                var spawnY = player.getY();
                var spawnZ = player.getZ() + viewVector.z * 5;
                var angleToStructure = Math.atan2(blockpos.getZ() - spawnZ, blockpos.getX() - spawnX);
                var hologramEntity = GigEntities.ENGINEER_HOLOGRAM.get().create(level);
                if (hologramEntity != null) {
                    hologramEntity.setPos(spawnX, spawnY, spawnZ);
                    hologramEntity.setDistanceState(distanceCategory);
                    hologramEntity.setDistanceFromStructure((int) horizontalDistance);
                    hologramEntity.setYRot((float) Math.toDegrees(angleToStructure) - 90);
                    hologramEntity.setOnGround(true);
                    level.addFreshEntity(hologramEntity);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), GigSounds.TRACKER_SUMMON.get(),
                        SoundSource.NEUTRAL, 1.0F, 1.0F);
                player.getCooldowns().addCooldown(this, 200);
                return InteractionResultHolder.success(itemstack);
            } else {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CRAFTER_FAIL,
                        SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
            return InteractionResultHolder.consume(itemstack);
        }
        return super.use(level, player, hand);
    }
}
