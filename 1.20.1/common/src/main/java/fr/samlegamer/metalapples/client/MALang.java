package fr.samlegamer.metalapples.client;

import net.minecraft.world.item.Item;
import java.util.HashMap;
import java.util.Map;
import static fr.samlegamer.metalapples.MetalApple.getItemByName;

public class MALang
{
    public static Map<Item, String> getLangEnUS()
    {
        Map<Item, String> lang = new HashMap<>();
        lang.put(getItemByName("copper_apple"), "Copper Apple");
        lang.put(getItemByName("iron_apple"), "Iron Apple");
        lang.put(getItemByName("lapis_apple"), "Lapis Lazuli Apple");
        lang.put(getItemByName("redstone_apple"), "Redstone Apple");
        lang.put(getItemByName("diamond_apple"), "Diamond Apple");
        lang.put(getItemByName("netherite_apple"), "Netherite Apple");
        return lang;
    }

    public static Map<Item, String> getLangFrFR()
    {
        Map<Item, String> lang = new HashMap<>();
        lang.put(getItemByName("copper_apple"), "Pomme en cuivre");
        lang.put(getItemByName("iron_apple"), "Pomme en fer");
        lang.put(getItemByName("lapis_apple"), "Pomme en lapis lazuli");
        lang.put(getItemByName("redstone_apple"), "Pomme en redstone");
        lang.put(getItemByName("diamond_apple"), "Pomme en diamant");
        lang.put(getItemByName("netherite_apple"), "Pomme en netherite");
        return lang;
    }
}
