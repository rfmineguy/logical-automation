package github.rfmineguy.io.logical_automation.init;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.blocks.ControllerBlock;
import github.rfmineguy.io.logical_automation.blocks.SimpleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static github.rfmineguy.io.logical_automation.init.Registration.Blocks.SIMPLE_BLOCK;
import static github.rfmineguy.io.logical_automation.init.Registration.Registries.BLOCKS;
import static github.rfmineguy.io.logical_automation.init.Registration.Registries.ITEMS;

public class Registration {
    public static class Registries {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LogicalAutomation.MODID);
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LogicalAutomation.MODID);
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LogicalAutomation.MODID);
    }
    public static class BlockEntities {
        // public static final RegistryObject<>
    }
    public static class Blocks {
        public static final RegistryObject<SimpleBlock> SIMPLE_BLOCK = registerBlock("simple_block", SimpleBlock::new);
        public static final RegistryObject<ControllerBlock> CONTROLLER_BLOCK = registerBlock("controller_block", ControllerBlock::new);

        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
            RegistryObject<T> t = BLOCKS.register(name, block);
            registerBlockItem(name, t);
            return t;
        }

        private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
            return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        }
    }
    public static class Items {
        public static final RegistryObject<Item> SIMPLE_BLOCK_ITEM = ITEMS.register("simple_block", () -> new BlockItem(SIMPLE_BLOCK.get(), new Item.Properties()));
    }
    public static class Tabs {
    }
    public static void registerAll(IEventBus modEventBus) {
        Registries.BLOCKS.register(modEventBus);
        Registries.BLOCK_ENTITIES.register(modEventBus);
    }

    @SubscribeEvent
    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(Items.SIMPLE_BLOCK_ITEM);
        }
    }
}
