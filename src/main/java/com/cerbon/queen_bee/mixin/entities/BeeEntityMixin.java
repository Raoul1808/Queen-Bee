package com.cerbon.queen_bee.mixin.entities;

import com.cerbon.queen_bee.item.QueenBeeModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Bee.class)
public abstract class BeeEntityMixin extends Animal implements NeutralMob{
    public BeeEntityMixin(EntityType<? extends Bee> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public boolean canAttack(LivingEntity target){
        if (target.getItemBySlot(EquipmentSlot.HEAD).getItem() == (QueenBeeModItems.ANTENNA.get())){
            return false;
        }
        return super.canAttack(target);
    }
}
