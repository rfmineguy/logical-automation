package github.rfmineguy.io.logical_automation.init;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.blocks.cable.CableBlockEntityRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockRendererRegistration {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        LogicalAutomation.LOGGER.debug("Registering block entity renderer");
        event.registerBlockEntityRenderer(Registration.CABLE_BLOCK_ENTITY.get(), CableBlockEntityRenderer::new);
    }
}
