package com.cerbon.queen_bee.item.custom;

import com.cerbon.queen_bee.QueenBeeMod;
import com.cerbon.queen_bee.config.QueenBeeModCommonConfigs;
import com.cerbon.queen_bee.item.QueenBeeModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StingerSwordItem extends SwordItem {
    public StingerSwordItem(int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(new Tier() {
            @Override
            public int getUses() {
                return 250;
            }

            @Override
            public float getSpeed() {
                return 2.0F;
            }

            @Override
            public float getAttackDamageBonus() {
                return 0;
            }

            @Override
            public int getLevel() {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 12;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }
        }, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (QueenBeeModCommonConfigs.ENABLE_POISON_EFFECT.get()) {
            pTarget.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        }
        if (QueenBeeModCommonConfigs.ENABLE_NAUSEA_EFFECT.get()) {
            pTarget.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (QueenBeeModCommonConfigs.ENABLE_CURE_BEE.get()) {
            if (pPlayer.isCrouching() && pPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() == QueenBeeModItems.ANTENNA.get()) {
                Bee bee = EntityType.BEE.create(pLevel);
                if (bee != null) {
                    bee.moveTo(pPlayer.getX(), pPlayer.getY() + 1, pPlayer.getZ());
                    bee.setInvulnerable(true);
                    bee.setNoAi(true);
                    pLevel.addFreshEntity(bee);
                }
                AreaEffectCloud areaEffectCloud = new AreaEffectCloud(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
                areaEffectCloud.setOwner(pPlayer);
                areaEffectCloud.setDuration(200);
                areaEffectCloud.setRadius(4.0F);
                areaEffectCloud.setFixedColor(13458603);
                areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 1));
                pLevel.addFreshEntity(areaEffectCloud);

                pPlayer.swing(pUsedHand);
                pPlayer.getCooldowns().addCooldown(this, QueenBeeModCommonConfigs.STINGER_SWORD_COOLDOWN.get());

                // I think this is not the best way to do it. Not sure to be honest.
                // TODO: Find a better way to schedule the bee death
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bee.remove(Entity.RemovalReason.KILLED);
                    }
                }, 11700);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (QueenBeeModCommonConfigs.ENABLE_CURE_BEE.get() && QueenBeeModCommonConfigs.ENABLE_STINGER_SWORD_TOOLTIP.get()) {
            if (Screen.hasShiftDown()) {
                pTooltipComponents.add(Component.translatable("tooltip." + QueenBeeMod.MOD_ID + ".stinger_sword").withStyle(ChatFormatting.YELLOW));
            } else {
                pTooltipComponents.add(Component.translatable("tooltip." + QueenBeeMod.MOD_ID + ".item.shift_up").withStyle(ChatFormatting.YELLOW));
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
