package com.reclipse.drit.datagen;

import com.reclipse.drit.DritMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.reclipse.drit.DritBlocks.*;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DritMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add block tags here
        tag(BlockTags.MINEABLE_WITH_SHOVEL,
                Drit.get(),
                FramLand.get()
        );
    }

    private void tag(TagKey<Block> blockTagKey, Block... blocks) {
        tag(blockTagKey).add(blocks);
    }

    @SafeVarargs
    private void tag(TagKey<Block> blockTagKey, DeferredBlock<? extends Block>... blocks) {
        IntrinsicTagAppender<Block> tag = tag(blockTagKey);
        for (DeferredBlock<? extends Block> block : blocks) {
            tag.add(block.get());
        }
    }
}
