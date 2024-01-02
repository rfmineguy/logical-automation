package github.rfmineguy.io.logical_automation.blocks.test_chest;

import com.mojang.blaze3d.systems.RenderSystem;
import github.rfmineguy.io.logical_automation.LogicalAutomation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.logging.Log;

public class TestChestBlockScreen extends AbstractContainerScreen<TestChestBlockMenu> {
    // private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_27.png");
    private static final ResourceLocation TEXTURE = new ResourceLocation(LogicalAutomation.MODID, "textures/gui/container/generic_27.png");
    public TestChestBlockScreen(TestChestBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        imageWidth = 176;
        imageHeight = 222;
        inventoryLabelY = 129;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.f, 1.f, 1.f, 1.f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = getGuiLeft();
        int y = getGuiTop();

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
