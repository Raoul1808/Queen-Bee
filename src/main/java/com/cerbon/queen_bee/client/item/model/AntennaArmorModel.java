package com.cerbon.queen_bee.client.item.model;

import com.cerbon.queen_bee.item.custom.AntennaArmorItem;
import com.cerbon.queen_bee.util.QBConstants;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AntennaArmorModel extends GeoModel<AntennaArmorItem> {
    @Override
    public ResourceLocation getModelResource(AntennaArmorItem animatable) {
        return new ResourceLocation(QBConstants.MOD_ID, QBConstants.ANTENNA_MODEL_PATH);
    }

    @Override
    public ResourceLocation getTextureResource(AntennaArmorItem animatable) {
        return new ResourceLocation(QBConstants.MOD_ID, QBConstants.ANTENNA_TEXTURE_PATH);
    }

    @Override
    public ResourceLocation getAnimationResource(AntennaArmorItem animatable) {
        return new ResourceLocation(QBConstants.MOD_ID, QBConstants.ANTENNA_ANIMATION_PATH);
    }
}
