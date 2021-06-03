package com.mods.CrynessDoubleSlabs.Inits;

import com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs.DoubleSlabTE;
import com.mods.CrynessDoubleSlabs.Core.CrynessDoubleSlabs;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CrynessDoubleSlabs.MOD_ID);
    public static final RegistryObject<TileEntityType<DoubleSlabTE>> SLAB_TE = TILE_ENTITY_TYPES.register("slab_te",()->TileEntityType.Builder.create(DoubleSlabTE::new, BlockInit.DOUBLE_SLAB.get()).build(null));


}
