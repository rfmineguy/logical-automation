package github.rfmineguy.io.logical_automation.blocks.controller;

import com.mojang.blaze3d.systems.RenderSystem;
import github.rfmineguy.io.logical_automation.LogicalAutomation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ControllerBlockScreen extends AbstractContainerScreen<ControllerBlockMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(LogicalAutomation.MODID, "textures/gui/controller_gui.png");

    private static final ResourceLocation BACKGROUND =
            new ResourceLocation("minecraft", "textures/gui/container/creative_inventory/tab_items.png");

    private static final ResourceLocation TAB =
            new ResourceLocation("minecraft", "textures/gui/container/creative_inventory/tabs.png");

    public ControllerBlockScreen(ControllerBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000; // remove this label
        this.titleLabelY = 10000; // remove this label
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        super.renderBackground(pGuiGraphics);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);
        RenderSystem.setShaderTexture(0, TAB);
        pGuiGraphics.blit(TAB, x, y - 26, 0, 32, 26, 30);

        // try {
        //     menu.tabs.get(menu.selectedTab).renderBackground(pGuiGraphics);
        // } catch (IndexOutOfBoundsException e) {
        //     LogicalAutomation.LOGGER.error(e.getLocalizedMessage());
        // }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float v, int i, int i1) {
        // try {
        //     menu.tabs.get(menu.selectedTab).renderBg(pGuiGraphics, v, i, i1);
        // } catch (IndexOutOfBoundsException e) {
        //     LogicalAutomation.LOGGER.error(e.getLocalizedMessage());
        // }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
