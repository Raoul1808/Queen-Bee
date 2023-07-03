package com.cerbon.queen_bee.event;

import com.cerbon.queen_bee.QueenBeeMod;
import com.cerbon.queen_bee.entity.QBEntities;
import com.cerbon.queen_bee.entity.custom.QueenBeeEntity;
import com.cerbon.queen_bee.recipe.brewing.StingerToPoisonPotionBrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = QueenBeeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QBEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(@NotNull EntityAttributeCreationEvent event){
        event.put(QBEntities.QUEEN_BEE.get(), QueenBeeEntity.setAttribute());
    }
    @SubscribeEvent
    public static void onCommonSetup(@NotNull FMLCommonSetupEvent event){
        event.enqueueWork(()-> BrewingRecipeRegistry.addRecipe(new StingerToPoisonPotionBrewingRecipe()));
    }
}
