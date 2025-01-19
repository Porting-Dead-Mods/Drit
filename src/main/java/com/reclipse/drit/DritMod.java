package com.reclipse.drit;

import com.mojang.logging.LogUtils;
import com.reclipse.drit.content.DritBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import static com.reclipse.drit.DritBlocks.Drit;
import static com.reclipse.drit.DritBlocks.FramLand;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DritMod.MODID)
public class DritMod {
	public static final String MODID = "drit";
	public static final Logger LOGGER = LogUtils.getLogger();

	public DritMod(IEventBus modEventBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, DritConfig.SPEC);
		DritItems.ITEMS.register(modEventBus);
		DritBlocks.BLOCKS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
	}

	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DRIT_TAB = CREATIVE_MODE_TABS.register("drit", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.drit"))
			.withTabsBefore(CreativeModeTabs.COMBAT)
			.icon(() -> Drit.get().asItem().getDefaultInstance())
			.displayItems((parameters, output) -> {
				output.accept(Drit.get());
				output.accept(FramLand.get());
			}).build());

	public static ResourceLocation rl(String name) {
		return ResourceLocation.fromNamespaceAndPath(MODID, name);
	}
}
