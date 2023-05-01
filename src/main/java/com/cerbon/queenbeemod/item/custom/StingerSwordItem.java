package com.cerbon.queenbeemod.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class StingerSwordItem extends SwordItem {
    public StingerSwordItem(int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(new Tier() {
            @Override
            public int getUses() {
                return 250;
            }

            @Override
            public float getSpeed() {
                return 2.0F;
            }

            @Override
            public float getAttackDamageBonus() {
                return 0;
            }

            @Override
            public int getLevel() {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 12;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }
        }, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        pTarget.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
