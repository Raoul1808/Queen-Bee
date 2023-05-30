package com.cerbon.queen_bee.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QueenBeeModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_QUEEN_BEE_BOSS_BAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BEES_DROPPING_STINGER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SUMMON_ANGRY_BEES;
    public static final ForgeConfigSpec.ConfigValue<Integer> SUMMON_ANGRY_BEES_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_POISON_NIMBUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> TARGET_DISTANCE_TO_SQR;
    public static final ForgeConfigSpec.ConfigValue<Integer> POISON_NIMBUS_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> POISON_NIMBUS_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> POISON_EFFECT_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> POISON_EFFECT_AMPLIFIER;
    public static final ForgeConfigSpec.ConfigValue<Integer> POISON_NIMBUS_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> ANGRY_BEES_AMOUNT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_CURE_BEE;
    public static final ForgeConfigSpec.ConfigValue<Integer> REGENERATION_AREA_EFFECT_CLOUD_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> REGENERATION_AREA_EFFECT_CLOUD_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Integer> REGENERATION_EFFECT_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> REGENERATION_EFFECT_AMPLIFIER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_NAUSEA_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Integer> NAUSEA_EFFECT_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> STINGER_SWORD_POISON_EFFECT_DURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> STINGER_SWORD_POISON_EFFECT_AMPLIFIER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_POISON_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Integer> STINGER_SWORD_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_STINGER_SWORD_TOOLTIP;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ANTENNA;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ANTENNA_BUMBLEZONE_DIMENSION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ANTENNA_TOOLTIP;

    static {
        BUILDER.push("Queen Bee Configs");

        ENABLE_QUEEN_BEE_BOSS_BAR = BUILDER.comment("If true enables Queen Bee boss bar. DEFAULT: FALSE")
                        .define("Enable Boss Bar",false);
        ENABLE_BEES_DROPPING_STINGER = BUILDER.comment("If false bees will not drop stinger. DEFAULT: TRUE")
                .define("Enable Bees Dropping Stinger", true);

        BUILDER.push("Summon Angry Bees Attack");

        ENABLE_SUMMON_ANGRY_BEES = BUILDER.comment("If false Queen Bee will not summon angry bees during battle. DEFAULT: TRUE")
                        .define("Enable Summon Angry Bees", true);
        ANGRY_BEES_AMOUNT = BUILDER.comment("Sets the amount of angry bees to spawn. DEFAULT: 3")
                .define("Angry Bees Amount", 3);
        SUMMON_ANGRY_BEES_COOLDOWN = BUILDER.comment("Sets the cooldown of the attack in ticks. DEFAULT: 200")
                        .define("Summon Angry Bees Cooldown", 200);

        BUILDER.pop();

        BUILDER.push("Poison Nimbus Attack");

        ENABLE_POISON_NIMBUS = BUILDER.comment("If false Queen Bee will not summon a poison nimbus during battle. DEFAULT: TRUE")
                        .define("Enable Poison Nimbus", true);
        TARGET_DISTANCE_TO_SQR = BUILDER.comment("Sets the maximum distance entity can be for the attack to be executed. The real value passed here is the square root of this value, e.g. 49 = 7 blocks of distance. DEFAULT: 49")
                .define("Target Maximum Distance", 49);
        POISON_NIMBUS_DURATION = BUILDER.comment("Sets the poison nimbus area effect cloud duration in ticks. DEFAULT: 40")
                .define("Poison Nimbus Duration", 40);
        POISON_NIMBUS_RADIUS = BUILDER.comment("Sets the area effect cloud size. DEFAULT: 7")
                .define("Poison Nimbus Radius", 7);
        POISON_EFFECT_DURATION = BUILDER.comment("Sets the duration in ticks of the poison effect. DEFAULT: 200")
                .define("Poison Effect Duration", 200);
        POISON_EFFECT_AMPLIFIER = BUILDER.comment("Sets the amplifier of the poison effect. DEFAULT: 1")
                .define("Poison Effect Amplifier", 1);
        POISON_NIMBUS_COOLDOWN = BUILDER.comment("Sets the cooldown of the attack in ticks. DEFAULT: 200")
                        .define("Poison Nimbus Cooldown", 200);

        BUILDER.pop(2);

        BUILDER.push("Stinger Sword Configs");

        ENABLE_CURE_BEE = BUILDER.comment("If false right click ability of stinger sword will not work. DEFAULT: TRUE")
                .define("Enable Cure Bee", true);
        REGENERATION_AREA_EFFECT_CLOUD_DURATION = BUILDER.comment("Sets the regeneration area effect cloud duration in ticks. DEFAULT: 200")
                .define("Regeneration Area Effect Cloud Duration", 200);
        REGENERATION_AREA_EFFECT_CLOUD_RADIUS = BUILDER.comment("Sets the regeneration area effect cloud size. DEFAULT: 4")
                .define("Regeneration Area Effect Cloud Radius", 4);
        REGENERATION_EFFECT_DURATION = BUILDER.comment("Sets the duration in ticks of the regeneration effect. DEFAULT: 40 ")
                .define("Regeneration Effect Duration", 40);
        REGENERATION_EFFECT_AMPLIFIER = BUILDER.comment("Sets the amplifier of the regeneration effect. DEFAULT: 1")
                .define("Regeneration Effect Amplifier", 1);
        ENABLE_NAUSEA_EFFECT = BUILDER.comment("If false stinger sword will not inflict nausea effect. DEFAULT: TRUE")
                        .define("Enable Nausea Effect", true);
        NAUSEA_EFFECT_DURATION = BUILDER.comment("Sets the duration in ticks of the nausea effect. DEFAULT: 300")
                .define("Nausea Effect Duration", 300);
        ENABLE_POISON_EFFECT = BUILDER.comment("If false stinger sword will not inflict poison effect. DEFAULT: TRUE")
                        .define("Enable Poison Effect", true);
        STINGER_SWORD_POISON_EFFECT_DURATION = BUILDER.comment("Sets the duration in ticks of the poison effect. DEFAULT: 100")
                .define("Poison Effect Duration", 100);
        STINGER_SWORD_POISON_EFFECT_AMPLIFIER = BUILDER.comment("Sets the amplifier of the poison effect. DEFAULT: 0")
                .define("Poison Effect Amplifier", 0);
        STINGER_SWORD_COOLDOWN = BUILDER.comment("Sets Stinger Sword cooldown in ticks after using right click ability. DEFAULT: 900")
                        .define("Stinger Sword Cooldown", 900);
        ENABLE_STINGER_SWORD_TOOLTIP = BUILDER.comment("If false will disable stinger sword tooltip. DEFAULT: TRUE")
                        .define("Enable Stinger Sword Tooltip", true);

        BUILDER.pop();

        BUILDER.push("Antenna Configs");

        ENABLE_ANTENNA = BUILDER.comment("If false antenna will not prevent bees aggro on you. DEFAULT: TRUE")
                        .define("Enable Antenna", true);
        ENABLE_ANTENNA_BUMBLEZONE_DIMENSION = BUILDER.comment("If false will disable antenna only in the bumblezone dimension. DEFAULT: TRUE")
                        .define("Enable Antenna Bumblezone Dimension", true);
        ENABLE_ANTENNA_TOOLTIP = BUILDER.comment("If false will disable antenna tooltip. DEFAULT: TRUE")
                        .define("Enable Antenna Tooltip", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
