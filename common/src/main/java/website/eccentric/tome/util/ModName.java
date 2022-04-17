package website.eccentric.tome.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface ModName {
    public String from(BlockState state);
    public String from(ItemStack stack);
    public String orAlias(String mod);
    public String name(String mod);
}