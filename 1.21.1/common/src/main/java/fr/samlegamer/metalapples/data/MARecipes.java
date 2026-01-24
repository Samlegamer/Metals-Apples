package fr.samlegamer.metalapples.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import static fr.samlegamer.metalapples.MetalApple.getItemByName;

public class MARecipes extends RecipeProvider
{
    public MARecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        makeAppleRecipe(consumer, "copper_apple", Blocks.WAXED_COPPER_BLOCK, Items.COPPER_INGOT);
        makeAppleRecipe(consumer, "iron_apple", Blocks.IRON_BLOCK, Items.IRON_INGOT);
        makeAppleRecipe(consumer, "lapis_apple", Blocks.LAPIS_BLOCK, Items.LAPIS_LAZULI);
        makeAppleRecipe(consumer, "redstone_apple", Blocks.REDSTONE_BLOCK, Items.REDSTONE);
        makeAppleRecipe(consumer, "diamond_apple", Blocks.DIAMOND_BLOCK, Items.DIAMOND);
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(), Ingredient.of(getItemByName("diamond_apple")),
                Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.FOOD, getItemByName("netherite_apple"))
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(consumer, BuiltInRegistries.ITEM.getKey(getItemByName("netherite_apple")));
    }

    private void makeAppleRecipe(RecipeOutput consumer, String name, Block gemBlock, Item gemItem) {
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, getItemByName(name))
                .pattern("DBD")
                .pattern("BAB")
                .pattern("DBD")
                .define('A', Items.APPLE)
                .define('B', gemBlock)
                .define('D', gemItem)
                .unlockedBy("has_item", has(Items.APPLE))
                .save(consumer);
    }
}