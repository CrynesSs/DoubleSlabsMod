package com.mods.CrynessDoubleSlabs.Core;

import com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs.DoubleSlabBlock;
import com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs.DoubleSlabTE;
import com.mods.CrynessDoubleSlabs.Inits.BlockInit;
import javafx.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrynessDoubleSlabs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onBlockBreak(final BlockEvent.BreakEvent event) {
        if (event.getState().matchesBlock(BlockInit.DOUBLE_SLAB.get())) {
            BlockPos eventPos = event.getPos();
            PlayerEntity p = event.getPlayer();
            DoubleSlabTE te = (DoubleSlabTE) event.getWorld().getTileEntity(eventPos);
            if (te == null) {
                return;
            }
            if (event.getPlayer().isSneaking() && event.getState().get(DoubleSlabBlock.TYPE) == SlabType.DOUBLE) {
                BlockRayTraceResult result = event.getWorld().rayTraceBlocks(
                        p.getPositionVec().add(new Vector3d(0, p.getHeight(), 0)),
                        p.getPositionVec().add(new Vector3d(0, p.getEyeHeight(), 0)).add(p.getLookVec().mul(5, 5, 5)), eventPos, VoxelShapes.fullCube(), event.getState());

                if (result != null) {
                    double hitHeight = result.getHitVec().getY() - eventPos.getY();
                    if (hitHeight >= .5f) {
                        if (!p.isCreative() && ForgeHooks.canHarvestBlock(te.getTextures().getValue(), p, event.getWorld(), eventPos)) {
                            InventoryHelper.spawnItemStack((World) event.getWorld(), eventPos.getX(), eventPos.getY(), eventPos.getZ(), new ItemStack(te.getTextures().getValue().getBlock()));
                        }
                        te.updateTextures(new Pair<>(te.getTextures().getKey(), null));
                        event.getWorld().setBlockState(eventPos, event.getState().with(DoubleSlabBlock.TYPE, SlabType.BOTTOM), 3);
                    } else {
                        if (!p.isCreative() && ForgeHooks.canHarvestBlock(te.getTextures().getKey(), p, event.getWorld(), eventPos)) {
                            InventoryHelper.spawnItemStack((World) event.getWorld(), eventPos.getX(), eventPos.getY(), eventPos.getZ(), new ItemStack(te.getTextures().getKey().getBlock()));
                        }
                        te.updateTextures(new Pair<>(null, te.getTextures().getValue()));
                        event.getWorld().setBlockState(eventPos, event.getState().with(DoubleSlabBlock.TYPE, SlabType.TOP), 3);
                    }
                    event.setCanceled(true);
                    return;
                }
                return;
            }
            if (!event.getPlayer().isCreative()) {
                if (te.getTextures().getKey() != null && ForgeHooks.canHarvestBlock(te.getTextures().getKey(), event.getPlayer(), event.getWorld(), event.getPos())) {
                    InventoryHelper.spawnItemStack((World) event.getWorld(), eventPos.getX(), eventPos.getY(), eventPos.getZ(), new ItemStack(te.getTextures().getKey().getBlock()));
                }
                if (te.getTextures().getValue() != null && ForgeHooks.canHarvestBlock(te.getTextures().getValue(), event.getPlayer(), event.getWorld(), event.getPos())) {
                    InventoryHelper.spawnItemStack((World) event.getWorld(), eventPos.getX(), eventPos.getY(), eventPos.getZ(), new ItemStack(te.getTextures().getValue().getBlock()));
                }
            }
        }
    }
}
