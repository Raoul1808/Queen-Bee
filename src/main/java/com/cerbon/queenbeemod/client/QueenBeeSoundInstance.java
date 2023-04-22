package com.cerbon.queenbeemod.client;

import com.cerbon.queenbeemod.entity.custom.QueenBeeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class QueenBeeSoundInstance extends AbstractTickableSoundInstance {
    protected final QueenBeeEntity queenBee;
    private boolean hasSwitched;
    protected QueenBeeSoundInstance(QueenBeeEntity queenBee, SoundEvent pSoundEvent, SoundSource pSource) {
        super(pSoundEvent, pSource, SoundInstance.createUnseededRandom());
        this.queenBee = queenBee;
        this.x = (double)((float)queenBee.getX());
        this.y = (double)((float)queenBee.getY());
        this.z = (double)((float)queenBee.getZ());
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public void tick() {
        boolean flag = this.shouldSwitchSounds();
        if (flag && !this.isStopped()) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(this.getAlternativeSoundInstance());
            this.hasSwitched = true;
        }

        if (!this.queenBee.isRemoved() && !this.hasSwitched) {
            this.x = (double)((float)this.queenBee.getX());
            this.y = (double)((float)this.queenBee.getY());
            this.z = (double)((float)this.queenBee.getZ());
            float f = (float)this.queenBee.getDeltaMovement().horizontalDistance();
            if (f >= 0.01F) {
                this.pitch = Mth.lerp(Mth.clamp(f, 0.7F, 1.0F), 0.7F, 1.0F);
                this.volume = Mth.lerp(Mth.clamp(f, 0.0F, 0.5F), 0.0F, 1.0F);
            } else {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }

        } else {
            this.stop();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.queenBee.isSilent();
    }

    protected abstract AbstractTickableSoundInstance getAlternativeSoundInstance();

    protected abstract boolean shouldSwitchSounds();
}
