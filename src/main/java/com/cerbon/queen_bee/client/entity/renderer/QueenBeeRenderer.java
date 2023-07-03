package com.cerbon.queen_bee.client.entity.renderer;

import com.cerbon.queen_bee.client.entity.model.QueenBeeModel;
import com.cerbon.queen_bee.entity.custom.QueenBeeEntity;
import com.cerbon.queen_bee.util.QBConstants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QueenBeeRenderer extends GeoEntityRenderer<QueenBeeEntity> {
    public QueenBeeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QueenBeeModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull QueenBeeEntity entity) {
        if(entity.isAngry()){
            return new ResourceLocation(QBConstants.MOD_ID, QBConstants.QUEEN_BEE_ANGRY_TEXTURE_PATH);
        }else {
            return new ResourceLocation(QBConstants.MOD_ID, QBConstants.QUEEN_BEE_TEXTURE_PATH);
        }
    }
}
