package fr.samlegamer.metalapples;

import fr.samlegamer.metalapples.client.MALang;
import fr.samlegamer.metalapples.client.MAModels;
import fr.samlegamer.metalapples.data.MARecipes;
import fr.samlegamer.metalapples.data.MATags;
import fr.samlegamer.metalapples.item.MAApple;
import fr.samlegamer.metalapples.item.MAItemsRegistry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mod(MetalApple.MODID)
public class MetalAppleForge {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MetalApple.MODID);

    public MetalAppleForge(FMLJavaModLoadingContext context) {
        MetalApple.LOGGER.info("Loading Metal Apples Forge mod");
        MetalApple.buildConfig(FMLPaths.CONFIGDIR.get());
        BusGroup bus = context.getModBusGroup();
        ITEMS.register(bus);
        registerItems();
        GatherDataEvent.getBus(bus).addListener(this::gatherData);
        BuildCreativeModeTabContentsEvent.getBus(bus).addListener(this::addToTab);
        MetalApple.LOGGER.info("Finish loading Metal Apples Forge mod");
    }

    public void registerItems() {
        String configDir = FMLPaths.CONFIGDIR.get().toString();

        Map<String, MAApple> mapVanilla = MAItemsRegistry.getVanillaAppleConfigs(configDir);
        Map<String, MAApple> mapModded = MAItemsRegistry.getModdedAppleConfigs(configDir);

        for(Map.Entry<String, MAApple> entry : mapVanilla.entrySet()) {
            MAApple appleConfig = entry.getValue();
            String name = entry.getKey();
            ITEMS.register(name, () -> new Item(new Item.Properties().setId(ITEMS.key(name)).food(MAItemsRegistry.createFoodProperties(appleConfig), MAItemsRegistry.createConsumable(appleConfig))));
        }

        for(Map.Entry<String, MAApple> entry : mapModded.entrySet()) {
            MAApple appleConfig = entry.getValue();
            String name = entry.getKey();
            ITEMS.register(name, () -> new Item(new Item.Properties().setId(ITEMS.key(name)).food(MAItemsRegistry.createFoodProperties(appleConfig), MAItemsRegistry.createConsumable(appleConfig))));
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
            generator.addProvider(true, new VanillaItemTagsProvider(output, lookupProvider, MetalApple.MODID, existingFileHelper) {
                @Override
                public void addTags(HolderLookup.Provider provider) {
                    tag(MATags.TAG_METAL_APPLES).add(MATags.getMetalAppleItems());
                }
            });
            generator.addProvider(true, new MARecipes.Generator(output, lookupProvider));
        }

        if (event.includeClient()) {
//            generator.addProvider(true, new ModelProvider(output) {
//
//                @Override
//                protected ItemModelGenerators getItemModelGenerators(ItemInfoCollector items, SimpleModelCollector models) {
//                    return new ItemModelGenerators(items, models) {
//                        @Override
//                        public void run() {
//                            for(Item item : MAModels.getModels()) {
//                                this.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
//                            }
//                        }
//                    };
//                }
//            });

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