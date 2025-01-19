package com.reclipse.drit.datagen;

import com.reclipse.drit.DritMod;
import static com.reclipse.drit.DritBlocks.*;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends BlockStateProvider {
    public BlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DritMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Drit.get());
        simpleBlock(FramLand.get());
    }

    public ResourceLocation blockTexture(Block block, String suffix) {
        ResourceLocation name = key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + suffix);
    }

    public ResourceLocation existingModelFile(Block block) {
        ResourceLocation name = key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }

    public ResourceLocation existingModelFile(String name) {
        return modLoc(ModelProvider.BLOCK_FOLDER + "/" + name);
    }

    public ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public String name(Block block) {
        return key(block).getPath();
    }

    public ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }
}