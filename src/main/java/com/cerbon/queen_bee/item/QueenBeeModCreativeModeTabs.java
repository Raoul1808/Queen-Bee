package com.cerbon.queen_bee.item;

import com.cerbon.queen_bee.QueenBeeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class QueenBeeModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            QueenBeeMod.MOD_ID);

    public static RegistryObject<CreativeModeTab> QUEEN_BEE_TAB = CREATIVE_MODE_TABS.register("queen_bee_tab", ()->
            CreativeModeTab.builder()
                    .icon(()-> new ItemStack(QueenBeeModItems.STINGER.get()))
                    .title(Component.translatable("creativemodetab." + QueenBeeMod.MOD_ID + ".queen_bee_tab"))
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
