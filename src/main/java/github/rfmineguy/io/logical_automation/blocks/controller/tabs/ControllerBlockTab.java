package github.rfmineguy.io.logical_automation.blocks.controller.tabs;

import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public abstract class ControllerBlockTab {
    abstract public void renderBackground(GuiGraphics guiGraphics);
    abstract public void renderBg(GuiGraphics guiGraphics, float v, int i, int i1);
    abstract public void render(GuiGraphics guiGraphics);

    abstract public <T extends ControllerBlockMenu> void replaceSlots(T containerMenu, ItemStackHandler itemStackHandler);

    abstract public <T extends ControllerBlockMenu> ItemStack quickMoveStacks(T containerMenu, Player player, int index);
}
