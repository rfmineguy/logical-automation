package github.rfmineguy.io.logical_automation.blocks.controller.tabs;

import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockMenu;
import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public abstract class ControllerBlockTab {
    abstract public <T extends ControllerBlockScreen> void renderBackground(T screen, GuiGraphics guiGraphics);
    abstract public <T extends ControllerBlockScreen> void renderBg(T screen, GuiGraphics guiGraphics, float v, int i, int i1);
    abstract public <T extends ControllerBlockScreen> void render(T screen, GuiGraphics guiGraphics);

    abstract public <T extends ControllerBlockMenu> void replaceSlots(T containerMenu, Container container);

    abstract public <T extends ControllerBlockMenu> ItemStack quickMoveStacks(T containerMenu, Player player, int index);
}
