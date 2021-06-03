package com.mods.CrynessDoubleSlabs.Blocks.Double_Slabs;

import com.mods.CrynessDoubleSlabs.Inits.TileEntityTypes;
import javafx.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class DoubleSlabTE extends TileEntity {
    public static final ModelProperty<Pair<BlockState, BlockState>> TOP_AND_BOTTOM_TEXTURE = new ModelProperty<>();

    public Pair<BlockState, BlockState> getTextures() {
        return textures;
    }

    public Pair<BlockState, BlockState> textures = new Pair<>(null, null);

    public DoubleSlabTE() {
        super(TileEntityTypes.SLAB_TE.get());
    }

    public void updateTextures(Pair<BlockState, BlockState> textures) {
        this.textures = textures;
        markDirty();
        if (world == null) {
            return;
        }
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }
    @Override
    public void remove() {
        super.remove();
    }
    // The getUpdateTag()/handleUpdateTag() pair is called whenever the client receives a new chunk
    // it hasn't seen before. i.e. the chunk is loaded
    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        writeTextures(tag);
        return tag;
    }
    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        // This is actually the default but placed here so you
        // know this is the place to potentially have a lighter read() that only
        // considers things needed client-side
        read(state, tag);
    }
    // The getUpdatePacket()/onDataPacket() pair is used when a block update happens on the client
    // (a blockstate change or an explicit notification of a block update from the server). It's
    // easiest to implement them based on getUpdateTag()/handleUpdateTag()
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        handleUpdateTag(getBlockState(), tag);
        if (world == null) {
            return;
        }
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        ModelDataManager.requestModelDataRefresh(this);
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(TOP_AND_BOTTOM_TEXTURE, textures)
                .build();
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT tag) {
        super.read(state, tag);
        readTextures(tag);
    }

    private void readTextures(CompoundNBT nbt) {
        if (nbt.contains("bottom") && nbt.contains("top")) {
            this.textures = new Pair<>(NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(nbt.get("bottom"))), NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(nbt.get("top"))));
        } else {
            this.textures = new Pair<>(nbt.contains("bottom") ? NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(nbt.get("bottom"))) : null, nbt.contains("top") ? NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(nbt.get("top"))) : null);
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT tag) {
        writeTextures(tag);
        return super.write(tag);
    }

    private void writeTextures(CompoundNBT tag) {
        if (this.textures != null) {
            if (this.textures.getKey() != null) {
                tag.put("bottom", NBTUtil.writeBlockState(this.textures.getKey()));
            }
            if (this.textures.getValue() != null) {
                tag.put("top", NBTUtil.writeBlockState(this.textures.getValue()));
            }
        }
    }

}
