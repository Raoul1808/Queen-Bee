package com.cerbon.queen_bee.recipe.brewing;

import com.cerbon.queen_bee.item.QBItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

public class StingerToPoisonPotionBrewingRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(@NotNull ItemStack input) {
        Item inputItem = input.getItem();
        return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION) && PotionUtils.getPotion(input) == Potions.AWKWARD;

    }

    @Override
    public boolean isIngredient(@NotNull ItemStack ingredient) {
        return ingredient.is(QBItems.STINGER.get());
    }

    @Override
    public @NotNull ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient)){
            return PotionUtils.setPotion(new ItemStack(input.getItem()), Potions.POISON);
        }
        return ItemStack.EMPTY;
    }
}
