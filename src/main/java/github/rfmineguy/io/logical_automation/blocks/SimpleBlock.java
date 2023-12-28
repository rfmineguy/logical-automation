package github.rfmineguy.io.logical_automation.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class SimpleBlock extends Block {
    public SimpleBlock() {
        super(Properties.of().strength(3.5f).requiresCorrectToolForDrops().sound(SoundType.ANVIL).randomTicks());
    }
}
