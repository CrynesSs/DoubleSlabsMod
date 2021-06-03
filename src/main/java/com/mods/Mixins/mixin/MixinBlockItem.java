package com.mods.Mixins.mixin;

import com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs.DoubleSlabTE;
import com.mods.CrynessDoubleSlabs.Inits.BlockInit;
import javafx.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(BlockItem.class)
public class MixinBlockItem {
    @Inject(at = @At("HEAD"), method = "tryPlace(Lnet/minecraft/item/BlockItemUseContext;)Lnet/minecraft/util/ActionResultType;", cancellable = true)
    public void tryPlace(BlockItemUseContext context, CallbackInfoReturnable<ActionResultType> cir) {
        if (!context.canPlace()) {
            cir.setReturnValue(ActionResultType.FAIL);
        } else {
            BlockItemUseContext blockitemusecontext = this.getBlockItemUseContext(context);
            if (blockitemusecontext == null) {
                cir.setReturnValue(ActionResultType.FAIL);
            } else {
                BlockState currentState = context.getWorld().getBlockState(context.getPos());
                if (currentState.matchesBlock(BlockInit.DOUBLE_SLAB.get())) {
                    TileEntity te = context.getWorld().getTileEntity(context.getPos());
                    if (te instanceof DoubleSlabTE) {
                        if (currentState.get(SlabBlock.TYPE) == SlabType.BOTTOM) {
                            ((DoubleSlabTE) te).updateTextures(new Pair<>(((DoubleSlabTE) te).getTextures().getKey(), ((BlockItem) blockitemusecontext.getItem().getItem()).getBlock().getDefaultState().with(SlabBlock.TYPE, SlabType.TOP)));
                        } else {
                            ((DoubleSlabTE) te).updateTextures(new Pair<>(((BlockItem) blockitemusecontext.getItem().getItem()).getBlock().getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM), ((DoubleSlabTE) te).getTextures().getValue()));
                        }
                        context.getWorld().setBlockState(context.getPos(), currentState.with(SlabBlock.TYPE, SlabType.DOUBLE), 11);
                        cir.setReturnValue(func_233537_a_(context.getWorld().isRemote));

                        World world = context.getWorld();
                        BlockPos blockpos = context.getPos();
                        PlayerEntity playerentity = context.getPlayer();
                        ItemStack itemstack = blockitemusecontext.getItem();
                        SoundType soundtype = currentState.with(SlabBlock.TYPE, SlabType.DOUBLE).getSoundType(world, blockpos, context.getPlayer());
                        world.playSound(playerentity, blockpos, this.getPlaceSound(currentState.with(SlabBlock.TYPE, SlabType.DOUBLE), world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        if (playerentity == null || !playerentity.abilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                        return;
                    }
                }
                BlockState blockstate = this.getStateForPlacement(blockitemusecontext);
                if (blockstate == null) {
                    cir.setReturnValue(ActionResultType.FAIL);
                } else if (blockstate.getBlock() instanceof SlabBlock) {
                    SlabType type = blockstate.get(SlabBlock.TYPE);
                    switch (type) {
                        case BOTTOM: {
                            context.getWorld().setBlockState(context.getPos(), BlockInit.DOUBLE_SLAB.get().getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, blockstate.get(SlabBlock.WATERLOGGED)), 3);
                            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
                            if (tileEntity instanceof DoubleSlabTE) {
                                ((DoubleSlabTE) tileEntity).textures = new Pair<>(blockstate, null);
                            }
                            cir.setReturnValue(func_233537_a_(context.getWorld().isRemote));
                            World world = context.getWorld();
                            BlockPos blockpos = context.getPos();
                            PlayerEntity playerentity = context.getPlayer();
                            ItemStack itemstack = blockitemusecontext.getItem();
                            SoundType soundtype = BlockInit.DOUBLE_SLAB.get().getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE).getSoundType(world, blockpos, context.getPlayer());
                            world.playSound(playerentity, blockpos, this.getPlaceSound( BlockInit.DOUBLE_SLAB.get().getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE), world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                            if (playerentity == null || !playerentity.abilities.isCreativeMode) {
                                itemstack.shrink(1);
                            }
                            return;
                        }
                        case TOP: {
                            context.getWorld().setBlockState(context.getPos(), BlockInit.DOUBLE_SLAB.get().getDefaultState().with(SlabBlock.TYPE, SlabType.TOP).with(SlabBlock.WATERLOGGED, blockstate.get(SlabBlock.WATERLOGGED)), 3);
                            TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
                            if (tileEntity instanceof DoubleSlabTE) {
                                ((DoubleSlabTE) tileEntity).updateTextures(new Pair<>(null, blockstate));
                            }
                            cir.setReturnValue(func_233537_a_(context.getWorld().isRemote));
                            World world = context.getWorld();
                            BlockPos blockpos = context.getPos();
                            PlayerEntity playerentity = context.getPlayer();
                            ItemStack itemstack = blockitemusecontext.getItem();
                            SoundType soundtype =  BlockInit.DOUBLE_SLAB.get().getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE).getSoundType(world, blockpos, context.getPlayer());
                            world.playSound(playerentity, blockpos, this.getPlaceSound( BlockInit.DOUBLE_SLAB.get().getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE), world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                            if (playerentity == null || !playerentity.abilities.isCreativeMode) {
                                itemstack.shrink(1);
                            }
                        }
                    }
                } else if (!this.placeBlock(blockitemusecontext, blockstate)) {
                    cir.setReturnValue(ActionResultType.FAIL);
                } else {
                    BlockPos blockpos = blockitemusecontext.getPos();
                    World world = blockitemusecontext.getWorld();
                    PlayerEntity playerentity = blockitemusecontext.getPlayer();
                    ItemStack itemstack = blockitemusecontext.getItem();
                    BlockState blockstate1 = world.getBlockState(blockpos);
                    Block block = blockstate1.getBlock();
                    if (block == blockstate.getBlock()) {
                        blockstate1 = this.func_219985_a(blockpos, world, itemstack, blockstate1);
                        this.onBlockPlaced(blockpos, world, playerentity, itemstack, blockstate1);
                        block.onBlockPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
                        if (playerentity instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerentity, blockpos, itemstack);
                        }
                    }
                    SoundType soundtype = blockstate1.getSoundType(world, blockpos, context.getPlayer());
                    world.playSound(playerentity, blockpos, this.getPlaceSound(blockstate1, world, blockpos, context.getPlayer()), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (playerentity == null || !playerentity.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    cir.setReturnValue(func_233537_a_(world.isRemote));
                }
            }
        }
    }

    private ActionResultType func_233537_a_(boolean isRemote) {
        return isRemote ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
    }

    @Shadow
    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        throw new IllegalStateException("Mixin failed to shadow getBlockItemUseContext()");
    }

    @Shadow
    private BlockState func_219985_a(BlockPos blockpos, World world, ItemStack itemstack, BlockState blockstate1) {
        throw new IllegalStateException("Mixin failed to shadow getBlockItemUseContext()");
    }

    @Shadow
    protected SoundEvent getPlaceSound(BlockState blockstate1, World world, BlockPos blockpos, PlayerEntity player) {
        throw new IllegalStateException("Mixin failed to shadow getBlockItemUseContext()");
    }

    @Shadow
    protected boolean placeBlock(BlockItemUseContext blockitemusecontext, BlockState blockstate) {
        throw new IllegalStateException("Mixin failed to shadow getBlockItemUseContext()");
    }

    @Shadow
    public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context) {
        throw new IllegalStateException("Mixin failed to shadow getBlockItemUseContext()");
    }

    @Shadow
    protected BlockState getStateForPlacement(BlockItemUseContext context) {
        throw new IllegalStateException("Mixin failed to shadow getStateForPlacement()");
    }
}
