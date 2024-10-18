package mods.cybercat.gigeresque.common.entity.impl.runner;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.look.LookAtTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.misc.Idle;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.MoveToWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import mod.azure.azurelib.sblforked.api.core.sensor.ExtendedSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.NearbyBlocksSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.UnreachableTargetSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.HurtBySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyPlayersSensor;
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.ItemEntitySensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillCropsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.AlienPanic;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.EatFoodTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.JumpToTargetTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Objects;

public class RunnerbursterEntity extends ChestbursterEntity implements Growable {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public RunnerbursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
        super(type, level);
        this.vibrationUser = new AzureVibrationUser(this, 0.0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 1.0F, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                        CommonMod.config.runnerbusterConfigs.runnerbusterHealth).add(Attributes.ARMOR, 0.0f).add(
                        Attributes.ARMOR_TOUGHNESS, 0.0f).add(Attributes.KNOCKBACK_RESISTANCE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251).add(Attributes.ATTACK_DAMAGE,
                        CommonMod.config.runnerbusterConfigs.runnerbusterAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.runnerbursterGrowthMultiplier;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount < 5) {
            this.triggerAnim(Constants.ATTACK_CONTROLLER, "birth");
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 10), this);
        }
    }

    /*
    TODO: Remove classic alien when Rom stages ready ready
     */
    @Override
    public LivingEntity growInto() {
        LivingEntity alien;
        if (Objects.equals(hostId, "runner")) alien = GigEntities.RUNNER_ALIEN.get().create(level());
        else alien = GigEntities.ALIEN.get().create(level());

        return alien;
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target) || target.getBbHeight() >= 0.8),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.5f).stopIf(entity ->  this.isPassedOut() || this.isVehicle()),
                new AlienMeleeAttack<>(5, GigMeleeAttackSelector.RBUSTER_ANIM_SELECTOR));
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !isDead && !this.isInWater())
                if (walkAnimation.speedOld >= 0.35F) return event.setAndContinue(GigAnimationsDefault.RUN);
                else return event.setAndContinue(GigAnimationsDefault.WALK);
            if (event.isMoving() && !isDead && this.isInWater())
                return event.setAndContinue(GigAnimationsDefault.SWIM);
            return event.setAndContinue(this.wasEyeInWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE);
        }));
        controllers.add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 0,
                event -> PlayState.STOP).triggerableAnim("eat", GigAnimationsDefault.CHOMP).triggerableAnim("birth",
                GigAnimationsDefault.BIRTH).triggerableAnim("death", GigAnimationsDefault.DEATH));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
