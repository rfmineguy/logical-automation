package github.rfmineguy.io.logical_automation.blocks.controller;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.blocks.controller.tabs.ControllerBlockTab;
import github.rfmineguy.io.logical_automation.blocks.controller.tabs.NetworkCardStorageTab;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class ControllerBlockMenu extends AbstractContainerMenu {
    protected ControllerBlockEntity blockEntity;
    public List<ControllerBlockTab> tabs;
    private final Level level;
    public int selectedTab;
    private final ContainerData data;

    public ControllerBlockMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public ControllerBlockMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(Registration.CONTROLLER_BLOCK_MENU.get(), pContainerId);
        checkContainerSize(inv, 2);
        blockEntity = ((ControllerBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;
        this.selectedTab = 0;       // default tab to open to

        tabs = new ArrayList<>();
        NetworkCardStorageTab tab = new NetworkCardStorageTab();
        tabs.add(tab);
        LogicalAutomation.LOGGER.debug("Spacer");
        // LogicalAutomation.LOGGER.debug(tabs.toString());

        // try {
        //     tabs.get(0).replaceSlots(this, blockEntity.networkCardStorage);
        // } catch (IndexOutOfBoundsException e) {
        //     LogicalAutomation.LOGGER.error(e.getLocalizedMessage());
        // }

        addDataSlots(data);
    }

    private void addPlayerInv(Inventory playerInv) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInv, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInv) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    // expose protected method so we can use it in @ControllerBlockTab
    public boolean moveItemStackTo(ItemStack pStack, int pStartIndex, int pEndIndex, boolean pReverseDirection) {
        return super.moveItemStackTo(pStack, pStartIndex, pEndIndex, pReverseDirection);
    }

    // https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.20.X/blob/30-blockEntity/src/main/java/net/kaupenjoe/tutorialmod/screen/GemPolishingStationMenu.java
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        // try {
        //     ControllerBlockTab tab = this.tabs.get(selectedTab);
        //     return tab.quickMoveStacks(this, playerIn, pIndex);
        // } catch (IndexOutOfBoundsException e) {
        //     LogicalAutomation.LOGGER.error(e.getLocalizedMessage());
        //     return ItemStack.EMPTY;
        // }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, Registration.CONTROLLER_BLOCK.get());
    }
}
