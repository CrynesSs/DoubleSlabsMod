package com.mods.CrynessDoubleSlabs.Core;

import com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs.DoubleSlabModelLoader;
import com.mods.CrynessDoubleSlabs.Inits.BlockInit;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CrynessDoubleSlabs.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            return;
        }
        event.addSprite(new ResourceLocation(CrynessDoubleSlabs.MOD_ID,"block/double_slab"));
    }
    @SubscribeEvent
    public static void clientStuff(FMLClientSetupEvent event){
        RenderTypeLookup.setRenderLayer(BlockInit.DOUBLE_SLAB.get(), RenderType.getCutoutMipped());
    }
    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        System.out.println("Registering Loader");
        ModelLoaderRegistry.registerLoader(new ResourceLocation(CrynessDoubleSlabs.MOD_ID, "fancyloader"), new DoubleSlabModelLoader());
    }
}
