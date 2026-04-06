package fr.samlegamer.metalapples.item;

import fr.samlegamer.metalapples.MetalApple;
import fr.samlegamer.metalapples.config.MAJsons;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MAItemsRegistry {
    public static Map<String, MAApple> getVanillaAppleConfigs(String configDir) {
        Map<String, MAApple> map = new HashMap<>();
        String[] appleTypes = new String[]{
                "copper_apple",
                "iron_apple",
                "lapis_apple",
                "redstone_apple",
                "diamond_apple",
                "netherite_apple"
        };

        for (String appleType : appleTypes) {
            MAApple maApple = MAJsons.loadApple(configDir + File.separator + MetalApple.MODID + File.separator + "vanilla" + File.separator, appleType);
            map.put(appleType, maApple);
        }

        return map;
    }

    public static Map<String, MAApple> getModdedAppleConfigs(String configDir) {
        Path path = Paths.get(configDir + File.separator + MetalApple.MODID + File.separator + "custom" + File.separator);
        Map<String, MAApple> map = new HashMap<>();

        try (Stream<Path> files = Files.list(path)) {
            List<Path> listOfAppleFile = files.filter(file -> file.toString().endsWith("apple.json")).toList();
            for (Path file : listOfAppleFile) {
                String appleType = file.getFileName().toString().replace(".json", "");
                MAApple maApple = MAJsons.loadApple(configDir + File.separator + MetalApple.MODID + File.separator + "custom" + File.separator, appleType);
                map.put(appleType, maApple);
            }
        } catch (IOException e) {
            MetalApple.LOGGER.info("Error while loading modded apples: {}", e.getMessage());
        }

        return map;
    }

    public static FoodProperties createFoodProperties(MAApple maApple) {
        FoodProperties.Builder foodBuilder = new FoodProperties.Builder()
                .nutrition(maApple.nutrition())
                .saturationModifier(maApple.saturationMod());
        if (maApple.alwaysEat()) {
            foodBuilder.alwaysEdible();
        }

        return foodBuilder.build();
    }

    public static Consumable createConsumable(MAApple maApple) {
        Consumable.Builder consumableBuilder = Consumables.defaultFood();
        List<MobEffectInstance> mobEffectInstancesList = Arrays.stream(maApple.effects()).toList();
        consumableBuilder.onConsume(new ApplyStatusEffectsConsumeEffect(mobEffectInstancesList));
        return consumableBuilder.build();
    }
}