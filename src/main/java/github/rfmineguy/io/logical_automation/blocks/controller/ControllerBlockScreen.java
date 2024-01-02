package github.rfmineguy.io.logical_automation.blocks.controller;

import com.mojang.blaze3d.systems.RenderSystem;
import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.blocks.controller.tabs.NetworkCardStorageTab;
import github.rfmineguy.io.logical_automation.init.Registration;
import github.rfmineguy.io.logical_automation.items.ConnectionCardItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.extensions.IForgeGuiGraphics;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ControllerBlockScreen extends AbstractContainerScreen<ControllerBlockMenu> {
    private static final ResourceLocation CONTROLLER =
            new ResourceLocation(LogicalAutomation.MODID, "textures/gui/controller_gui.png");

    private static final ResourceLocation GENERIC54 =
            new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    private static final ResourceLocation TAB =
            new ResourceLocation("minecraft", "textures/gui/container/creative_inventory/tabs.png");

    private List<Button> buttons = new ArrayList<>();

    public ControllerBlockScreen(ControllerBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        LogicalAutomation.LOGGER.debug("Screen init");
        this.inventoryLabelY = 10000; // remove this label
        this.titleLabelY = 10000; // remove this label
        {
            menu.tabs2.clear();
            // NetworkController
            menu.tabs2.add(new ControllerBlockMenu.ControllerTab() {
                @Override
                int getTabIndex() {
                    return 0;
                }

                @Override
                String getTabTitle() {
                    return "Controller";
                }

                @Override
                void render(GuiGraphics guiGraphics) {
                    ControllerBlockEntity entity = menu.blockEntity;
                    entity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {
                        if (itemHandler instanceof ControllerBlockEntity.NetworkItemStackHandler stackHandler) {
                            List<ItemStack> stacks = stackHandler.getStacks().stream().filter(itemStack -> !itemStack.isEmpty()).toList();
                            for (int i = 0; i < stacks.size(); i++) {
                                ItemStack stack = stacks.get(i);
                                boolean validBlockPos = stack.getTag().getBoolean(ConnectionCardItem.BLOCK_POS_VALID_TAG);
                                if (validBlockPos) {
                                    BlockPos pos = NbtUtils.readBlockPos(stack.getTag().getCompound(ConnectionCardItem.BLOCK_POS_TAG));
                                    String dimension = stack.getTag().getString(ConnectionCardItem.DIMENSION);

                                    guiGraphics.drawString(font, Component.literal(dimension), getGuiLeft() + 10, getGuiTop() + 10 + i * 35, IForgeGuiGraphics.DEFAULT_BACKGROUND_COLOR);
                                    guiGraphics.drawString(font, Component.literal(pos.toShortString()), getGuiLeft() + 10, getGuiTop() + 25 + i * 35, IForgeGuiGraphics.DEFAULT_BACKGROUND_COLOR);
                                }
                            }
                        }
                    });
                }

                @Override
                void renderBackground(GuiGraphics guiGraphics) {
                    int x = getGuiLeft();
                    int y = getGuiTop();
                    imageWidth = 176;
                    imageHeight = 166;
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderSystem.setShaderTexture(0, CONTROLLER);
                    guiGraphics.blit(CONTROLLER, x, y, 0, 0, getXSize(), getYSize());
                }

                @Override
                void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {}

                @Override
                void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {}

                @Override
                void replaceSlots() {}

                @Override
                ItemStack quickMoveStacks(Player player, int index) {
                    return null;
                }
            });

            // NetworkItemContainer
            menu.tabs2.add(new ControllerBlockMenu.ControllerTab() {
                private class StorageSlot extends SlotItemHandler {
                    public StorageSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
                        super(itemHandler, index, xPosition, yPosition);
                    }

                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return stack.getItem() instanceof ConnectionCardItem;
                    }
                }
                @Override
                String getTabTitle() {
                    return "Network Storage";
                }

                @Override
                int getTabIndex() {
                    return 1;
                }

                @Override
                void render(GuiGraphics guiGraphics) {
                    // Unused (Cant use right now) (Inoperative)
                }

                @Override
                void renderBackground(GuiGraphics guiGraphics) {
                    int x = getGuiLeft();
                    int y = getGuiTop();
                    imageWidth = 176;
                    imageHeight = 222;
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderSystem.setShaderTexture(0, GENERIC54);
                    guiGraphics.blit(GENERIC54, x, y, 0, 0, getXSize(), getYSize());
                }

                @Override
                void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
                }

                @Override
                void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
                }

                @Override
                void replaceSlots() {
                    LogicalAutomation.LOGGER.debug("replaceSlots() tab1");
                    menu.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {
                        LogicalAutomation.LOGGER.debug("Found capability down");
                        for (int j = 0; j < 6; j++) {
                            for (int i = 0; i < 9; i++) {
                                int index = j * 9 + i;
                                LogicalAutomation.LOGGER.debug("" + index);
                                menu.addSlot(new StorageSlot(itemHandler, index, 8 + i * 18, 18 + j * 18));
                            }
                        }
                    });
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {
                            int index = j + i * 9;
                            index += 9;
                            menu.addSlot(new Slot(menu.playerInventory, index, 8 + j * 18, 140 + i * 18));
                        }
                    }
                    for (int i = 0; i < 9; i++) {
                        menu.addSlot(new Slot(menu.playerInventory, i, 8 + i * 18, 198));
                    }
                    LogicalAutomation.LOGGER.debug("Slots : " + menu.slots.size());
                }

                private static final int HOTBAR_SLOT_COUNT = 9;
                private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
                private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
                private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
                private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
                private static final int VANILLA_FIRST_SLOT_INDEX = 0;
                private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

                // THIS YOU HAVE TO DEFINE!
                private static final int TE_INVENTORY_SLOT_COUNT = 6 * 9;  // must be the number of slots you have!

                ItemStack quickMoveStacks(Player player, int index) {
                    Slot sourceSlot = menu.slots.get(index);
                    if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
                    ItemStack sourceStack = sourceSlot.getItem();
                    ItemStack copyOfSourceStack = sourceStack.copy();

                    LogicalAutomation.LOGGER.debug("" + index);
                    // Check if the slot clicked is one of the vanilla container slots
                    if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
                        // This is a vanilla container slot so merge the stack into the tile inventory
                        if (!menu.moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, true)) {
                            return ItemStack.EMPTY;  // EMPTY_ITEM
                        }
                    } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
                        // This is a TE slot so merge the stack into the players inventory
                        if (!menu.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
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
                    sourceSlot.onTake(player, sourceStack);
                    return copyOfSourceStack;
                }

            });
            // menu.setSelectedTab(0);
        }

        for (int i = 0; i < menu.tabs2.size(); i++) {
            int finalI = i;
            String tabTitle = menu.tabs2.get(i).getTabTitle();
            addRenderableWidget(Button.builder(Component.literal(tabTitle), button -> {
                menu.setSelectedTab(finalI);
            }).bounds(getGuiLeft() + i * 26 + 8, getGuiTop() - 20, 20, 20).tooltip(Tooltip.create(Component.literal(tabTitle))).build());
        }
    }

    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        super.resize(pMinecraft, pWidth, pHeight);
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        super.renderBackground(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TAB);
        for (int i = 0; i < menu.tabs2.size(); i++) {
            pGuiGraphics.blit(TAB, getGuiLeft() + i * 28, getGuiTop() + 2, 0, 0, 26, 30);
        }
        menu.tabs2.stream().filter(controllerTab -> controllerTab.getTabIndex() == menu.selectedTab).findFirst().ifPresent(controllerTab -> controllerTab.renderBackground(pGuiGraphics));
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float v, int i, int i1) {
        menu.tabs2.stream().filter(controllerTab -> controllerTab.getTabIndex() == menu.selectedTab).findFirst().ifPresent(controllerTab -> controllerTab.renderBg(pGuiGraphics, v, i, i1));
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        menu.tabs2.stream().filter(controllerTab -> controllerTab.getTabIndex() == menu.selectedTab).findFirst().ifPresent(controllerTab -> controllerTab.renderTooltip(pGuiGraphics, pX, pY));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        menu.tabs2.stream().filter(controllerTab -> controllerTab.getTabIndex() == menu.selectedTab).findFirst().ifPresent(controllerTab -> controllerTab.render(pGuiGraphics));
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
