package fr.samlegamer.metalapples.data;

import fr.samlegamer.metalapples.MetalApple;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static fr.samlegamer.metalapples.MetalApple.getItemByName;

public class MARecipes extends RecipeProvider
{
    public MARecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {

    }

    public static class Generator extends RecipeProvider.Runner {
        public Generator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(packOutput, completableFuture);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new RecipeProvider(provider, recipeOutput) {
                @Override
                public void buildRecipes() {
                    makeAppleRecipe(recipeOutput, "copper_apple", Blocks.WAXED_COPPER_BLOCK, Items.COPPER_INGOT);
                    makeAppleRecipe(recipeOutput, "iron_apple", Blocks.IRON_BLOCK, Items.IRON_INGOT);
                    makeAppleRecipe(recipeOutput, "lapis_apple", Blocks.LAPIS_BLOCK, Items.LAPIS_LAZULI);
                    makeAppleRecipe(recipeOutput, "redstone_apple", Blocks.REDSTONE_BLOCK, Items.REDSTONE);
                    makeAppleRecipe(recipeOutput, "diamond_apple", Blocks.DIAMOND_BLOCK, Items.DIAMOND);
                    shapeless(RecipeCategory.FOOD, getItemByName("netherite_apple"))
                            .requires(getItemByName("diamond_apple"))
                            .requires(Items.NETHERITE_INGOT)
                            .unlockedBy("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                            .save(recipeOutput);
                }

                private void makeAppleRecipe(RecipeOutput consumer, String name, Block gemBlock, Item gemItem) {
                    shaped(RecipeCategory.FOOD, getItemByName(name))
                            .pattern("DBD")
                            .pattern("BAB")
                            .pattern("DBD")
                            .define('A', Items.APPLE)
                            .define('B', gemBlock)
                            .define('D', gemItem)
                            .unlockedBy("has_item", has(Items.APPLE))
                            .save(consumer);
                }

            };
        }

        @Override
        public @NotNull String getName() {
            return MetalApple.MODID + " Recipes";
        }
    }
}