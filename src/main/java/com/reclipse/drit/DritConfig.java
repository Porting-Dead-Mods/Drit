package com.reclipse.drit;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DritMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DritConfig {
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.IntValue GROWTH_TICK_CHANCE = BUILDER
			.comment("Chance for a random tick to give the crop a growth tick")
			.defineInRange("growthTickChance", 4, 0, Integer.MAX_VALUE);

	static final ModConfigSpec SPEC = BUILDER.build();

	public static int growthTickChance;

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		growthTickChance = GROWTH_TICK_CHANCE.get();
	}
}
