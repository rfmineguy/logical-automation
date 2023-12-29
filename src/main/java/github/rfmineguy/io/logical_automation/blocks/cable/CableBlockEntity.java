package github.rfmineguy.io.logical_automation.blocks.cable;

import github.rfmineguy.io.logical_automation.LogicalAutomation;
import github.rfmineguy.io.logical_automation.init.BlockRendererRegistration;
import github.rfmineguy.io.logical_automation.init.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.BitField;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CableBlockEntity extends BlockEntity {
    public CableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.CABLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return super.getCapability(cap);
    }

    public void update() {
        LogicalAutomation.LOGGER.debug("updating cable");
    }

    List<Direction> getNeighborDirections() {
        List<Direction> directions = new ArrayList<>(6);
        assert level != null;
        BlockPos p = worldPosition;
        if (level.getBlockEntity(p.west()) != null)  directions.add(Direction.WEST);
        if (level.getBlockEntity(p.east()) != null)  directions.add(Direction.EAST);
        if (level.getBlockEntity(p.north()) != null) directions.add(Direction.NORTH);
        if (level.getBlockEntity(p.south()) != null) directions.add(Direction.SOUTH);
        if (level.getBlockEntity(p.above()) != null) directions.add(Direction.UP);
        if (level.getBlockEntity(p.below()) != null) directions.add(Direction.DOWN);
        return directions;
    }

    List<CableBlockEntity> getNeighborBlockEntities() {
        List<BlockEntity> blockEntities = new ArrayList<>(6);
        assert level != null;
        BlockPos p = worldPosition;
        blockEntities.add(level.getBlockEntity(p.west()));
        blockEntities.add(level.getBlockEntity(p.east()));
        blockEntities.add(level.getBlockEntity(p.north()));
        blockEntities.add(level.getBlockEntity(p.south()));
        blockEntities.add(level.getBlockEntity(p.above()));
        blockEntities.add(level.getBlockEntity(p.below()));
        return blockEntities.stream().filter(Objects::nonNull).filter(blockEntity -> blockEntity instanceof CableBlockEntity).map(blockEntity -> (CableBlockEntity)blockEntity).toList();
    }
}
