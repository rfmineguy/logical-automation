package github.rfmineguy.io.logical_automation.init;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.blocks.cable.CableBlock;
import github.rfmineguy.io.logical_automation.blocks.cable.CableBlockEntity;
import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlock;
import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockEntity;
import github.rfmineguy.io.logical_automation.blocks.SimpleBlock;
import github.rfmineguy.io.logical_automation.blocks.controller.ControllerBlockMenu;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Registration {
    /**
     * Registries
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LogicalAutomation.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LogicalAutomation.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LogicalAutomation.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LogicalAutomation.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, LogicalAutomation.MODID);


    /**
     * Blocks
     */
    public static final RegistryObject<SimpleBlock> SIMPLE_BLOCK = registerBlock("simple_block", SimpleBlock::new);
    public static final RegistryObject<Block> CONTROLLER_BLOCK = registerBlock("controller_block", () -> new ControllerBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> CABLE_BLOCK = registerBlock("cable_core", () -> new CableBlock(BlockBehaviour.Properties.of()));

    /**
     * Menus
     */
    public static final RegistryObject<MenuType<ControllerBlockMenu>> CONTROLLER_BLOCK_MENU = registerMenuType("controller_menu", ControllerBlockMenu::new);

    /**
     * Block Entities
     */
    public static final RegistryObject<BlockEntityType<ControllerBlockEntity>> CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("controller_block_entity",
                    () -> BlockEntityType.Builder.of(ControllerBlockEntity::new, CONTROLLER_BLOCK.get()).build(null)
            );
    public static final RegistryObject<BlockEntityType<CableBlockEntity>> CABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cable_block_entity",
                    () -> BlockEntityType.Builder.of(CableBlockEntity::new, CABLE_BLOCK.get()).build(null)
            );

    /**
     * Tabs
     */
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("logistical_automation_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(CONTROLLER_BLOCK.get().asItem()))
                    .title(Component.translatable("creativetab.logistical_automation_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(SIMPLE_BLOCK.get().asItem());
                        output.accept(CONTROLLER_BLOCK.get().asItem());
                    }))
                    .build());

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> t = BLOCKS.register(name, block);
        registerBlockItem(name, t);
        return t;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void registerAll(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MENUS.register(modEventBus);
    }
}
