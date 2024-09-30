package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.client.particle.GigParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CustomStairsBlock extends StairBlock {
    public CustomStairsBlock(BlockState baseBlockState, Properties settings) {
        super(baseBlockState, settings.randomTicks());
    }

    public CustomStairsBlock(Block block, Properties settings) {
        this(block.defaultBlockState(), settings);
    }

    public CustomStairsBlock(Block block) {
        this(block, Properties.ofFullCopy(block));
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (level.getBlockState(pos.above()).isAir() && pos.getY() <= -50)
            for (var i = 0; i < 5; i++) {
                var offsetX = random.nextDouble() * 0.6D + 0.2D;
                var offsetY = 1.0D;
                var offsetZ = random.nextDouble() * 0.6D + 0.2D;

                level.addParticle(GigParticles.MIST.get(),
                        pos.getX() + offsetX,
                        pos.getY() + offsetY,
                        pos.getZ() + offsetZ,
                        0.0D, 0.002D, 0.0D);
            }
    }
}
