package com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

import javax.annotation.Nonnull;

public class DoubleSlabModelLoader implements IModelLoader<DoubleSlabGeometry> {
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {

    }

    @Override
    @Nonnull
    public DoubleSlabGeometry read(@Nonnull JsonDeserializationContext deserializationContext,@Nonnull JsonObject modelContents) {
        return new DoubleSlabGeometry();
    }
}
