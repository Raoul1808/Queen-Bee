package com.cerbon.queen_bee.client.event;

import com.cerbon.queen_bee.client.entity.renderer.QueenBeeRenderer;
import com.cerbon.queen_bee.entity.QBEntities;
import com.cerbon.queen_bee.util.QBConstants;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = QBConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class QBClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(QBEntities.QUEEN_BEE.get(), QueenBeeRenderer::new);
    }
}
