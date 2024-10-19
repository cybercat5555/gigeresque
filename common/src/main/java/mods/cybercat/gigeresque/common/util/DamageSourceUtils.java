package mods.cybercat.gigeresque.common.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

import mods.cybercat.gigeresque.common.tags.GigTags;

public class DamageSourceUtils {

    private DamageSourceUtils() {}

    public static boolean isDamageSourceNotPuncturing(DamageSource source, DamageSources sources) {
        return source == sources.onFire() || source == sources.magic() || source == sources.fall();
    }

    public static void damageArmor(ItemStack itemStack, RandomSource randomSource, int minDamage, int maxDamage) {
        if (!Objects.equals(itemStack, ItemStack.EMPTY) && !itemStack.is(GigTags.ACID_IMMUNE_ITEMS))
            itemStack.setDamageValue(itemStack.getDamageValue() + randomSource.nextIntBetweenInclusive(minDamage, maxDamage));
    }

    public static boolean isDamageFromFront(DamageSource source, LivingEntity alien) {
        if (source.getDirectEntity() != null)
            return alien.getLookAngle().dot(source.getDirectEntity().position().subtract(alien.position()).normalize()) > 0.5;
        return false;
    }
}
