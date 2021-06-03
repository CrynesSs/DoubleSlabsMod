package com.mods.CrynessDoubleSlabs.Core;

import com.mods.CrynessDoubleSlabs.Inits.BlockInit;
import com.mods.CrynessDoubleSlabs.Inits.TileEntityTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("cryness_double_slabs")
public class CrynessDoubleSlabs {
    // Directly reference a log4j logger.
    public static final String MOD_ID = "cryness_double_slabs";

    public CrynessDoubleSlabs() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the doClientStuff method for modloading
        BlockInit.BLOCKS.register(modEventBus);
        TileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        // Register ourselves for server and other game events we are interested
        System.out.println("NOT MAD YET");
        MinecraftForge.EVENT_BUS.register(this);
    }
}
