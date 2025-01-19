package com.reclipse.drit.datagen;

import com.reclipse.drit.DritBlocks;
import com.reclipse.drit.DritMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecipesProvider extends RecipeProvider {
    public RecipesProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, DritBlocks.Drit.asItem(), 1)
                .pattern("D")
                .pattern("D")
                .define('D', Items.DIRT)
                .unlockedBy("has_item", has(Items.DIRT))
                .save(pRecipeOutput, DritMod.rl("drit"));
    }
}
