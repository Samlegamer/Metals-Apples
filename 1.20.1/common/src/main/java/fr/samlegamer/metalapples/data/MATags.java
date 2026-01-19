package fr.samlegamer.metalapples.data;

import fr.samlegamer.metalapples.MetalApple;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MATags
{
    public static final TagKey<Item> TAG_METAL_APPLES = TagKey.create(Registries.ITEM, new ResourceLocation(MetalApple.MODID, "special_apples"));

    public static Item[] getMetalAppleItems() {
        return new Item[] {
                MetalApple.getItemByName("copper_apple"),
                MetalApple.getItemByName("iron_apple"),
                MetalApple.getItemByName("lapis_apple"),
                MetalApple.getItemByName("redstone_apple"),
                MetalApple.getItemByName("diamond_apple"),
                MetalApple.getItemByName("netherite_apple")
        };
    }
}
