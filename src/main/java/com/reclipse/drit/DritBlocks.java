package com.reclipse.drit;

import com.reclipse.drit.content.DritBlock;
import com.reclipse.drit.content.FramlandBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class DritBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DritMod.MODID);

	public static final DeferredBlock<DritBlock> Drit = registerBlockAndItem("drit", com.reclipse.drit.content.DritBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT));

	public static final DeferredBlock<FramlandBlock> FramLand = registerBlockAndItem("fram_land", FramlandBlock::new,
			BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND));

	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties) {
		return registerBlockAndItem(name, blockConstructor, properties, true, true);
	}

	// NOTE: This also attempts to generate the item model for the block, when running datagen
	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties, boolean addToTab, boolean genItemModel) {
		DeferredBlock<T> block = BLOCKS.registerBlock(name, blockConstructor, properties);
		DeferredItem<BlockItem> blockItem = DritItems.registerItem(name, props -> new BlockItem(block.get(), props), new Item.Properties(), addToTab);
		if (genItemModel) {
			DritItems.BLOCK_ITEMS.add(blockItem);
		}
		return block;
	}

	private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Function<BlockBehaviour.Properties, T> blockConstructor, BlockBehaviour.Properties properties, BiFunction<T, Item.Properties, BlockItem> blockItemConstructor) {
		DeferredBlock<T> block = BLOCKS.registerBlock(name, blockConstructor, properties);
		DeferredItem<BlockItem> blockItem = DritItems.registerItem(name, props -> blockItemConstructor.apply(block.get(), props), new Item.Properties());
		DritItems.BLOCK_ITEMS.add(blockItem);
		return block;
	}
}