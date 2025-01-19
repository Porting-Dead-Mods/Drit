package com.reclipse.drit;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DritMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DritConfig {
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.IntValue GROWTH_TICK_CHANCE = BUILDER
			.comment(" Chance for a redstone pulse to give the crop a random tick/age increase (1 in X chance)")
			.defineInRange("growthTickChance", 4, 0, Integer.MAX_VALUE);

	private static final ModConfigSpec.BooleanValue RANDOM_OR_AGE = BUILDER
			.comment(" If the crop should grow by random tick or by age (false - random tick, true - age)")
			.define("randomOrAge", true);

	private static final ModConfigSpec.IntValue TICKING_QUANTITY = BUILDER
			.comment(" How many times to tick/grow the crop")
			.defineInRange("tickingQuantity", 1, 0, Integer.MAX_VALUE);

	static final ModConfigSpec SPEC = BUILDER.build();

	public static int growthTickChance;
	public static boolean randomOrAge;
	public static int tickingQuantity;

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		growthTickChance = GROWTH_TICK_CHANCE.get();
		randomOrAge = RANDOM_OR_AGE.get();
		tickingQuantity = TICKING_QUANTITY.get();
	}
}
