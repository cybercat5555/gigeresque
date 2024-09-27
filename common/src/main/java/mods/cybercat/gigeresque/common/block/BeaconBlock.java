package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class BeaconBlock extends Block {
    private long lastEffectTime = 0;

    public BeaconBlock() {
        super(Properties.of().sound(SoundType.DRIPSTONE_BLOCK).strength(Float.MAX_VALUE, Float.MAX_VALUE).noOcclusion().noLootTable().randomTicks().noTerrainParticles());
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.tick(state, level, pos, random);
        var currentTime = level.getGameTime();
        var cooldown = 600 + random.nextInt(600);

        if (currentTime - lastEffectTime >= cooldown) {
            var nearbyPlayers = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(10));
            for (var player : nearbyPlayers) {
                if (player.getBlockStateOn().is(GigTags.DUNGEON_BLOCKS)) {
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));  // Apply blindness for 200 ticks (10 seconds)
                }
            }
            lastEffectTime = currentTime;
        }
    }
}
