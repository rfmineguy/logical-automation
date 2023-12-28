package github.rfmineguy.io.logical_automation.datagen;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LogicalAutomation.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(Registration.Blocks.SIMPLE_BLOCK);
        blockWithItem(Registration.Blocks.CONTROLLER_BLOCK);
    }

    public <T extends Block> void blockWithItem(RegistryObject<T> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
