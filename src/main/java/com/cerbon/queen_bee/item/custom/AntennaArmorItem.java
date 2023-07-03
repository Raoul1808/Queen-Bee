package com.cerbon.queen_bee.item.custom;

import com.cerbon.queen_bee.QueenBeeMod;
import com.cerbon.queen_bee.client.item.renderer.AntennaArmorRenderer;
import com.cerbon.queen_bee.config.QBCommonConfigs;
import com.cerbon.queen_bee.util.QBConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.function.Consumer;

public class AntennaArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public AntennaArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AntennaArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                   EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new AntennaArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if (QBCommonConfigs.ENABLE_ANTENNA.get() && QBCommonConfigs.ENABLE_ANTENNA_TOOLTIP.get()){
            if (Screen.hasShiftDown()){
                pTooltipComponents.add(Component.translatable(QBConstants.ANTENNA_SHIFT_DOWN_TOOLTIP).withStyle(ChatFormatting.YELLOW));
            }else {
                pTooltipComponents.add(Component.translatable(QBConstants.ITEM_SHIFT_UP_TOOLTIP).withStyle(ChatFormatting.YELLOW));
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private <T extends GeoAnimatable> PlayState predicate(@NotNull AnimationState<T> animationState){
     animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
     return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
