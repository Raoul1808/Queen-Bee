package com.cerbon.queenbeemod.client.entity.model;

import com.cerbon.queenbeemod.QueenBeeMod;
import com.cerbon.queenbeemod.entity.custom.QueenBeeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class QueenBeeModel extends GeoModel<QueenBeeEntity> {
    @Override
    public ResourceLocation getModelResource(QueenBeeEntity animatable) {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "geo/queen_bee.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(QueenBeeEntity animatable) {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "textures/entity/queen_bee.png");
    }

    @Override
    public ResourceLocation getAnimationResource(QueenBeeEntity animatable) {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "animations/queen_bee.animation.json");
    }
}
