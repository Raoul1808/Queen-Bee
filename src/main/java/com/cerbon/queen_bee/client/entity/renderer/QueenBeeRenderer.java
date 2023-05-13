package com.cerbon.queen_bee.client.entity.renderer;

import com.cerbon.queen_bee.QueenBeeMod;
import com.cerbon.queen_bee.client.entity.model.QueenBeeModel;
import com.cerbon.queen_bee.entity.custom.QueenBeeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QueenBeeRenderer extends GeoEntityRenderer<QueenBeeEntity> {
    public QueenBeeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QueenBeeModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(QueenBeeEntity entity) {
        if(entity.isAngry()){
            return new ResourceLocation(QueenBeeMod.MOD_ID, "textures/entity/queen_bee_angry.png");
        }else {
            return new ResourceLocation(QueenBeeMod.MOD_ID, "textures/entity/queen_bee.png");
        }
    }
}
