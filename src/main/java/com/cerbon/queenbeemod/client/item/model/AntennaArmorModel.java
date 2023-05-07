package com.cerbon.queenbeemod.client.item.model;

import com.cerbon.queenbeemod.QueenBeeMod;
import com.cerbon.queenbeemod.item.custom.AntennaArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AntennaArmorModel extends GeoModel<AntennaArmorItem> {
    @Override
    public ResourceLocation getModelResource(AntennaArmorItem animatable) {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "geo/antenna.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AntennaArmorItem animatable) {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "textures/armor/antenna.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AntennaArmorItem animatable) {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "animations/antenna.animation.json");
    }
}
