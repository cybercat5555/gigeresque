package mods.cybercat.gigeresque.common.entity.impl.misc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.look.LookAtTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.misc.Idle;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.MoveToWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.StrafeTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import mod.azure.azurelib.sblforked.api.core.sensor.ExtendedSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.NearbyBlocksSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.HurtBySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienProjectileAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.impl.projectile.AcidSpitProjectile;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class SpitterEntity extends AlienEntity implements SmartBrainOwner<SpitterEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public SpitterEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        this.vibrationUser = new AzureVibrationUser(this, 1.3F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 1.0F, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.spitterConfigs.spitterXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.spitterConfigs.spitterXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                0.0
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
            .add(
                Attributes.FOLLOW_RANGE,
                16.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.spitterConfigs.spitterAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !this.isCrawling() && !isDead) {
                if (this.onGround() && !this.wasEyeInWater) {
                    if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
                        return event.setAndContinue(GigAnimationsDefault.RUN);
                    else
                        return event.setAndContinue(GigAnimationsDefault.WALK);
                } else if (this.wasEyeInWater && !this.isVehicle())
                    if (this.isAggressive() && !this.isVehicle()) {
                        return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                    } else {
                        return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
                    }
            } else if ((this.isCrawling() || this.isTunnelCrawling()) && !this.isVehicle() && !this.isInWater())
                return event.setAndContinue(GigAnimationsDefault.CRAWL);
            return event.setAndContinue(
                this.wasEyeInWater ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE
            );
        }).setSoundKeyframeHandler(event -> {
            if (this.level().isClientSide) {
                if (event.getKeyframeData().getSound().matches("footstepSoundkey"))
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
                if (event.getKeyframeData().getSound().matches("handstepSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_HANDSTEP.get(),
                            SoundSource.HOSTILE,
                            0.5F,
                            1.0F,
                            true
                        );
                if (event.getKeyframeData().getSound().matches("ambientSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_AMBIENT.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
                if (event.getKeyframeData().getSound().matches("thudSoundkey"))
                    this.level()
                        .playLocalSound(
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            GigSounds.ALIEN_DEATH_THUD.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F,
                            true
                        );
            }
        })
            .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
            .triggerableAnim("idle", GigAnimationsDefault.IDLE)) // idle
            .add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 0, event -> {
                if (event.getAnimatable().isPassedOut())
                    return event.setAndContinue(RawAnimation.begin().thenLoop("stasis_loop"));
                return PlayState.STOP;
            }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("acidspit", GigAnimationsDefault.SPIT) // spit
                .triggerableAnim("acidspray", GigAnimationsDefault.SPRAY) // spray
                .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                .triggerableAnim("swipe_left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                .triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                .triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
                .setSoundKeyframeHandler(event -> {
                    if (this.level().isClientSide) {
                        if (event.getKeyframeData().getSound().matches("clawSoundkey"))
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
                        if (event.getKeyframeData().getSound().matches("tailSoundkey"))
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
                        if (event.getKeyframeData().getSound().matches("crunchSoundkey"))
                            this.level()
                                .playLocalSound(
                                    this.getX(),
                                    this.getY(),
                                    this.getZ(),
                                    GigSounds.ALIEN_CRUNCH.get(),
                                    SoundSource.HOSTILE,
                                    1.0F,
                                    1.0F,
                                    true
                                );
                    }
                }))
            .add(new AnimationController<>(this, "hissController", 0, event -> {
                var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
                if (this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead)
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
    public List<ExtendedSensor<SpitterEntity>> getSensors() {
        return ObjectArrayList.of(
            // Player Sensor
            new NearbyPlayersSensor<>(),
            // Living Sensor
            new NearbyLivingEntitySensor<SpitterEntity>().setRadius(32)
                .setPredicate(GigEntityUtils::entityTest),
            // Block Sensor
            new NearbyBlocksSensor<SpitterEntity>().setRadius(7),
            // Fire Sensor
            new NearbyRepellentsSensor<SpitterEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            // Lights Sensor
            new NearbyLightsBlocksSensor<SpitterEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            // Nest Sensor
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<SpitterEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Looks at target
            new LookAtTarget<>(),
            // Flee Fire
            new FleeFireTask<>(1.3F),
            new StrafeTarget<SpitterEntity>().stopStrafingWhen(
                spitterEntity -> spitterEntity.getTarget() != null && spitterEntity.isWithinMeleeAttackRange(spitterEntity.getTarget())
            ),
            new MoveToWalkTarget<>()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<SpitterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Kill Lights
            new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
            // Do first
            new FirstApplicableBehaviour<SpitterEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                )
                    .stopIf(
                        entity -> this.isPassedOut() || this.isExecuting()
                    ),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.isPassedOut() || !this.isSearching()
                )
            ).stopIf(
                entity -> this.isPassedOut() || this.isExecuting()
            ),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater().setRadius(20).speedModifier(1.15f),
                // Idle
                new Idle<>().startCondition(entity -> !this.isAggressive())
                    .runFor(
                        entity -> entity.getRandom().nextInt(30, 60)
                    )
            )
        );
    }

    @Override
    public BrainActivityGroup<SpitterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            // Invalidate Target
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            // Walk to Target
            new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.5F),
            // Xeno Acid Spit
            new AlienProjectileAttack<>(18, GigMeleeAttackSelector.SPITTER_RANGE_SELECTOR),
            // Xeno attacking
            new AlienMeleeAttack<>(5, GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick() {
        super.tick();
        GigEntityUtils.breakBlocks(this);
        if (!this.isInWater())
            this.setIsCrawling(
                this.horizontalCollision || !this.level().getBlockState(this.blockPosition().below()).isSolid()
            );
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor level,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        if (spawnType != MobSpawnType.NATURAL)
            setGrowth(getMaxGrowth());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        if (this.wasEyeInWater)
            return EntityDimensions.scalable(3.0f, 1.0f);
        if (this.isTunnelCrawling())
            return EntityDimensions.scalable(0.9f, 0.9f);
        return EntityDimensions.scalable(0.9f, 1.9f);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (
            target instanceof LivingEntity livingEntity && !this.level().isClientSide && this.getRandom()
                .nextInt(
                    0,
                    10
                ) > 7
        ) {
            if (livingEntity instanceof Player playerEntity) {
                playerEntity.drop(playerEntity.getInventory().getSelected(), false);
                playerEntity.getInventory().setItem(playerEntity.getInventory().selected, ItemStack.EMPTY);
            }
            if (livingEntity instanceof Mob mobEntity) {
                mobEntity.getMainHandItem();
                this.drop(mobEntity, mobEntity.getMainHandItem());
                mobEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
            }
            livingEntity.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
            livingEntity.hurt(
                damageSources().mobAttack(this),
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.spitterConfigs.spitterXenoTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper)
            creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    public void shootAcid(LivingEntity target, LivingEntity attacker) {
        if (attacker.hasLineOfSight(target)) {
            var acidProjectile = new AcidSpitProjectile(
                level(),
                this,
                new Vec3(
                    this.getTarget().getX() - (this.getX() + this.getViewVector(1.0F).x * 2),
                    this.getTarget().getY(0.5) - (this.getY(0.5)),
                    this.getTarget().getZ() - (this.getZ() + this.getViewVector(1.0F).z * 2)
                )
            );
            acidProjectile.setPos(
                (attacker.getX() + attacker.getViewVector(1.0F).x),
                attacker.getY(0.5),
                attacker.getZ() + attacker.getViewVector(1.0F).z
            );
            attacker.level().addFreshEntity(acidProjectile);
        }
    }

}
