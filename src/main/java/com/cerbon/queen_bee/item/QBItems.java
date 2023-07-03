package com.cerbon.queen_bee.item;

import com.cerbon.queen_bee.QueenBeeMod;
import com.cerbon.queen_bee.entity.QBEntities;
import com.cerbon.queen_bee.item.custom.AntennaArmorItem;
import com.cerbon.queen_bee.item.custom.StingerSwordItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class QBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, QueenBeeMod.MOD_ID);

    public static final RegistryObject<Item> STINGER = ITEMS.register("stinger",
            () -> new Item(new Item.Properties().food(QBFoods.STINGER)));

    public static final RegistryObject<Item> QUEEN_BEE_SPAWN_EGG = ITEMS.register("queen_bee_spawn_egg",
            () -> new ForgeSpawnEggItem(QBEntities.QUEEN_BEE, 0xffffff, 0xffffff,
                    new Item.Properties()));

    public static final RegistryObject<Item> STINGER_SWORD = ITEMS.register("stinger_sword",
            () -> new StingerSwordItem(3, -2.0F, new Item.Properties()));

    public static final RegistryObject<Item> ANTENNA = ITEMS.register("antenna",
            () -> new AntennaArmorItem(QBArmorMaterials.ANTENNA, ArmorItem.Type.HELMET, new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
