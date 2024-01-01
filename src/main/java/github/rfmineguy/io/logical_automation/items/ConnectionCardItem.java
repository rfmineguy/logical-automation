package github.rfmineguy.io.logical_automation.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConnectionCardItem extends Item {
    public static final String BLOCK_POS_VALID_TAG = "blockPosValid";
    public static final String BLOCK_POS_TAG = "blockPos";
    public static final String DIMENSION = "dimension";

    public ConnectionCardItem() {
        super(new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getPlayer() == null) return super.useOn(pContext);
        if (!pContext.getPlayer().isCrouching()) return super.useOn(pContext);

        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();

        ItemStack itemStack = pContext.getItemInHand();
        ensureTags(itemStack);
        itemStack.getTag().putBoolean(BLOCK_POS_VALID_TAG, true);
        itemStack.getTag().put(BLOCK_POS_TAG, NbtUtils.writeBlockPos(blockPos));
        itemStack.getTag().putString(DIMENSION, level.dimension().location().getPath());

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        ensureTags(pStack);
        CompoundTag nbt = pStack.getTag();
        assert nbt != null;

        assert Minecraft.getInstance().player != null;
        if (Screen.hasShiftDown()) {
            // Write the block pos to the tooltip
            boolean valid = nbt.getBoolean(BLOCK_POS_VALID_TAG);
            BlockPos pos = NbtUtils.readBlockPos(nbt.getCompound(BLOCK_POS_TAG));
            {
                String tooltip = valid ? "BlockPos : " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() : "BlockPos : <invalid>";
                pTooltipComponents.add(Component.literal(tooltip));
            }
            {
                String tooltip = valid ? pLevel.getBlockState(pos).getBlock().toString() : "Block : <invalid>";
                pTooltipComponents.add(Component.literal(tooltip));
            }

            // Write the dimension to the tooltip
            {
                String tooltip = !nbt.getString(DIMENSION).isEmpty() ? "Dimension : " + nbt.getString(DIMENSION) : "Dimension : <invalid>";
                pTooltipComponents.add(Component.literal(tooltip));
            }
        }
        else {
            pTooltipComponents.add(Component.literal("Hold Shift for more info"));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }

    private void ensureTags(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (!tag.contains(DIMENSION))
            tag.putInt(DIMENSION, -1);
        if (!tag.contains(BLOCK_POS_TAG))
            tag.put(BLOCK_POS_TAG, NbtUtils.writeBlockPos(BlockPos.ZERO));
        if (!tag.contains(BLOCK_POS_VALID_TAG))
            tag.putBoolean(BLOCK_POS_VALID_TAG, false);
        itemStack.setTag(tag);
    }
}
