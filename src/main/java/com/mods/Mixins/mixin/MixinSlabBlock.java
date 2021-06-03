package com.mods.Mixins.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlabBlock.class)
public class MixinSlabBlock {
    @Inject(at = @At("HEAD"), method = "isReplaceable(Lnet/minecraft/block/BlockState;Lnet/minecraft/item/BlockItemUseContext;)Z", cancellable = true)
    public void isReplaceable(BlockState state, BlockItemUseContext useContext, CallbackInfoReturnable<Boolean> cir) {
        SlabType slabtype = state.get(SlabBlock.TYPE);
        if (slabtype != SlabType.DOUBLE && useContext.getItem().getItem() instanceof BlockItem && ((BlockItem) useContext.getItem().getItem()).getBlock() instanceof SlabBlock) {
            if (useContext.replacingClickedOnBlock()) {
                boolean flag = useContext.getHitVec().y - (double) useContext.getPos().getY() > 0.5D;
                Direction direction = useContext.getFace();
                if (slabtype == SlabType.BOTTOM) {
                    cir.setReturnValue(direction == Direction.UP || flag && direction.getAxis().isHorizontal());
                } else {
                    cir.setReturnValue(direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal());
                }
            } else {
                cir.setReturnValue(true);
            }
        } else {
            cir.setReturnValue(false);
        }
    }
}
