package com.cerbon.queenbeemod;

import com.cerbon.queenbeemod.client.entity.renderer.QueenBeeRenderer;
import com.cerbon.queenbeemod.entity.QueenBeeModEntities;
import com.cerbon.queenbeemod.item.QueenBeeCreativeModeTabs;
import com.cerbon.queenbeemod.item.QueenBeeModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(QueenBeeMod.MOD_ID)
public class QueenBeeMod
{
    public static final String MOD_ID = "queen_bee";
    private static final Logger LOGGER = LogUtils.getLogger();

    public QueenBeeMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        QueenBeeModItems.register(modEventBus);
        QueenBeeModEntities.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreativeTab);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreativeTab(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == QueenBeeCreativeModeTabs.QUEEN_BEE_TAB){
            event.accept(QueenBeeModItems.STINGER);
            event.accept(QueenBeeModItems.QUEEN_BEE_SPAWN_EGG);
            event.accept(QueenBeeModItems.STINGER_SWORD);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(QueenBeeModEntities.QUEEN_BEE.get(), QueenBeeRenderer::new);
        }
    }
}
