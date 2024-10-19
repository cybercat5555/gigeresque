package mods.cybercat.gigeresque.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mods.cybercat.gigeresque.common.tags.GigTags;

public class BlockBreakProgressManager {

    private static final Map<BlockPos, Map.Entry<Long, Float>> BLOCK_BREAK_PROGRESS_MAP = new HashMap<>();

    public static void tick(Level level) {
        var gameTime = level.getGameTime();
        if (gameTime % Tick.PER_MINUTE != 0)
            return;

        BlockBreakProgressManager.BLOCK_BREAK_PROGRESS_MAP.entrySet().removeIf(entry -> {
            var lastUpdateTimeMillis = entry.getValue().getKey();
            return System.currentTimeMillis() > lastUpdateTimeMillis;
        });
    }

    public static void damage(Level level, BlockPos blockPos) {
        if (!level.isClientSide())
            BlockBreakProgressManager.BLOCK_BREAK_PROGRESS_MAP.compute(blockPos, (key, entry) -> {
                var blockState = level.getBlockState(blockPos.below());
                var block = blockState.getBlock();
                var cachedValue = entry == null ? 0 : entry.getValue();
                var hardness = block.defaultDestroyTime();
                if (blockState.is(GigTags.WEAK_BLOCKS))
                    hardness *= 6.0f;
                else
                    hardness *= 0.19f;
                var newValue = cachedValue + hardness;
                var progress = (int) Mth.clamp(newValue, 0F, 9F);

                if (progress >= 9) {
                    level.destroyBlockProgress(level.getRandom().nextInt(), blockPos, -1);
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    return null;
                } else {
                    level.destroyBlockProgress(level.getRandom().nextInt(), blockPos, progress);
                }
                return Map.entry(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5), newValue);
            });
    }

    private BlockBreakProgressManager() {
        throw new UnsupportedOperationException();
    }
}
