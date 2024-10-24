package mods.cybercat.gigeresque.common.entity.impl.hellmorphs;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.HissingTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FindDarknessTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Add animations once animated
 */
public class BaphomorphEntity extends AlienEntity implements SmartBrainOwner<BaphomorphEntity> {

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
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.baphomorphConfigs.baphomorphXenoHealth
            )
            .add(
                Attributes.ARMOR,
                CommonMod.config.baphomorphConfigs.baphomorphXenoArmor
            )
            .add(Attributes.ARMOR_TOUGHNESS, 0.0)
            .add(
                Attributes.KNOCKBACK_RESISTANCE,
                0.0
            )
            .add(Attributes.FOLLOW_RANGE, 16.0)
            .add(
                Attributes.MOVEMENT_SPEED,
                0.3300000041723251
            )
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.baphomorphConfigs.baphomorphAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    protected Brain.@NotNull Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        super.customServerAiStep();
    }

    @Override
    public List<ExtendedSensor<BaphomorphEntity>> getSensors() {
        return ObjectArrayList.of(
            new NearbyPlayersSensor<>(),
            new NearbyLivingEntitySensor<BaphomorphEntity>().setRadius(32),
            new NearbyBlocksSensor<BaphomorphEntity>().setRadius(7),
            new NearbyRepellentsSensor<BaphomorphEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            new NearbyLightsBlocksSensor<BaphomorphEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            new HurtBySensor<>(),
            new UnreachableTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<BaphomorphEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Flee Fire
            new FleeFireTask<ClassicAlienEntity>(3.5F).whenStarting(
                entity -> entity.setFleeingStatus(true)
            ).whenStopping(entity -> entity.setFleeingStatus(false)),
            // Looks at target
            new LookAtTarget<>().stopIf(entity -> this.isPassedOut())
                .startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching()
                ),
            // Hisses
            new HissingTask<>(800).startCondition(entity -> !this.isAggressive())
                .stopIf(entity -> this.isPassedOut() || this.isExecuting() || this.isAggressive()),
            // Move to target
            new MoveToWalkTarget<>().startCondition(entity -> !this.isExecuting())
                .stopIf(entity -> this.isPassedOut() || this.isExecuting())
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<BaphomorphEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Kill Lights
            new KillLightsTask<>().startCondition(
                entity -> !this.isAggressive() || !this.isPassedOut() || !this.isExecuting() || !this.isFleeing()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isPassedOut() || this.isFleeing())
                ),
            // Find Darkness
            new FindDarknessTask<BaphomorphEntity>().stopIf(Mob::isAggressive),
            // Do first
            new FirstApplicableBehaviour<BaphomorphEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isVehicle() || this.isFleeing() || this.isPassedOut())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                ).stopIf(entity -> this.isPassedOut() || this.isExecuting()),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching()
                )
            ).stopIf(entity -> this.isPassedOut() || this.isExecuting() || this.isAggressive()),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater()
                    .setRadius(20)
                    .speedModifier(1.2f)
                    .startCondition(
                        entity -> !this.isPassedOut() || !this.isExecuting() || !this.isAggressive()
                    )
                    .stopIf(
                        entity -> this.isExecuting() || this.isPassedOut() || this.isAggressive() || this.isVehicle()
                    ),
                new Idle<>().startCondition(entity -> !this.isAggressive())
            ).stopIf(entity -> entity.getDeltaMovement().horizontalDistance() > 0)
        );
    }

    @Override
    public BrainActivityGroup<BaphomorphEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.5f).stopIf(entity -> this.isPassedOut() || this.isVehicle()),
            new AlienMeleeAttack<>(5, GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR)
        );
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (isDead)
                return event.setAndContinue(GigAnimationsDefault.DEATH);
            if (
                event.isMoving() && !(this.isCrawling() || this.isTunnelCrawling()) && !this.isExecuting() && !this.isPassedOut()
                    && !this.swinging && !(this.level()
                        .getFluidState(
                            this.blockPosition()
                        )
                        .is(Fluids.WATER) && this.level()
                            .getFluidState(
                                this.blockPosition()
                            )
                            .getAmount() >= 8)
            )
                if (walkAnimation.speedOld >= 0.45F && this.getFirstPassenger() == null)
                    return event.setAndContinue(GigAnimationsDefault.RUN);
                else if (!this.isExecuting() && walkAnimation.speedOld < 0.45F)
                    return event.setAndContinue(GigAnimationsDefault.WALK);
                else if (
                    (this.level()
                        .getFluidState(this.blockPosition())
                        .is(
                            Fluids.WATER
                        ) && this.level()
                            .getFluidState(
                                this.blockPosition()
                            )
                            .getAmount() >= 8) && !this.isExecuting() && !this.isVehicle()
                )
                    return event.setAndContinue(GigAnimationsDefault.SWIM);
            return event.setAndContinue(
                (this.level()
                    .getFluidState(
                        this.blockPosition()
                    )
                    .is(Fluids.WATER) && this.level()
                        .getFluidState(
                            this.blockPosition()
                        )
                        .getAmount() >= 8) ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE
            );
        }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
            .triggerableAnim("idle", GigAnimationsDefault.IDLE_LAND) // idle
            .setSoundKeyframeHandler(event -> {
                if (event.getKeyframeData().getSound().matches("footstepSoundkey") && this.level().isClientSide)
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_FOOTSTEP.get(),
                            SoundSource.HOSTILE,
                            0.5F,
                            1.0F,
                            true
                        );
            }))
            .add(
                new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 1, event -> PlayState.STOP).triggerableAnim(
                    "alert",
                    GigAnimationsDefault.IDLE
                ) // reset hands
                    .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                    .triggerableAnim("alert", GigAnimationsDefault.HISS) // reset hands
                    .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                    .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                    .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                    .triggerableAnim("left_tail_basic", GigAnimationsDefault.LEFT_TAIL) // attack
                    .triggerableAnim("right_tail_basic", GigAnimationsDefault.RIGHT_TAIL) // attack
                    .setSoundKeyframeHandler(event -> {
                        if (event.getKeyframeData().getSound().matches("clawSoundkey") && this.level().isClientSide)
                            this.level()
                                .playLocalSound(
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    GigSounds.ALIEN_CLAW.get(),
                                    SoundSource.HOSTILE,
                                    0.25F,
                                    1.0F,
                                    true
                                );
                        if (event.getKeyframeData().getSound().matches("tailSoundkey") && this.level().isClientSide)
                            this.level()
                                .playLocalSound(
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    GigSounds.ALIEN_TAIL.get(),
                                    SoundSource.HOSTILE,
                                    0.25F,
                                    1.0F,
                                    true
                                );
                    })
            )
            .add(new AnimationController<>(this, "hissController", 0, event -> {
                var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                if (
                    this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead && !(this.level()
                        .getFluidState(
                            this.blockPosition()
                        )
                        .is(Fluids.WATER) && this.level()
                            .getFluidState(
                                this.blockPosition()
                            )
                            .getAmount() >= 8)
                )
                    return event.setAndContinue(GigAnimationsDefault.HISS);
                return PlayState.STOP;
            }).setSoundKeyframeHandler(event -> {
                if (event.getKeyframeData().getSound().matches("hissSoundkey") && this.level().isClientSide)
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_HISS.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
            }).triggerableAnim("hiss", GigAnimationsDefault.HISS));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
