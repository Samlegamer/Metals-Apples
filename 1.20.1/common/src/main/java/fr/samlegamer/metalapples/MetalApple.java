package fr.samlegamer.metalapples;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MetalApple {
    public static final String MODID = "metalapples";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Item getItemByName(String name) {
        return BuiltInRegistries.ITEM.get(new ResourceLocation(MODID, name));
    }
}
