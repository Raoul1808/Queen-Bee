package com.cerbon.queenbeemod.entity.client;

import com.cerbon.queenbeemod.QueenBeeMod;
import com.cerbon.queenbeemod.entity.custom.QueenBeeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QueenBeeRenderer extends GeoEntityRenderer<QueenBeeEntity> {
    public QueenBeeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QueenBeeModel());
    }

    @Override
    public ResourceLocation getTextureLocation(QueenBeeEntity animatable) {
        return new ResourceLocation(QueenBeeMod.MOD_ID, "textures/entity/queen_bee.png");
    }
}
