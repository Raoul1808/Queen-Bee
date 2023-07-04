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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeEntityMixin extends Animal implements NeutralMob, IBeeEntityMixin {
    private int despawnTime;
    private boolean canDespawn;

    public BeeEntityMixin(EntityType<? extends Bee> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        pCompound.putInt("DespawnTime", this.getDespawnTime());
        pCompound.putBoolean("CanDespawn", this.canDespawn());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readCustomData(@NotNull CompoundTag pCompound, CallbackInfo ci){
        this.despawnTime = pCompound.getInt("DespawnTime");
        this.canDespawn = pCompound.getBoolean("CanDespawn");
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void despawnBee(CallbackInfo ci){
        if (!this.level().isClientSide){
            if (this.getDespawnTime() > 0){
                --this.despawnTime;
            }
            if (this.getDespawnTime() == 0 && this.canDespawn()){
                this.remove(RemovalReason.KILLED);
            }
        }
    }

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
    public int getDespawnTime() {
        return despawnTime;
    }

    @Override
    public void setDespawnTime(int ticks) {
        this.despawnTime = ticks;
    }

    @Override
    public boolean canDespawn() {
        return canDespawn;
    }

    @Override
    public void setCanDespawn(boolean canDespawn) {
        this.canDespawn = canDespawn;
    }
}
