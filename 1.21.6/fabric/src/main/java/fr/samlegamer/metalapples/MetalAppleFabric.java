package fr.samlegamer.metalapples;

import fr.samlegamer.metalapples.item.MAApple;
import fr.samlegamer.metalapples.item.MAItemsRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import java.util.Map;

public class MetalAppleFabric implements ModInitializer
{
    @Override
    public void onInitialize() {
        MetalApple.LOGGER.info("Loading Metal Apples Fabric mod");
        MetalApple.buildConfig(FabricLoader.getInstance().getConfigDir());
        registerItems();
        addToTab();
        MetalApple.LOGGER.info("Finish loading Metal Apples Fabric mod");
    }

    public void registerItems() {
        String configDir = FabricLoader.getInstance().getConfigDir().toString();

        Map<String, MAApple> mapVanilla = MAItemsRegistry.getVanillaAppleConfigs(configDir);
        Map<String, MAApple> mapModded = MAItemsRegistry.getModdedAppleConfigs(configDir);

        for(Map.Entry<String, MAApple> entry : mapVanilla.entrySet()) {
            MAApple appleConfig = entry.getValue();
            ResourceLocation key = ResourceLocation.fromNamespaceAndPath(MetalApple.MODID, entry.getKey());
            Registry.register(BuiltInRegistries.ITEM, key, new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, key)).food(MAItemsRegistry.createFoodProperties(appleConfig), MAItemsRegistry.createConsumable(appleConfig))));
        }

        for(Map.Entry<String, MAApple> entry : mapModded.entrySet()) {
            MAApple appleConfig = entry.getValue();
            ResourceLocation key = ResourceLocation.fromNamespaceAndPath(MetalApple.MODID, entry.getKey());
            Registry.register(BuiltInRegistries.ITEM, key, new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, key)).food(MAItemsRegistry.createFoodProperties(appleConfig), MAItemsRegistry.createConsumable(appleConfig))));
        }
    }

    private void addToTab()
    {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(content -> {
            for(Item item : BuiltInRegistries.ITEM.stream().toList().stream().filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(MetalApple.MODID)).toList()) {
                content.accept(item);
            }
        });
    }
}