package mods.cybercat.gigeresque.common.entity.impl.neo;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
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
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class NeobursterEntity extends RunnerbursterEntity {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public NeobursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
        super(type, level);
        this.vibrationUser = new AzureVibrationUser(this, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                        CommonMod.config.neobursterConfigs.neobursterXenoHealth).add(Attributes.ARMOR, 0.0f).add(
                        Attributes.ARMOR_TOUGHNESS, 3.0f).add(Attributes.KNOCKBACK_RESISTANCE, 7.0)
                .add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251).add(Attributes.ATTACK_DAMAGE,
                        CommonMod.config.neobursterConfigs.neobursterAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.chestbursterGrowthMultiplier;
    }

    @Override
    public LivingEntity growInto() {
        return GigEntities.NEOMORPH_ADOLESCENT.get().create(level());
    }

    @Override
    public List<ExtendedSensor<ChestbursterEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<ChestbursterEntity>().setPredicate(
                        GigEntityUtils::entityTest),
                new NearbyBlocksSensor<ChestbursterEntity>().setRadius(7).setPredicate(
                        (block, entity) -> block.is(BlockTags.CROPS)),
                new NearbyRepellentsSensor<ChestbursterEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new NearbyLightsBlocksSensor<ChestbursterEntity>().setRadius(7).setPredicate(
                        (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new HurtBySensor<>(), new ItemEntitySensor<>(),
                new UnreachableTargetSensor<>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                // Flee Fire
                new FleeFireTask<>(3.5F), new AlienPanic(4.0f),
                // Looks at target
                new LookAtTarget<>().stopIf(entity -> this.isPassedOut()).startCondition(
                        entity -> !this.isPassedOut() || !this.isSearching()),
                // Move to target
                new MoveToWalkTarget<>().startCondition(entity -> !this.isPassedOut()).stopIf(
                        entity -> this.isPassedOut()));
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                // Build Nest
                new EatFoodTask<>(40),
                // Kill Lights
                new KillLightsTask<>(), new KillCropsTask<>(),
                // Do first
                new FirstApplicableBehaviour<RunnerAlienEntity>(
                        // Targeting
                        new TargetOrRetaliate<>().stopIf(
                                target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
                        // Look at players
                        new SetPlayerLookTarget<>().predicate(
                                target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())).stopIf(
                                entity -> this.isPassedOut() || this.isExecuting()),
                        // Look around randomly
                        new SetRandomLookTarget<>().startCondition(
                                entity -> !this.isPassedOut() || !this.isSearching())).stopIf(
                        entity -> this.isPassedOut() || this.isExecuting()),
                // Random
                new OneRandomBehaviour<>(
                        // Randomly walk around
                        new SetRandomWalkTarget<>().dontAvoidWater().setRadius(20).speedModifier(1.2f)),
                // Idle
                new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(
                        entity -> entity.getRandom().nextInt(30, 60)));
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
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
            var velocityLength = this.getDeltaMovement().horizontalDistance();
            if (velocityLength >= 0.000000001 && !isDead)
                if (walkAnimation.speedOld >= 0.35F) return event.setAndContinue(GigAnimationsDefault.RUN);
                else return event.setAndContinue(GigAnimationsDefault.WALK);
            else if (this.isBirthed()) return event.setAndContinue(GigAnimationsDefault.BIRTH);
            else return event.setAndContinue(GigAnimationsDefault.IDLE);
        }).setSoundKeyframeHandler(event -> {
            if (this.level().isClientSide) {
                if (event.getKeyframeData().getSound().matches("thudSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD.get(),
                            SoundSource.HOSTILE, 0.5F, 2.6F, true);
                if (event.getKeyframeData().getSound().matches("stepSoundkey"))
                    this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP.get(),
                            SoundSource.HOSTILE, 0.3F, 1.5F, true);
            }
        }));
        controllers.add(
                new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 0,
                        event -> PlayState.STOP).triggerableAnim("eat",
                        GigAnimationsDefault.CHOMP).triggerableAnim("death", GigAnimationsDefault.DEATH));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
