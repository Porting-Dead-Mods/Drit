package com.reclipse.drit.datagen;

import com.reclipse.drit.DritMod;
import static com.reclipse.drit.DritBlocks.*;

import com.reclipse.drit.content.FramlandBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;

import java.util.ArrayList;

public class BlockModelProvider extends BlockStateProvider {
    private final ExistingFileHelper existing;
    public BlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DritMod.MODID, existingFileHelper);
        this.existing = existingFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Drit.get(), new ModelFile.ExistingModelFile(defaultNamespaceBlockTexture(Blocks.DIRT), existing));
        getVariantBuilder(FramLand.get()).forAllStatesExcept(blockState -> {
            if (blockState.getValue(FramlandBlock.MOISTURE) == FramlandBlock.MAX_MOISTURE) {
                return new ConfiguredModel[] {new ConfiguredModel(new ModelFile.ExistingModelFile(defaultNamespaceBlockTexture(Blocks.FARMLAND, "_moist"), existing))};
            } else {
                return new ConfiguredModel[] {new ConfiguredModel(new ModelFile.ExistingModelFile(defaultNamespaceBlockTexture(Blocks.FARMLAND), existing))};
            }
        }, FramlandBlock.POWERED, FramlandBlock.WAS_POWERED);
    }

    public ResourceLocation defaultNamespaceBlockTexture(Block vanilla, String suffix) {
        ResourceLocation name = key(vanilla);
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + suffix);
    }

    public ResourceLocation defaultNamespaceModelFile(Block vanilla, String suffix) {
        ResourceLocation name = key(vanilla);
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + suffix);
    }

    public ResourceLocation defaultNamespaceBlockTexture(Block vanilla) {
        ResourceLocation name = key(vanilla);
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }

    public ResourceLocation defaultNamespaceModelFile(Block vanilla) {
        ResourceLocation name = key(vanilla);
        return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
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