package com.cerbon.queen_bee.client.entity.model;

import com.cerbon.queen_bee.entity.custom.QueenBeeEntity;
import com.cerbon.queen_bee.util.QBConstants;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class QueenBeeModel extends GeoModel<QueenBeeEntity> {
    @Override
    public ResourceLocation getModelResource(QueenBeeEntity animatable) {
        return new ResourceLocation(QBConstants.MOD_ID, QBConstants.QUEEN_BEE_MODEL_PATH);
    }

    @Override
    public ResourceLocation getTextureResource(QueenBeeEntity animatable) {
        return new ResourceLocation(QBConstants.MOD_ID, QBConstants.QUEEN_BEE_TEXTURE_PATH);
    }

    @Override
    public ResourceLocation getAnimationResource(QueenBeeEntity animatable) {
        return new ResourceLocation(QBConstants.MOD_ID, QBConstants.QUEEN_BEE_ANIMATION_PATH);
    }
}
