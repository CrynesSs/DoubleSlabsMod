package com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DoubleSlabBlock extends SlabBlock {
    public DoubleSlabBlock() {
        super(AbstractBlock.Properties.create(Material.CLAY).hardnessAndResistance(0.5F).notSolid());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DoubleSlabTE();
    }

    @Override
    public float getPlayerRelativeBlockHardness(@Nonnull BlockState state, @Nonnull PlayerEntity player, IBlockReader worldIn, @Nonnull BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof DoubleSlabTE)) {
            return -1;
        }
        switch (state.get(TYPE)) {
            case BOTTOM: {
                return ((DoubleSlabTE) tile).getTextures().getKey().getPlayerRelativeBlockHardness(player, worldIn, pos);
            }
            case TOP: {
                return ((DoubleSlabTE) tile).getTextures().getValue().getPlayerRelativeBlockHardness(player, worldIn, pos);
            }
            case DOUBLE: {
                if (player.isSneaking()) {
                    BlockRayTraceResult result = worldIn.rayTraceBlocks(
                            player.getPositionVec().add(new Vector3d(0, player.getHeight(), 0)),
                            player.getPositionVec().add(new Vector3d(0, player.getEyeHeight(), 0)).add(player.getLookVec().mul(5, 5, 5)), pos, worldIn.getBlockState(pos).getShape(worldIn, pos), worldIn.getBlockState(pos));
                    if (result.getHitVec().getY() - pos.getY() <= .5f) {
                        return ((DoubleSlabTE) tile).getTextures().getKey().getPlayerRelativeBlockHardness(player, worldIn, pos);
                    } else {
                        return ((DoubleSlabTE) tile).getTextures().getValue().getPlayerRelativeBlockHardness(player, worldIn, pos);
                    }
                } else {
                    return (((DoubleSlabTE) tile).getTextures().getKey().getPlayerRelativeBlockHardness(player, worldIn, pos) + ((DoubleSlabTE) tile).getTextures().getValue().getPlayerRelativeBlockHardness(player, worldIn, pos)) / 2;
                }
            }
            default: {
                return -1;
            }
        }
    }
}
