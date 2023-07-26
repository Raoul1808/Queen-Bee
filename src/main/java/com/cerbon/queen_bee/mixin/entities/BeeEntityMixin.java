package com.cerbon.queen_bee.mixin.entities;

import com.cerbon.queen_bee.config.QBCommonConfigs;
import com.cerbon.queen_bee.item.QBItems;
import com.cerbon.queen_bee.util.IBeeEntityMixin;
import com.cerbon.queen_bee.util.QBConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeEntityMixin extends Animal implements NeutralMob, IBeeEntityMixin {
    @Unique private int queen_bee_despawnTime;
    @Unique private boolean queen_bee_canDespawn;

    public BeeEntityMixin(EntityType<? extends Bee> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void queen_bee_addCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        pCompound.putInt("QBDespawnTime", this.queen_bee_getDespawnTime());
        pCompound.putBoolean("QBCanDespawn", this.queen_bee_canDespawn());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void queen_bee_readCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        this.queen_bee_despawnTime = pCompound.getInt("QBDespawnTime");
        this.queen_bee_canDespawn = pCompound.getBoolean("QBCanDespawn");
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void queen_bee_despawnBee(CallbackInfo ci){
        if (!this.level().isClientSide){
            if (this.queen_bee_getDespawnTime() > 0){
                --this.queen_bee_despawnTime;
            }
            if (this.queen_bee_getDespawnTime() == 0 && this.queen_bee_canDespawn()){
                this.remove(RemovalReason.KILLED);
            }
        }
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target){
        if (QBCommonConfigs.ENABLE_ANTENNA.get()){
            String targetDimension = target.level().dimension().location().toString();
            boolean isTargetInBumblezoneDimension = targetDimension.equals(QBConstants.BUMBLEZONE_DIMENSION_ID);
            boolean isTargetWearingAntenna = target.getItemBySlot(EquipmentSlot.HEAD).getItem() == (QBItems.ANTENNA.get());
            boolean isAntennaEnabledInBlumblezoneDimension = QBCommonConfigs.ENABLE_ANTENNA_BUMBLEZONE_DIMENSION.get();

            if (isAntennaEnabledInBlumblezoneDimension && isTargetInBumblezoneDimension && isTargetWearingAntenna){
                return false;
            }else if(isTargetWearingAntenna && !isTargetInBumblezoneDimension){
                return false;
            }
        }
        return super.canAttack(target);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target != null){
            if (QBCommonConfigs.ENABLE_ANTENNA.get()){
                String targetDimension = target.level().dimension().location().toString();
                boolean isTargetInBumblezoneDimension = targetDimension.equals(QBConstants.BUMBLEZONE_DIMENSION_ID);
                boolean isTargetWearingAntenna = target.getItemBySlot(EquipmentSlot.HEAD).is(QBItems.ANTENNA.get());
                boolean isAntennaEnabledInBlumblezoneDimension = QBCommonConfigs.ENABLE_ANTENNA_BUMBLEZONE_DIMENSION.get();

                if (isAntennaEnabledInBlumblezoneDimension && isTargetInBumblezoneDimension && isTargetWearingAntenna){
                    return;
                }else if(isTargetWearingAntenna && !isTargetInBumblezoneDimension){
                    return;
                }
            }
        }
        super.setTarget(target);
    }

    @Override
    public int queen_bee_getDespawnTime() {
        return queen_bee_despawnTime;
    }

    @Override
    public void queen_bee_setDespawnTime(int ticks) {
        this.queen_bee_despawnTime = ticks;
    }

    @Override
    public boolean queen_bee_canDespawn() {
        return queen_bee_canDespawn;
    }

    @Override
    public void queen_bee_setCanDespawn(boolean canDespawn) {
        this.queen_bee_canDespawn = canDespawn;
    }
}
