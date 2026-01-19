package fr.samlegamer.metalapples.item;

import fr.samlegamer.metalapples.MetalApple;
import fr.samlegamer.metalapples.config.MAJsons;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MAItemsRegistry {
    public static Map<String, Item> getVanillaApples(String configDir) {
        Map<String, Item> map = new HashMap<>();
        String[] appleTypes = new String[]{
                "copper_apple",
                "iron_apple",
                "lapis_apple",
                "redstone_apple",
                "diamond_apple",
                "netherite_apple"
        };

        for (String appleType : appleTypes) {
            MAApple maApple = MAJsons.loadApple(configDir + File.separator + MetalApple.MODID+ File.separator + File.separator + "vanilla" + File.separator, appleType);
            putAppleInMap(map, appleType, maApple);
        }

        return map;
    }

    public static Map<String, Item> getModdedApples(String configDir) {
        Path path = Paths.get(configDir + File.separator +MetalApple.MODID+ File.separator + "custom" + File.separator);
        Map<String, Item> map = new HashMap<>();

        try (Stream<Path> files = Files.list(path)) {
            List<Path> listOfAppleFile = files.filter(file -> file.getFileName().endsWith("apple.json")).toList();

            for (Path file : listOfAppleFile) {
                String appleType = file.toString().replace(".json", "");
                MAApple maApple = MAJsons.loadApple(configDir + File.separator + "custom" + File.separator, appleType);
                putAppleInMap(map, appleType, maApple);
            }

        } catch (IOException e) {
            MetalApple.LOGGER.info("Error while loading modded apples: {}", e.getMessage());
        }

        return map;
    }

    // Nouvelle méthode qui retourne les configurations MAApple au lieu des Items
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

    // Nouvelle méthode qui retourne les configurations MAApple au lieu des Items
    public static Map<String, MAApple> getModdedAppleConfigs(String configDir) {
        Path path = Paths.get(configDir + File.separator + MetalApple.MODID + File.separator + "custom" + File.separator);
        Map<String, MAApple> map = new HashMap<>();

        try (Stream<Path> files = Files.list(path)) {
            List<Path> listOfAppleFile = files.filter(file -> file.toString().endsWith("apple.json")).toList();
            for (Path file : listOfAppleFile) {
                String appleType = file.getFileName().toString().replace(".json", "");
                MAApple maApple = MAJsons.loadApple(configDir + File.separator + "custom" + File.separator, appleType);
                map.put(appleType, maApple);
            }
        } catch (IOException e) {
            MetalApple.LOGGER.info("Error while loading modded apples: {}", e.getMessage());
        }

        return map;
    }

    private static void putAppleInMap(Map<String, Item> map, String appleType, MAApple maApple) {
        FoodProperties foodProperties = createFoodProperties(maApple);
        map.put(appleType, new Item(new Item.Properties().food(foodProperties)));
    }

    public static FoodProperties createFoodProperties(MAApple maApple) {
        MetalApple.LOGGER.info("Creating food properties for: {}", maApple.apple());
        MetalApple.LOGGER.info("  Effects array length: {}", maApple.effects().length);

        FoodProperties.Builder foodBuilder = new FoodProperties.Builder()
                .nutrition(maApple.nutrition())
                .saturationMod(maApple.saturationMod());
        if (maApple.alwaysEat()) {
            foodBuilder.alwaysEat();
        }

        for (MobEffectInstance effect : maApple.effects()) {
            MetalApple.LOGGER.info("  Adding effect: {} (Duration: {}, Amplifier: {})",
                net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect()),
                effect.getDuration(),
                effect.getAmplifier());
            foodBuilder.effect(effect, 1.0f);
        }

        FoodProperties props = foodBuilder.build();
        MetalApple.LOGGER.info("  Final food properties created with {} effects", props.getEffects().size());
        return props;
    }
}