package mods.cybercat.gigeresque.common.util;

import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class DamageSourceUtils {
    private DamageSourceUtils() {
    }

    public static boolean isDamageSourceNotPuncturing(DamageSource source, DamageSources sources) {
        return source == sources.onFire() || source == sources.magic() || source == sources.fall();
    }

    public static void damageArmor(ItemStack itemStack, RandomSource randomSource, int minDamage, int maxDamage) {
        if (!Objects.equals(itemStack, ItemStack.EMPTY) && !itemStack.is(GigTags.ACID_IMMUNE_ITEMS)) {
            itemStack.setDamageValue(itemStack.getDamageValue() + randomSource.nextIntBetweenInclusive(minDamage, maxDamage));
        }
    }
}
