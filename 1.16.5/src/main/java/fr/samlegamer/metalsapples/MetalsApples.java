package fr.samlegamer.metalsapples;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MetalsApples.MODID)
public class MetalsApples
{
	public static final String MODID = "metalsapples";
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
  //Foods
  	public static final RegistryObject<Item> DIAMOND_APPLE = ITEMS_REGISTRY.register("diamond_apple", () -> new Item(MetalsApples.DIAMOND));
   	public static final RegistryObject<Item> IRON_APPLE = ITEMS_REGISTRY.register("iron_apple", () -> new Item(MetalsApples.IRON));
   	public static final RegistryObject<Item> LAPIS_APPLE = ITEMS_REGISTRY.register("lapis_apple", () -> new Item(MetalsApples.LAPIS));
   	public static final RegistryObject<Item> REDSTONE_APPLE = ITEMS_REGISTRY.register("redstone_apple", () -> new Item(MetalsApples.REDSTONE));
  	
    public MetalsApples()
    {
    	LOGGER.info("Metals Apples Loading...");
    	ITEMS_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    	LOGGER.info("Metals Apples Is Charged !");
    }

    
    public static final Item.Properties DIAMOND = new Item.Properties().tab(ItemGroup.TAB_FOOD)
			.food(new Food.Builder().nutrition(10).saturationMod(15.0F)
			.effect(() -> new EffectInstance(Effects.MOVEMENT_SPEED, 100 * 30, 4), 1.0f)
            .effect(() -> new EffectInstance(Effects.ABSORPTION, 125 * 30, 5), 1.0f)
            .effect(() -> new EffectInstance(Effects.DAMAGE_RESISTANCE, 125 * 30, 1), 1.0f)
            .effect(() -> new EffectInstance(Effects.REGENERATION, 50 * 30, 4), 1.0f)
            .effect(() -> new EffectInstance(Effects.DIG_SPEED, 100 * 30, 2), 1.0f).build());
	
	public static final Item.Properties IRON = new Item.Properties().tab(ItemGroup.TAB_FOOD)
            .food(new Food.Builder().nutrition(6).saturationMod(4.0F).alwaysEat()
            .effect(() -> new EffectInstance(Effects.DAMAGE_RESISTANCE, 75 * 30, 0), 1.0f).build());
	
	public static final Item.Properties LAPIS = new Item.Properties().tab(ItemGroup.TAB_FOOD)
            .food(new Food.Builder().nutrition(6).saturationMod(4.0F).alwaysEat()
            .effect(() -> new EffectInstance(Effects.NIGHT_VISION, 150 * 30, 0), 1.0f).build());

	public static final Item.Properties REDSTONE = new Item.Properties().tab(ItemGroup.TAB_FOOD)
            .food(new Food.Builder().nutrition(6).saturationMod(4.0F).alwaysEat()
            .effect(() -> new EffectInstance(Effects.MOVEMENT_SPEED, 150 * 30, 0), 1.0f).build());
}
