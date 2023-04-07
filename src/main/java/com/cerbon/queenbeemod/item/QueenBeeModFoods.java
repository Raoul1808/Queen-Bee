package com.cerbon.queenbeemod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class QueenBeeModFoods {
    public static final FoodProperties STINGER = (new FoodProperties.Builder())
            .nutrition(4)
            .saturationMod(0.3F)
            .effect(new MobEffectInstance(MobEffects.POISON, 200, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0), 1.0F)
            .alwaysEat().build();
}
