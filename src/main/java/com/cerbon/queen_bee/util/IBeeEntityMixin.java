package com.cerbon.queen_bee.util;

public interface IBeeEntityMixin {
    int queen_bee_getDespawnTime();
    void queen_bee_setDespawnTime(int ticks);
    boolean queen_bee_canDespawn();
    void queen_bee_setCanDespawn(boolean canDespawn);
}
