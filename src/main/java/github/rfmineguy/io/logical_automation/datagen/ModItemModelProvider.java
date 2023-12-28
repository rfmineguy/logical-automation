package github.rfmineguy.io.logical_automation.datagen;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jline.utils.Log;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, LogicalAutomation.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(Registration.Items.SIMPLE_BLOCK_ITEM);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> itemRegistryObject) {
        return withExistingParent(itemRegistryObject.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(LogicalAutomation.MODID, "item/" + itemRegistryObject.getId().getPath()));
    }
}
