package github.rfmineguy.io.logical_automation.init;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

public class ModelRegistry {

    /**
     * Models
     */
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        event.register(new ResourceLocation(LogicalAutomation.MODID, "block/cable_core"));
        event.register(new ResourceLocation(LogicalAutomation.MODID, "block/cable_arm_down"));
        event.register(new ResourceLocation(LogicalAutomation.MODID, "block/cable_arm_up"));
        event.register(new ResourceLocation(LogicalAutomation.MODID, "block/cable_arm_east"));
        event.register(new ResourceLocation(LogicalAutomation.MODID, "block/cable_arm_west"));
        event.register(new ResourceLocation(LogicalAutomation.MODID, "block/cable_arm_north"));
        event.register(new ResourceLocation(LogicalAutomation.MODID, "block/cable_arm_south"));
    }
}
