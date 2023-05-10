package com.cerbon.queen_bee.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QueenBeeModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Queen Bee Entity Configs");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
