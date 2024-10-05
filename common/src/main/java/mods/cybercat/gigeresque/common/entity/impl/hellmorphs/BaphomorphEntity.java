package mods.cybercat.gigeresque.common.entity.impl.hellmorphs;

import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.level.Level;

/**
 * TODO: Add animations once animated
 * TODO: Start on AI
 */
public class BaphomorphEntity extends AlienEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public BaphomorphEntity(EntityType<? extends AlienEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 1.0F, true);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                CommonMod.config.baphomorphConfigs.baphomorphXenoHealth).add(Attributes.ARMOR,
                CommonMod.config.baphomorphConfigs.baphomorphXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(
                Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED,
                0.23000000417232513).add(Attributes.ATTACK_DAMAGE,
                CommonMod.config.baphomorphConfigs.baphomorphAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            return event.setAndContinue(GigAnimationsDefault.IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
