package fr.samlegamer.metalapples.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.samlegamer.metalapples.MetalApple;
import fr.samlegamer.metalapples.item.MAApple;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MAJsons
{
    private final Path configDirMA;
    private final Path configDirMAVanilla;
    private final Path configDirMACustom;

    public MAJsons(Path configDir)
    {
        this.configDirMA = Path.of(configDir.toString(), MetalApple.MODID);
        this.configDirMAVanilla = Path.of(configDir.toString(), MetalApple.MODID, "vanilla");
        this.configDirMACustom = Path.of(configDir.toString(), MetalApple.MODID, "custom");
    }

    public Path getConfigDirMA()
    {
        return configDirMA;
    }

    public Path getConfigDirMAVanilla()
    {
        return configDirMAVanilla;
    }

    public Path getConfigDirMACustom()
    {
        return configDirMACustom;
    }

    public void makeVanillaJsons()
    {
        mkFile(configDirMAVanilla, "copper_apple", 4, 1.2F, true, new MobEffectInstance(MobEffects.HASTE, 600, 0));
        mkFile(configDirMAVanilla, "iron_apple", 4, 1.2F, true, new MobEffectInstance(MobEffects.RESISTANCE, 1600, 0));
        mkFile(configDirMAVanilla, "lapis_apple", 4, 1.2F, true, new MobEffectInstance(MobEffects.NIGHT_VISION, 2000, 0));
        mkFile(configDirMAVanilla, "redstone_apple", 4, 1.2F, true, new MobEffectInstance(MobEffects.SPEED, 2600, 0));
        mkFile(configDirMAVanilla, "diamond_apple", 10, 1.2F, true,
                new MobEffectInstance(MobEffects.SPEED, 1800, 1),
                new MobEffectInstance(MobEffects.ABSORPTION, 1800, 2),
                new MobEffectInstance(MobEffects.RESISTANCE, 1800, 0),
                new MobEffectInstance(MobEffects.REGENERATION, 300, 2),
                new MobEffectInstance(MobEffects.HASTE, 1800, 1)
        );
        mkFile(configDirMAVanilla, "netherite_apple", 12, 1.4F, true,
                new MobEffectInstance(MobEffects.SPEED, 1800, 4),     // 90s, Speed II (durée x1.5)
                new MobEffectInstance(MobEffects.ABSORPTION, 1800, 3),         // 90s, Absorption III
                new MobEffectInstance(MobEffects.RESISTANCE, 1800, 0), // 90s, Resistance I
                new MobEffectInstance(MobEffects.REGENERATION, 300, 3),       // 15s, Regeneration III (légère hausse)
                new MobEffectInstance(MobEffects.HASTE, 1800, 2),          // 90s, Haste II
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 4000, 0));
        mkFile(configDirMACustom, "emerald_apple", 4, 1.2F, true, new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 1000, 0));
    }

    private void mkFile(Path path, String apple, int nutrition, float saturationMod, boolean alwaysEat, MobEffectInstance... effects) {

        if(Files.exists(configDirMA) && Files.exists(configDirMAVanilla) && Files.isDirectory(configDirMACustom))
        {
            if(!Files.exists(path.resolve(apple+".json"))) {
                try {
                    BufferedWriter fileWriter = Files.newBufferedWriter(path.resolve(apple + ".json"));
                    try (JsonWriter writer = new JsonWriter(fileWriter)) {
                        writer.setIndent("  ");
                        writer.beginObject();
                        writer.name("nutrition").value(nutrition);
                        writer.name("saturationMod").value(saturationMod);
                        writer.name("alwaysEat").value(alwaysEat);

                        writer.name("effects");
                        writer.beginArray();
                        for (MobEffectInstance effect : effects) {
                            writer.beginObject();
                            writer.name("nameEff").value(Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value())).toString());
                            writer.name("duration").value(effect.getDuration());
                            writer.name("amplifier").value(effect.getAmplifier());
                            writer.endObject();
                        }
                        writer.endArray();
                        writer.endObject();
                    }
                } catch (IOException e) {
                    MetalApple.LOGGER.error(e);
                }
            }
        }
    }

    public static MAApple loadApple(String configDir, String apple)
    {
        Path apple_json = Path.of(configDir, apple+".json");
        if(Files.exists(apple_json))
        {
            try {
                BufferedReader fileReader = Files.newBufferedReader(apple_json);
                try(JsonReader reader = new JsonReader(fileReader)){
                    reader.beginObject();
                    int nutrition = 0;
                    float saturationMod = 0.0F;
                    boolean alwaysEat = false;
                    List<MobEffectInstance> effects = new ArrayList<>();

                    while (reader.hasNext())
                    {
                        String name = reader.nextName();
                        switch (name)
                        {
                            case "nutrition":
                                nutrition = reader.nextInt();
                                break;
                            case "saturationMod":
                                saturationMod = (float)reader.nextDouble();
                                break;
                            case "alwaysEat":
                                alwaysEat = reader.nextBoolean();
                                break;
                            case "effects":
                                reader.beginArray();
                                while (reader.hasNext())
                                {
                                    reader.beginObject();
                                    String nameEff = "";
                                    int duration = 0;
                                    int amplifier = 0;
                                    while (reader.hasNext())
                                    {
                                        switch (reader.nextName())
                                        {
                                            case "nameEff":
                                                nameEff = reader.nextString();
                                                break;
                                            case "duration":
                                                duration = reader.nextInt();
                                                break;
                                            case "amplifier":
                                                amplifier = reader.nextInt();
                                                break;
                                        }
                                    }
                                    reader.endObject();
                                    effects.add(new MobEffectInstance(getEffect(nameEff), duration, amplifier));
                                }
                                reader.endArray();
                                break;
                            default:
                                reader.skipValue();
                        }
                    }
                    reader.endObject();


                    MobEffectInstance[] tabOfEffect = new MobEffectInstance[effects.size()];
                    for(int i = 0; i < effects.size(); i++)
                    {
                        tabOfEffect[i] = effects.get(i);
                    }

                    return new MAApple(apple, nutrition, saturationMod, alwaysEat, tabOfEffect);
                }
            }catch (IOException e)
            {
                MetalApple.LOGGER.error(e);
            }
        }
        return new MAApple("null", 0, 0, false);
    }

    private static Holder<MobEffect> getEffect(String effect)
    {
        if(effect.contains("minecraft"))
        {
            switch (effect.split(":")[1])
            {
                case "speed":
                    return MobEffects.SPEED;
                case "slowness":
                    return MobEffects.SLOWNESS;
                case "haste":
                    return MobEffects.HASTE;
                case "mining_fatigue":
                    return MobEffects.MINING_FATIGUE;
                case "strength":
                    return MobEffects.STRENGTH;
                case "instant_health":
                    return MobEffects.INSTANT_HEALTH;
                case "instant_damage":
                    return MobEffects.INSTANT_DAMAGE;
                case "jump_boost":
                    return MobEffects.JUMP_BOOST;
                case "nausea":
                    return MobEffects.NAUSEA;
                case "regeneration":
                    return MobEffects.REGENERATION;
                case "resistance":
                    return MobEffects.RESISTANCE;
                case "fire_resistance":
                    return MobEffects.FIRE_RESISTANCE;
                case "water_breathing":
                    return MobEffects.WATER_BREATHING;
                case "invisibility":
                    return MobEffects.INVISIBILITY;
                case "blindness":
                    return MobEffects.BLINDNESS;
                case "night_vision":
                    return MobEffects.NIGHT_VISION;
                case "hunger":
                    return MobEffects.HUNGER;
                case "weakness":
                    return MobEffects.WEAKNESS;
                case "poison":
                    return MobEffects.POISON;
                case "wither":
                    return MobEffects.WITHER;
                case "health_boost":
                    return MobEffects.HEALTH_BOOST;
                case "absorption":
                    return MobEffects.ABSORPTION;
                case "saturation":
                    return MobEffects.SATURATION;
                case "glowing":
                    return MobEffects.GLOWING;
                case "levitation":
                    return MobEffects.LEVITATION;
                case "luck":
                    return MobEffects.LUCK;
                case "unluck":
                    return MobEffects.UNLUCK;
                case "slow_falling":
                    return MobEffects.SLOW_FALLING;
                case "conduit_power":
                    return MobEffects.CONDUIT_POWER;
                case "dolphins_grace":
                    return MobEffects.DOLPHINS_GRACE;
                case "bad_omen":
                    return MobEffects.BAD_OMEN;
                case "hero_of_the_village":
                    return MobEffects.HERO_OF_THE_VILLAGE;
                case "darkness":
                    return MobEffects.DARKNESS;
                default:
                    return MobEffects.CONDUIT_POWER;
            }
        }
        else {
            if(BuiltInRegistries.MOB_EFFECT.get(Identifier.parse(effect)) != null)
            {
                var mobEffect = Holder.direct(BuiltInRegistries.MOB_EFFECT.get(Identifier.parse(effect)));
                if(mobEffect.value().isPresent())
                {
                    return mobEffect.value().get();
                }
                else
                {
                    return MobEffects.CONDUIT_POWER;
                }
            }
            else {
                MetalApple.LOGGER.error("No such effect: {}, make default conduit power effect", effect);
                return MobEffects.CONDUIT_POWER;
            }
        }
    }
}