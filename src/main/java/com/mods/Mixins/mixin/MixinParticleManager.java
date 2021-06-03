package com.mods.Mixins.mixin;

import com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs.DoubleSlabTE;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.DiggingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ParticleManager.class)
public class MixinParticleManager {


    @Shadow
    protected ClientWorld world;

    @Final
    @Shadow
    private final Random rand = new Random();

    @Shadow
    public void addEffect(Particle effect) {
        throw new IllegalStateException("Mixin failed to shadow getBlockItemUseContext()");
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "addBlockHitEffects(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)V")
    public void addBlockHitEffects(BlockPos pos, Direction side, CallbackInfo ci) {
        BlockState blockstate = this.world.getBlockState(pos);
        if (blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AxisAlignedBB axisalignedbb = blockstate.getShape(this.world, pos).getBoundingBox();
            double d0 = (double) i + this.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - (double) 0.2F) + (double) 0.1F + axisalignedbb.minX;
            double d1 = (double) j + this.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - (double) 0.2F) + (double) 0.1F + axisalignedbb.minY;
            double d2 = (double) k + this.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - (double) 0.2F) + (double) 0.1F + axisalignedbb.minZ;
            if (side == Direction.DOWN) {
                d1 = (double) j + axisalignedbb.minY - (double) 0.1F;
            }

            if (side == Direction.UP) {
                d1 = (double) j + axisalignedbb.maxY + (double) 0.1F;
            }

            if (side == Direction.NORTH) {
                d2 = (double) k + axisalignedbb.minZ - (double) 0.1F;
            }

            if (side == Direction.SOUTH) {
                d2 = (double) k + axisalignedbb.maxZ + (double) 0.1F;
            }

            if (side == Direction.WEST) {
                d0 = (double) i + axisalignedbb.minX - (double) 0.1F;
            }

            if (side == Direction.EAST) {
                d0 = (double) i + axisalignedbb.maxX + (double) 0.1F;
            }

            TileEntity entity = world.getTileEntity(pos);
            PlayerEntity player = Minecraft.getInstance().player;
            if (entity instanceof DoubleSlabTE && player != null) {
                BlockRayTraceResult result = world.rayTraceBlocks(
                        player.getPositionVec().add(new Vector3d(0, player.getHeight(), 0)),
                        player.getPositionVec().add(new Vector3d(0, player.getEyeHeight(), 0)).add(player.getLookVec().mul(5, 5, 5)), pos, world.getBlockState(pos).getShape(world, pos), world.getBlockState(pos));
                if (result == null) {
                    this.addEffect((new DiggingParticle(this.world, d0, d1, d2, 0.0D, 0.0D, 0.0D, blockstate)).setBlockPos(pos).multiplyVelocity(0.2F).multiplyParticleScaleBy(0.6F));
                    ci.cancel();
                    return;
                }
                float hitHeight = (float) (result.getHitVec().getY() - pos.getY());
                BlockState state = hitHeight <= .5f ? ((DoubleSlabTE) entity).textures.getKey() : ((DoubleSlabTE) entity).textures.getValue();
                this.addEffect((new DiggingParticle(this.world, d0, d1, d2, 0.0D, 0.0D, 0.0D, state)).setBlockPos(pos).multiplyVelocity(0.2F).multiplyParticleScaleBy(0.6F));
                ci.cancel();
            } else {
                this.addEffect((new DiggingParticle(this.world, d0, d1, d2, 0.0D, 0.0D, 0.0D, blockstate)).setBlockPos(pos).multiplyVelocity(0.2F).multiplyParticleScaleBy(0.6F));
                ci.cancel();
            }
        }
    }

}
