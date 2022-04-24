package website.eccentric.tome.services;

import net.minecraft.world.item.ItemStack;

public interface Tome {
    
    public ItemStack attach(ItemStack tome, ItemStack book);
    public ItemStack convert(ItemStack tome, ItemStack book);
    public ItemStack revert(ItemStack book);
}
