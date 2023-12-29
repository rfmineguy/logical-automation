package github.rfmineguy.io.logical_automation;

import com.mojang.logging.LogUtils;
import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockScreen;
import github.rfmineguy.io.logical_automation.init.BlockRendererRegistration;
import github.rfmineguy.io.logical_automation.init.ModelRegistry;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LogicalAutomation.MODID)
public class LogicalAutomation
{
    public static final String MODID = "logicalautomation";
    public static final Logger LOGGER = LogUtils.getLogger();
    public LogicalAutomation()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.registerAll(modEventBus);

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(ModelRegistry::onModelRegister);
        modEventBus.addListener(BlockRendererRegistration::registerRenderers);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
            });
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(ModelRegistry::onModelRegister);
            modEventBus.addListener(BlockRendererRegistration::registerRenderers);

            MenuScreens.register(Registration.CONTROLLER_BLOCK_MENU.get(), ControllerBlockScreen::new);
        }
    }
}
