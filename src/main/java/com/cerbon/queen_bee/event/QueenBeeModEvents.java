package com.cerbon.queen_bee.event;

import com.cerbon.queen_bee.QueenBeeMod;
import com.cerbon.queen_bee.entity.QueenBeeModEntities;
import com.cerbon.queen_bee.entity.custom.QueenBeeEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid = QueenBeeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QueenBeeModEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event){
        event.put(QueenBeeModEntities.QUEEN_BEE.get(), QueenBeeEntity.setAttribute());
    }
}
