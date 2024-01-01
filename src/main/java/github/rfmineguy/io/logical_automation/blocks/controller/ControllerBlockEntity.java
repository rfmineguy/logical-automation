package github.rfmineguy.io.logical_automation.blocks.controller;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ControllerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemStackhandler = new ItemStackHandler(2);
    private LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.empty();

    protected final ItemStackHandler networkCardStorage = new ItemStackHandler(5 * 9);
    private LazyOptional<IItemHandler> networkCardStorageLazy = LazyOptional.empty();

    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress = 78;

    public ControllerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.CONTROLLER_BLOCK_ENTITY.get(), pPos, pBlockState);

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ControllerBlockEntity.this.progress;
                    case 1 -> ControllerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> ControllerBlockEntity.this.progress = i1;
                    case 1 -> ControllerBlockEntity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void drops() {
        SimpleContainer simpleContainer = new SimpleContainer(itemStackhandler.getSlots());
        for (int i = 0; i < itemStackhandler.getSlots(); i++) {
            simpleContainer.setItem(i, itemStackhandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, simpleContainer);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return networkCardStorageLazy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        itemHandlerLazyOptional = LazyOptional.of(() -> itemStackhandler);
        networkCardStorageLazy = LazyOptional.of(() -> networkCardStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerLazyOptional.invalidate();
        networkCardStorageLazy.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.logicalautomation.controller_block");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemStackhandler.serializeNBT());
        pTag.put("networkCardStorage", networkCardStorage.serializeNBT());
        pTag.putInt("controller.progress", this.progress);
        super.saveAdditional(pTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemStackhandler.deserializeNBT(pTag.getCompound("inventory"));
        networkCardStorage.deserializeNBT(pTag.getCompound("networkCardStorage"));
        this.progress = pTag.getInt("controller.progress");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        LogicalAutomation.LOGGER.debug("createMenu");
        return new ControllerBlockMenu(i, inventory, this, this.data);
    }

    public void tick(Level pLevel, BlockPos blockPos, BlockState blockState) {
    }
}
