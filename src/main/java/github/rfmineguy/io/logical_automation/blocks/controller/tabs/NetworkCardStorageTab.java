package github.rfmineguy.io.logical_automation.blocks.controller.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockMenu;
import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NetworkCardStorageTab extends ControllerBlockTab {
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!

    // private static final ResourceLocation BACKGROUND = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(LogicalAutomation.MODID, "textures/gui/controller_network_storage_gui.png");

    public NetworkCardStorageTab() {}

    @Override
    public void renderBackground(ControllerBlockScreen screen, GuiGraphics guiGraphics) {
        int x = screen.getGuiLeft();
        int y = screen.getGuiTop();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        guiGraphics.blit(BACKGROUND, x, y, 0, 0, screen.getXSize(), screen.getYSize());
    }

    public void renderBg(ControllerBlockScreen screen, GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    @Override
    public void render(ControllerBlockScreen screen, GuiGraphics guiGraphics) {

    }

    public void replaceSlots(ControllerBlockMenu containerMenu, Container container, Inventory playerInventory) {
        container.startOpen(playerInventory.player);
        // for (int k = 0; k < 9; ++k) {
        //     containerMenu.slots.add(new Slot(playerInventory, k, 8 + k * 18, 144));
        // }

        /*
        for(int j = 0; j < 3; ++j) {
            for(int i = 0; i < 9; ++i) {
                int slotIndex = j * 9 + i + 9;
                containerMenu.slots.add(new Slot(playerInventory, slotIndex, 8 + i * 18, 86 + j * 18));
            }
        }
         */

        for(int j = 0; j < 9; ++j) {
            containerMenu.slots.add(new Slot(playerInventory, j, 8 + j * 18, 144));
        }
    }

    @Override
    public void replaceSlots(ControllerBlockMenu containerMenu, Container container) {
        /*
        containerMenu.slots.clear();

        // Main container storage
        final int SLOTS_WIDE = 9, SLOTS_TALL = 3;
        for (int j = 0; j < SLOTS_TALL; j++) {
            for (int i = 0; i < SLOTS_WIDE; i++) {
                int slotIndex = j * SLOTS_WIDE + i;
                LogicalAutomation.LOGGER.debug("i="+i+"; j="+j+"; slotIndex="+slotIndex);
                if (slotIndex >= itemStackHandler.getSlots())
                    continue;
                containerMenu.slots.add(slotIndex, new SlotItemHandler(itemStackHandler, slotIndex, 8 + 18 * i, 18 * j + 18));
            }
        }
         */
    }

    @Override
    public ItemStack quickMoveStacks(ControllerBlockMenu containerMenu, Player playerIn, int index) {
        Slot sourceSlot = containerMenu.slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!containerMenu.moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!containerMenu.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
}
