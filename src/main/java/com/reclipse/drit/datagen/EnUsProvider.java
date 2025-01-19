package com.reclipse.drit.datagen;

import com.reclipse.drit.DritMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.reclipse.drit.DritItems.*;
import static com.reclipse.drit.DritBlocks.*;

public final class EnUsProvider extends LanguageProvider {
    public EnUsProvider(PackOutput output) {
        super(output, DritMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(Drit, "Drit");
        addBlock(FramLand, "Fram Land");
    }

    private void addItem(String key, String val) {
        add("item.DritMod." + key, val);
    }

    private void addBlock(String key, String val) {
        add("block.DritMod." + key, val);
    }
}
