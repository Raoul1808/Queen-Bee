package com.cerbon.queen_bee.loot;

import com.cerbon.queen_bee.QueenBeeMod;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class QueenBeeModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, QueenBeeMod.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_STINGER_DROP_TO_BEE_LOOT_TABLE =
            LOOT_MODIFIERS_SERIALIZERS.register("add_stinger_drop_to_bee_loot_table", AddItemModifier.CODEC);

    public static void register(IEventBus eventBus){
        LOOT_MODIFIERS_SERIALIZERS.register(eventBus);
    }
}
