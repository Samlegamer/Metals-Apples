package fr.samlegamer.metalapples;

import fr.samlegamer.metalapples.config.MADirs;
import fr.samlegamer.metalapples.config.MAJsons;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;

public class MetalApple {
    public static final String MODID = "metalapples";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Item getItemByName(String name) {
        if(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(MODID, name)).isPresent())
        {
            return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(MODID, name)).get().value();
        }
        else
        {
            return Items.APPLE;
        }
    }

    public static void buildConfig(Path configDir) {
        MADirs maDirs = new MADirs(configDir);
        maDirs.addDirs();
        MAJsons maJsons = new MAJsons(configDir);
        maJsons.makeVanillaJsons();
    }
}
