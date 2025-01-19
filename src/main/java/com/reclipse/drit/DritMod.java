package com.reclipse.drit;

import com.mojang.logging.LogUtils;
import com.reclipse.drit.content.DritBlock;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DritMod.MODID)
public class DritMod {
	public static final String MODID = "drit";
	private static final Logger LOGGER = LogUtils.getLogger();

	public DritMod(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, DritConfig.SPEC);
		DritItems.ITEMS.register(modEventBus);
		DritBlocks.BLOCKS.register(modEventBus);
	}

	public static ResourceLocation rl(String name) {
		return ResourceLocation.fromNamespaceAndPath(MODID, name);
	}
}
