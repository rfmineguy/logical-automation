package github.rfmineguy.io.logical_automation.blocks.controller;

import com.mojang.blaze3d.systems.RenderSystem;
import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.blocks.controller.tabs.ControllerBlockTab;
import github.rfmineguy.io.logical_automation.blocks.controller.tabs.NetworkCardStorageTab;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.PrepareRamNearestTarget;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ControllerBlockMenu extends AbstractContainerMenu {
    protected abstract static class ControllerTab {
        abstract void render(GuiGraphics guiGraphics);
        abstract void renderBackground(GuiGraphics guiGraphics);
        abstract void renderBg(GuiGraphics guiGraphics, float v, int i, int i1);
        abstract void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY);
        abstract void replaceSlots();
        abstract ItemStack quickMoveStacks(Player player, int index);
        abstract int getTabIndex();
        abstract String getTabTitle();
    }
    protected ControllerBlockEntity blockEntity;
    public Inventory playerInventory;
    private final Level level;

    public List<ControllerTab> tabs2 = new ArrayList<>();
    public int selectedTab;

    public ControllerBlockMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public ControllerBlockMenu(int pContainerId, Inventory playerInventory, BlockEntity entity) {
        super(Registration.CONTROLLER_BLOCK_MENU.get(), pContainerId);
        checkContainerSize(playerInventory, 3 * 9);
        this.blockEntity = ((ControllerBlockEntity) entity);
        this.playerInventory = playerInventory;
        this.level = playerInventory.player.level();

        setSelectedTab(0);
        /*
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler ->
        {
            final int INV_WIDTH = 9;
            final int INV_HEIGHT = 3;
            for (int j = 0; j < INV_HEIGHT; j++) {
                for (int i = 0; i < INV_WIDTH; i++) {
                    int index = j * 9 + i;
                    this.addSlot(new SlotItemHandler(itemHandler, index, 8 + i * 18, 18 + j * 18));
                }
            }
        });
         */
    }

    public void setSelectedTab(int tab) {
        LogicalAutomation.LOGGER.debug("" + tab + " === " + tabs2.size());
        if (tab < 0) return;
        if (tab >= this.tabs2.size()) return;

        LogicalAutomation.LOGGER.debug("Set tab");
        this.selectedTab = tab;
        this.slots.clear();
        this.tabs2.get(this.selectedTab).replaceSlots();
    }

    public Slot addSlot(Slot slot) {
        return super.addSlot(slot);
    }
    // expose protected method so we can use it in @ControllerTab
    public boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
         LogicalAutomation.LOGGER.debug("moveItemStackTo : " + pStartIndex + " -> "  + pEndIndex);
         return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
    }

    // https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.20.X/blob/30-blockEntity/src/main/java/net/kaupenjoe/tutorialmod/screen/GemPolishingStationMenu.java
    @Override
    public @NotNull ItemStack quickMoveStack(Player playerIn, int pIndex) {
        try {
            LogicalAutomation.LOGGER.debug("quick move stack");
            return tabs2.get(selectedTab).quickMoveStacks(playerIn, pIndex);
        } catch (IndexOutOfBoundsException e) {
            LogicalAutomation.LOGGER.error(e.getLocalizedMessage());
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, Registration.CONTROLLER_BLOCK.get());
    }
}
