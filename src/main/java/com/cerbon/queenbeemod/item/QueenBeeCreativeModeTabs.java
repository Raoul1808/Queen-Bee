package com.cerbon.queenbeemod.item;

import com.cerbon.queenbeemod.QueenBeeMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QueenBeeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QueenBeeCreativeModeTabs {
    public static CreativeModeTab QUEEN_BEE_TAB;
    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event){
        QUEEN_BEE_TAB = event.registerCreativeModeTab(new ResourceLocation(QueenBeeMod.MOD_ID, "queen_bee_tab"),
                builder -> builder.icon(()-> new ItemStack(QueenBeeModItems.STINGER.get()))
                        .title(Component.translatable("creativemodetab.queen_bee_tab")));
    }
}
