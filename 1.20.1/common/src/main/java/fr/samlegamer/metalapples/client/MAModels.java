package fr.samlegamer.metalapples.client;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import static fr.samlegamer.metalapples.MetalApple.getItemByName;

public class MAModels
{
    public static List<Item> getModels()
    {
        List<Item> models = new ArrayList<>();
        models.add(getItemByName("copper_apple"));
        models.add(getItemByName("iron_apple"));
        models.add(getItemByName("lapis_apple"));
        models.add(getItemByName("redstone_apple"));
        models.add(getItemByName("diamond_apple"));
        models.add(getItemByName("netherite_apple"));
        models.add(getItemByName("emerald_apple"));
        return models;
    }
}
