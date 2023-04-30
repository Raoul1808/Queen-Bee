package com.cerbon.queenbeemod.client.entity.renderer;

import com.cerbon.queenbeemod.QueenBeeMod;
import com.cerbon.queenbeemod.client.entity.model.QueenBeeModel;
import com.cerbon.queenbeemod.entity.custom.QueenBeeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QueenBeeRenderer extends GeoEntityRenderer<QueenBeeEntity> {
    public QueenBeeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QueenBeeModel());
    }

    @Override
    public ResourceLocation getTextureLocation(QueenBeeEntity entity) {
        if(entity.isAngry()){
            return new ResourceLocation(QueenBeeMod.MOD_ID, "textures/entity/queen_bee_angry.png");
        }else {
            return new ResourceLocation(QueenBeeMod.MOD_ID, "textures/entity/queen_bee.png");
        }
    }
}
