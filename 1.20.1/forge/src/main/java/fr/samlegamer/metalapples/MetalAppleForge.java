package fr.samlegamer.metalapples;

import fr.samlegamer.metalapples.client.MALang;
import fr.samlegamer.metalapples.client.MAModels;
import fr.samlegamer.metalapples.config.MADirs;
import fr.samlegamer.metalapples.config.MAJsons;
import fr.samlegamer.metalapples.data.MARecipes;
import fr.samlegamer.metalapples.data.MATags;
import fr.samlegamer.metalapples.item.MAApple;
import fr.samlegamer.metalapples.item.MAItemsRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mod(MetalApple.MODID)
public class MetalAppleForge {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MetalApple.MODID);

    public MetalAppleForge() {
        MetalApple.LOGGER.info("Loading Metal Apples Forge mod");
        MetalApple.buildConfig(FMLPaths.CONFIGDIR.get());
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
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
            ITEMS.register(entry.getKey(), () -> new Item(new Item.Properties().food(MAItemsRegistry.createFoodProperties(appleConfig))));
        }

        for(Map.Entry<String, MAApple> entry : mapModded.entrySet()) {
            MAApple appleConfig = entry.getValue();
            ITEMS.register(entry.getKey(), () -> new Item(new Item.Properties().food(MAItemsRegistry.createFoodProperties(appleConfig))));
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

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new BlockTagsProvider(output, lookupProvider, MetalApple.MODID, existingFileHelper) {
                @Override
                protected void addTags(HolderLookup.@NonNull Provider provider) {

                }
            };
            generator.addProvider(true, blockTagsProvider);
            generator.addProvider(true, new ItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), MetalApple.MODID, existingFileHelper) {
                @Override
                public void addTags(HolderLookup.@NonNull Provider provider) {
                    tag(MATags.TAG_METAL_APPLES).add(MATags.getMetalAppleItems());
                }
            });
            //generator.addProvider(true, new MARecipes(output));
        }

        if (event.includeClient()) {
            generator.addProvider(true, new ItemModelProvider(output, MetalApple.MODID, existingFileHelper) {
                @Override
                protected void registerModels() {
                    for(Item item : MAModels.getModels())
                    {
                        basicItem(item);
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
}