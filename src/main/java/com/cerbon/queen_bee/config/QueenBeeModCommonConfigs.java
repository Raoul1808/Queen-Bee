package com.cerbon.queen_bee.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QueenBeeModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_CURE_BEE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_NAUSEA_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_POISON_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Integer> STINGER_SWORD_COOLDOWN;

    static {

        BUILDER.push("Stinger Sword Configs");

        ENABLE_CURE_BEE = BUILDER.comment("If false right click ability of stinger sword will not work. DEFAULT: TRUE")
                .define("Enable Cure Bee", true);
        ENABLE_NAUSEA_EFFECT = BUILDER.comment("If false stinger sword will not inflict nausea effect. DEFAULT: TRUE")
                        .define("Enable Nausea Effect", true);
        ENABLE_POISON_EFFECT = BUILDER.comment("If false stinger sword will not inflict poison effect. DEFAULT: TRUE")
                        .define("Enable Poison Effect", true);
        STINGER_SWORD_COOLDOWN = BUILDER.comment("Sets Stinger Sword cooldown in ticks after using right click ability. DEFAULT: 900")
                        .define("Stinger Sword Cooldown", 900);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
