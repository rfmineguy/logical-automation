package github.rfmineguy.io.logical_automation;

import com.mojang.logging.LogUtils;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
}
