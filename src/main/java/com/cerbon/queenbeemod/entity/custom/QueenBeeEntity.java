package com.cerbon.queenbeemod.entity.custom;

import com.cerbon.queenbeemod.client.sound.QueenBeeFlyingSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
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

public class QueenBeeEntity extends Monster implements GeoEntity, FlyingAnimal, NeutralMob {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(QueenBeeEntity.class, EntityDataSerializers.INT);
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private  AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private int underWaterTicks;

    public QueenBeeEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = pEntity.hurt(this.damageSources().sting(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if(flag){
            this.doEnchantDamageEffects(this, pEntity);
            if(pEntity instanceof LivingEntity){
                if(this.level.getDifficulty() == Difficulty.EASY){
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
        if (!pSource.isCreativePlayer() && pSource.getEntity() instanceof LivingEntity){
            Entity target = pSource.getEntity();

            double d0 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
            AABB aabb = this.getBoundingBox().inflate(d0, 10.0D, d0);
            List<Bee> nearbyBees = this.level.getEntitiesOfClass(Bee.class, aabb);

            if (!nearbyBees.isEmpty()){
                setNearbyBeesAngry((LivingEntity) target, nearbyBees);
            }
            if(Math.random() <= 0.2){
                summonAngryBeesTo((LivingEntity) target, nearbyBees);
            }
            if(Math.random() <= 0.2){
                summonPoisonNimbus(pSource);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    protected void setNearbyBeesAngry(LivingEntity target, List<Bee> nearbyBees){
        for(Bee bee: nearbyBees){
            if (bee.getPersistentAngerTarget() == null){
                bee.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
                bee.setPersistentAngerTarget(target.getUUID());
            }
        }
    }

    protected void summonAngryBeesTo(LivingEntity target, List<Bee> nearbyBees){
        boolean allStung = nearbyBees.stream().allMatch(Bee::hasStung);

        if(nearbyBees.isEmpty() || allStung){
            for(int i = 0; i < 3; i++){
                Bee bee = EntityType.BEE.create(this.level);
                if (bee != null){
                    bee.moveTo(this.getX(), this.getY(), this.getZ());
                    bee.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
                    bee.setPersistentAngerTarget(target.getUUID());
                    this.level.addFreshEntity(bee);
                }
            }
        }
    }

    protected void summonPoisonNimbus(DamageSource pSource){
        AABB aabb = this.getBoundingBox().inflate(4.0D);
        List<LivingEntity> nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, aabb);

        for (LivingEntity entity : nearbyEntities){
            if (entity == pSource.getEntity()){
                AreaEffectCloud areaEffectCloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
                areaEffectCloud.setOwner(this);
                areaEffectCloud.setDuration(40);
                areaEffectCloud.setRadius(4.0F);
                areaEffectCloud.setPotion(Potions.POISON);
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1)); //Bees should not get the effect (Needs a fix)
                this.level.addFreshEntity(areaEffectCloud);
            }
        }
    }
    @Override
    public boolean canBeAffected(MobEffectInstance pPotionEffect) {
        return pPotionEffect.getEffect() == MobEffects.POISON ? false : super.canBeAffected(pPotionEffect);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    public static AttributeSupplier setAttribute(){
      return Monster.createMonsterAttributes()
              .add(Attributes.MAX_HEALTH, 100)
              .add(Attributes.MOVEMENT_SPEED, 0.25)
              .add(Attributes.ARMOR, 5)
              .add(Attributes.ATTACK_DAMAGE, 7)
              .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
              .add(Attributes.FLYING_SPEED, 0.25)
              .add(Attributes.FOLLOW_RANGE, 48).build();
    };
    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new QueenBeeEntity.QueenBeeHurtByOtherGoal(this).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new QueenBeeBecomeAngryTargetGoal(this));
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
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

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, false);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        Minecraft.getInstance().getSoundManager().play(new QueenBeeFlyingSoundInstance(this));
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
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
    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);

        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {}

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
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.queen_bee.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
        protected void alertOther(Mob pMob, LivingEntity pTarget) {
            if (pMob instanceof QueenBeeEntity && this.mob.hasLineOfSight(pTarget)) {
                pMob.setTarget(pTarget);
            }
        }
    }
}
