package com.cerbon.queen_bee.client.sound;

import com.cerbon.queen_bee.entity.custom.QueenBeeEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QueenBeeFlyingSoundInstance extends QueenBeeSoundInstance{
    public QueenBeeFlyingSoundInstance(QueenBeeEntity queenBee) {
        super(queenBee, SoundEvents.BEE_LOOP, SoundSource.NEUTRAL);
    }

    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return new QueenBeeAggressiveSoundInstance(this.queenBee);
    }

    @Override
    protected boolean shouldSwitchSounds() {
        return this.queenBee.isAngry();
    }
}
