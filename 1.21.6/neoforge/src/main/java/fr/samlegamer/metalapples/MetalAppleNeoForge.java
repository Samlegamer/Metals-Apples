package fr.samlegamer.metalapples;

import fr.samlegamer.metalapples.client.MALang;
import fr.samlegamer.metalapples.client.MAModels;
import fr.samlegamer.metalapples.data.MARecipes;
import fr.samlegamer.metalapples.data.MATags;
import fr.samlegamer.metalapples.item.MAApple;
import fr.samlegamer.metalapples.item.MAItemsRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mod(MetalApple.MODID)
public class MetalAppleNeoForge {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MetalApple.MODID);

    public MetalAppleNeoForge(IEventBus bus) {
        MetalApple.LOGGER.info("Loading Metal Apples Forge mod");
        MetalApple.buildConfig(FMLPaths.CONFIGDIR.get());
        ITEMS.register(bus);
        registerItems();
        bus.addListener(this::gatherData);
        bus.addListener(this::addToTab);
        MetalApple.LOGGER.info("Finish loading Metal Apples Forge mod");
    }

    public void registerItems() {
        String configDir = FMLPaths.CONFIGDIR.get().toString();

        Map<String, MAApple> mapVanilla = MAItemsRegistry.getVanillaAppleConfigs(configDir);
        Map<String, MAApple> mapModded = MAItemsRegistry.getModdedAppleConfigs(configDir);

        for(Map.Entry<String, MAApple> entry : mapVanilla.entrySet()) {
            MAApple appleConfig = entry.getValue();
            ITEMS.register(entry.getKey(), rl -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, rl)).food(MAItemsRegistry.createFoodProperties(appleConfig), MAItemsRegistry.createConsumable(appleConfig))));
        }

        for(Map.Entry<String, MAApple> entry : mapModded.entrySet()) {
            MAApple appleConfig = entry.getValue();
            ITEMS.register(entry.getKey(), rl -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, rl)).food(MAItemsRegistry.createFoodProperties(appleConfig), MAItemsRegistry.createConsumable(appleConfig))));
        }
    }

    private void addToTab(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            for(Item item : BuiltInRegistries.ITEM.stream().toList().stream().filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(MetalApple.MODID)).toList()) {
                event.accept(item);
            }
        }
    }

    private void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new MARecipes.Generator(output, lookupProvider));
        generator.addProvider(true, new ItemTagsProvider(output, lookupProvider, MetalApple.MODID) {
            @Override
            public void addTags(HolderLookup.@NonNull Provider provider) {
                tag(MATags.TAG_METAL_APPLES).add(MATags.getMetalAppleItems());
            }
        });
        generator.addProvider(true, new ModelProvider(output, MetalApple.MODID) {
            @Override
            protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
                for(Item item : MAModels.getModels()) {
                    itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
                }
            }
        });
        generator.addProvider(true, new LanguageProvider(output, MetalApple.MODID, "en_us") {
            @Override
            protected void addTranslations() {
                for(Map.Entry<Item, String> entry : MALang.getLangEnUS().entrySet()) {
                    this.add(entry.getKey(), entry.getValue());
                }
            }
        });
        generator.addProvider(true, new LanguageProvider(output, MetalApple.MODID, "fr_fr") {
            @Override
            protected void addTranslations() {
                for(Map.Entry<Item, String> entry : MALang.getLangFrFR().entrySet()) {
                    this.add(entry.getKey(), entry.getValue());
                }
            }
        });
    }
}