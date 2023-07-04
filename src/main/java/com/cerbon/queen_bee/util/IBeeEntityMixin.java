package com.cerbon.queen_bee.util;

public interface IBeeEntityMixin {
    int getDespawnTime();
    void setDespawnTime(int ticks);
    boolean canDespawn();
    void setCanDespawn(boolean canDespawn);
}
