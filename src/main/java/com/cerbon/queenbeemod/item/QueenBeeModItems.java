package com.cerbon.queenbeemod.item;

import com.cerbon.queenbeemod.QueenBeeMod;
import com.cerbon.queenbeemod.entity.QueenBeeModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class QueenBeeModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, QueenBeeMod.MOD_ID);

    public static final RegistryObject<Item> STINGER = ITEMS.register("stinger",
            () -> new Item(new Item.Properties().food(QueenBeeModFoods.STINGER)));

    public static final RegistryObject<Item> QUEEN_BEE_SPAWN_EGG = ITEMS.register("queen_bee_spawn_egg",
            () -> new ForgeSpawnEggItem(QueenBeeModEntities.QUEEN_BEE, 0xEDC343, 0x1E1E28,
                    new Item.Properties()));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
