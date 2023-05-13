package com.cerbon.queen_bee.integration.jei;

import com.cerbon.queen_bee.QueenBeeMod;
import com.cerbon.queen_bee.item.QueenBeeModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@JeiPlugin
public class QueenBeeModJEIPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(QueenBeeModItems.STINGER_SWORD.get()),
                VanillaTypes.ITEM_STACK, Component.translatable("tooltip." + QueenBeeMod.MOD_ID + ".stinger_sword"));

        registration.addIngredientInfo(new ItemStack(QueenBeeModItems.ANTENNA.get()),
                VanillaTypes.ITEM_STACK, Component.translatable("tooltip." + QueenBeeMod.MOD_ID + ".antenna"));

        List<IJeiBrewingRecipe> recipes = new ArrayList<>();
        IVanillaRecipeFactory recipeFactory = registration.getVanillaRecipeFactory();
        List<ItemStack> ingredient = Collections.singletonList(new ItemStack(QueenBeeModItems.STINGER.get()));

        ItemStack awkwardPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
        ItemStack poisonPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.POISON);

        ItemStack awkwardSplashPotion = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD);
        ItemStack poisonSplashPotion = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.POISON);

        ItemStack awkwardLingeringPotion = PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD);
        ItemStack poisonLingeringPotion = PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.POISON);

        addBrewingRecipe(recipes, recipeFactory, ingredient, awkwardPotion, poisonPotion);
        addBrewingRecipe(recipes, recipeFactory, ingredient, awkwardSplashPotion, poisonSplashPotion);
        addBrewingRecipe(recipes, recipeFactory, ingredient, awkwardLingeringPotion, poisonLingeringPotion);

        registration.addRecipes(RecipeTypes.BREWING, recipes);
    }

    private void addBrewingRecipe(List<IJeiBrewingRecipe> recipes, IVanillaRecipeFactory recipeFactory,
                                  List<ItemStack> ingredient, ItemStack input, ItemStack output) {
        recipes.add(recipeFactory.createBrewingRecipe(ingredient, Collections.singletonList(input), output));
    }
}
