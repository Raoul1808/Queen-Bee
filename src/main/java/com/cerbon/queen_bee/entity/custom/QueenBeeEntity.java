package com.cerbon.queen_bee.entity.custom;

import com.cerbon.queen_bee.client.sound.QueenBeeFlyingSoundInstance;
import com.cerbon.queen_bee.config.QueenBeeModCommonConfigs;
import com.cerbon.queen_bee.item.QueenBeeModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.UUID;

public class QueenBeeEntity extends PathfinderMob implements GeoEntity, FlyingAnimal, NeutralMob {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(QueenBeeEntity.class, EntityDataSerializers.INT);
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(39, 58);
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private int underWaterTicks;
    private int poisonNimbusCooldown;
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.PROGRESS);


    public QueenBeeEntity(EntityType<? extends QueenBeeEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.xpReward = 20;
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier setAttribute(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.FOLLOW_RANGE, 48.0D).build();
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.4F, true));
        this.goalSelector.addGoal(1, new QueenBeeEntity.SetQueenBeeAngryWhenBeeIsAngryGoal(this));
        this.goalSelector.addGoal(1, new QueenBeeEntity.SetNearbyBeesAngryGoal(this));
        this.goalSelector.addGoal(1, new QueenBeeEntity.SummonAngryBeesGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new QueenBeeEntity.QueenBeeHurtByOtherGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new QueenBeeEntity.QueenBeeBecomeAngryTargetGoal(this));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity pTarget) {
        boolean isAntennaEnabled = QueenBeeModCommonConfigs.ENABLE_ANTENNA.get();

        if (isAntennaEnabled){
            String targetDimension = pTarget.level().dimension().location().toString();
            boolean isTargetInBumblezoneDimension = targetDimension.equals("the_bumblezone:the_bumblezone");
            boolean isTargetWearingAntenna = pTarget.getItemBySlot(EquipmentSlot.HEAD).getItem() == (QueenBeeModItems.ANTENNA.get());
            boolean isAntennaEnabledInBlumblezoneDimension = QueenBeeModCommonConfigs.ENABLE_ANTENNA_BUMBLEZONE_DIMENSION.get();

            if (isAntennaEnabledInBlumblezoneDimension && isTargetInBumblezoneDimension && isTargetWearingAntenna){
                return false;
            }else if(isTargetWearingAntenna && !isTargetInBumblezoneDimension){
                return false;
            }
        }
        return super.canAttack(pTarget);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = pEntity.hurt(this.damageSources().sting(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if(flag){
            this.doEnchantDamageEffects(this, pEntity);
            if(pEntity instanceof LivingEntity){
                if(this.level().getDifficulty() == Difficulty.EASY){
                    ((LivingEntity)pEntity).addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
                    ((LivingEntity)pEntity).addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
                } else {
                    ((LivingEntity)pEntity).addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
                    ((LivingEntity)pEntity).addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
                }
            }
            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }
        return flag;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.isCreativePlayer() && pSource.getEntity() instanceof LivingEntity) {
            if (this.isAngry() && QueenBeeModCommonConfigs.ENABLE_POISON_NIMBUS.get()) {
                if (this.poisonNimbusCooldown == 0 && this.random.nextFloat() <= 0.2F) {
                    this.summonPoisonNimbus();
                    this.poisonNimbusCooldown = QueenBeeModCommonConfigs.POISON_NIMBUS_COOLDOWN.get();
                }
            }
        }
        return super.hurt(pSource, pAmount);
    }

    protected void summonPoisonNimbus(){
        LivingEntity target = this.getTarget();
          if (target != null){
              if (target.distanceToSqr(this) <= QueenBeeModCommonConfigs.TARGET_DISTANCE_TO_SQR.get() && this.hasLineOfSight(target)) {
                  PoisonNimbusAreaEffectCloud areaEffectCloud = new PoisonNimbusAreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
                  areaEffectCloud.setOwner(this);
                  areaEffectCloud.setDuration(QueenBeeModCommonConfigs.POISON_NIMBUS_DURATION.get());
                  areaEffectCloud.setRadius(QueenBeeModCommonConfigs.POISON_NIMBUS_RADIUS.get());
                  areaEffectCloud.setFixedColor(8889187);
                  areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.POISON, QueenBeeModCommonConfigs.POISON_EFFECT_DURATION.get(), QueenBeeModCommonConfigs.POISON_EFFECT_AMPLIFIER.get()));
                  this.level().addFreshEntity(areaEffectCloud);
              }
          }
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pPos, LevelReader pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        if (QueenBeeModCommonConfigs.ENABLE_QUEEN_BEE_BOSS_BAR.get()){
            this.bossInfo.addPlayer(pServerPlayer);
        }
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        if (QueenBeeModCommonConfigs.ENABLE_QUEEN_BEE_BOSS_BAR.get()){
            this.bossInfo.removePlayer(pServerPlayer);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide){
            if (this.poisonNimbusCooldown > 0){
                --poisonNimbusCooldown;
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        if(this.isInWaterOrBubble()){
            ++this.underWaterTicks;
        }else {
            this.underWaterTicks = 0;
        }

        if(this.underWaterTicks > 20){
            this.hurt(this.damageSources().drown(), 1.0F);
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), false);
        }
        if (QueenBeeModCommonConfigs.ENABLE_QUEEN_BEE_BOSS_BAR.get()){
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    protected SoundEvent getAmbientSound() {
        Minecraft.getInstance().getSoundManager().play(new QueenBeeFlyingSoundInstance(this));
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);

        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, @NotNull BlockState pState, @NotNull BlockPos pPos) {}

    @Override
    public void setNoGravity(boolean pNoGravity) {
        super.setNoGravity(true);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
        this.persistentAngerTarget = pPersistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "attackController", 4, this::attackPredicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.queen_bee.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<T> tAnimationState) {
        if (this.swinging && tAnimationState.getController().getAnimationState().equals(AnimationController.State.STOPPED)){
            tAnimationState.getController().forceAnimationReset();
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.queen_bee.attack", Animation.LoopType.PLAY_ONCE));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static class PoisonNimbusAreaEffectCloud extends AreaEffectCloud{
        public PoisonNimbusAreaEffectCloud(Level pLevel, double pX, double pY, double pZ) {
            super(pLevel, pX, pY, pZ);
        }

        @Override
        public void addEffect(@NotNull MobEffectInstance pEffectInstance) {
            AABB aabb = this.getBoundingBox().inflate( 7.0D);
            List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, aabb);

            for (LivingEntity entity : nearbyEntities){
                if (!(entity instanceof Bee || entity instanceof QueenBeeEntity)){
                    entity.addEffect(pEffectInstance);
                }
            }
        }
    }

    static class QueenBeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
        public QueenBeeBecomeAngryTargetGoal(QueenBeeEntity pMob) {
            super(pMob, Player.class, 10, true, false, pMob::isAngryAt);
        }

        @Override
        public boolean canUse() {
            return this.queenBeeCanTarget() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            boolean flag = this.queenBeeCanTarget();
            if (flag && this.mob.getTarget() != null) {
                return super.canContinueToUse();
            } else {
                this.targetMob = null;
                return false;
            }
        }
        private boolean queenBeeCanTarget() {
            QueenBeeEntity queenBee = (QueenBeeEntity) this.mob;
            return queenBee.isAngry();
        }
    }

    class QueenBeeHurtByOtherGoal extends HurtByTargetGoal {
        QueenBeeHurtByOtherGoal(QueenBeeEntity pMob) {
            super(pMob);
        }

        @Override
        public boolean canContinueToUse() {
            return QueenBeeEntity.this.isAngry() && super.canContinueToUse();
        }


        @Override
        protected void alertOther(@NotNull Mob pMob, @NotNull LivingEntity pTarget) {
            if (pMob instanceof QueenBeeEntity && this.mob.hasLineOfSight(pTarget)) {
                pMob.setTarget(pTarget);
            }
        }
    }

    static class SetNearbyBeesAngryGoal extends Goal {
        private final QueenBeeEntity queenBee;

        public SetNearbyBeesAngryGoal(QueenBeeEntity queenBee){
            this.queenBee = queenBee;
        }
        @Override
        public boolean canUse() {
            LivingEntity target = this.queenBee.getTarget();
            return target != null && target.isAlive() && this.queenBee.isAngry();
        }

        @Override
        public void tick() {
            LivingEntity target = this.queenBee.getTarget();
            if (target == null) return;
            double d0 = this.queenBee.getAttributeValue(Attributes.FOLLOW_RANGE);
            AABB aabb = this.queenBee.getBoundingBox().inflate(d0, 10.0D, d0);
            List<Bee> nearbyBees = this.queenBee.level().getEntitiesOfClass(Bee.class, aabb);

            for(Bee bee : nearbyBees){
                if (bee.getPersistentAngerTarget() == null){
                    bee.setTarget(target);
                }
            }
        }
    }

    static class SummonAngryBeesGoal extends Goal{
        private final QueenBeeEntity queenBee;
        private int cooldown;
        public SummonAngryBeesGoal(QueenBeeEntity queenBee){
            this.queenBee = queenBee;
        }
        @Override
        public boolean canUse() {
            LivingEntity target = this.queenBee.getTarget();
            return target != null && target.isAlive() && this.queenBee.isAngry() && QueenBeeModCommonConfigs.ENABLE_SUMMON_ANGRY_BEES.get();
        }

        @Override
        public void start() {
            this.cooldown = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
           LivingEntity target = this.queenBee.getTarget();
           if(target == null) return;

           ++this.cooldown;
           if (this.cooldown >= QueenBeeModCommonConfigs.SUMMON_ANGRY_BEES_COOLDOWN.get()){
               double d0 = this.queenBee.getAttributeValue(Attributes.FOLLOW_RANGE);
               AABB aabb = this.queenBee.getBoundingBox().inflate(d0, 10.0D, d0);
               List<Bee> nearbyBees = this.queenBee.level().getEntitiesOfClass(Bee.class, aabb);
               boolean allStung = nearbyBees.stream().allMatch(Bee::hasStung);

               if (nearbyBees.isEmpty() || allStung){
                   for(int i = 0; i < QueenBeeModCommonConfigs.ANGRY_BEES_AMOUNT.get(); i++){
                       Bee bee = EntityType.BEE.create(this.queenBee.level());
                       if (bee != null){
                           bee.moveTo(this.queenBee.getX(), this.queenBee.getY(), this.queenBee.getZ());
                           bee.setTarget(target);
                           this.queenBee.level().addFreshEntity(bee);
                       }
                   }
                   if (this.cooldown >= QueenBeeModCommonConfigs.SUMMON_ANGRY_BEES_COOLDOWN.get()){
                       this.cooldown = 0;
                   }
               }
           }
       }
    }
    static class SetQueenBeeAngryWhenBeeIsAngryGoal extends Goal{
        private final QueenBeeEntity queenBee;
        public SetQueenBeeAngryWhenBeeIsAngryGoal(QueenBeeEntity queenBee){
            this.queenBee = queenBee;
        }

        @Override
        public boolean canUse() {
            return !this.queenBee.isAngry();
        }

        @Override
        public void tick() {
            double d0 = this.queenBee.getAttributeValue(Attributes.FOLLOW_RANGE);
            AABB aabb = this.queenBee.getBoundingBox().inflate(d0, 10.0D, d0);
            List<Bee> nearbyBees = this.queenBee.level().getEntitiesOfClass(Bee.class, aabb);

            for (Bee bee : nearbyBees){
                if (bee.isAngry()){
                    LivingEntity target = bee.getTarget();
                    if (target == null || this.queenBee.getTarget() != null)return;
                    this.queenBee.setTarget(target);
                }
            }
        }
    }
}
