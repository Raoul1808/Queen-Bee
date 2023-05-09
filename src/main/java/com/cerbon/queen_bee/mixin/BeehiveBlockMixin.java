package com.cerbon.queen_bee.mixin;

import com.cerbon.queen_bee.item.QueenBeeModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin {
    @Shadow protected abstract boolean hiveContainsBees(Level pLevel, BlockPos pPos);

    @Shadow protected abstract void angerNearbyBees(Level pLevel, BlockPos pPos);

    @Shadow public abstract void releaseBeesAndResetHoneyLevel(Level pLevel, BlockState pState, BlockPos pPos, @Nullable Player pPlayer, BeehiveBlockEntity.BeeReleaseStatus pBeeReleaseStatus);

    @Shadow public abstract void resetHoneyLevel(Level pLevel, BlockState pState, BlockPos pPos);

    @Inject(method = "use", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/CampfireBlock;isSmokeyPos(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"),
            cancellable = true)
    public void preventBeesFromLeavingBeeHive(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                                              InteractionHand pHand, BlockHitResult pHit,
                                              CallbackInfoReturnable<InteractionResult> cir)
    {
        if (!(pPlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() == (QueenBeeModItems.ANTENNA.get()) || CampfireBlock.isSmokeyPos(pLevel, pPos))) {
            if (this.hiveContainsBees(pLevel, pPos)) {
                this.angerNearbyBees(pLevel, pPos);
            }
            this.releaseBeesAndResetHoneyLevel(pLevel, pState, pPos, pPlayer, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
        }else {
            this.resetHoneyLevel(pLevel, pState, pPos);
        }

        cir.setReturnValue(InteractionResult.sidedSuccess(pLevel.isClientSide));
    }
}
