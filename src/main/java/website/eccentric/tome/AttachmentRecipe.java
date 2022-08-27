package website.eccentric.tome;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AttachmentRecipe extends SpecialRecipe {
    public static IRecipeSerializer<?> SERIALIZER;

    public AttachmentRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    public boolean matches(CraftingInventory crafting, World level) {
        boolean foundTome = false;
        boolean foundTarget = false;

        for (int i = 0; i < crafting.getContainerSize(); i++) {
            ItemStack stack = crafting.getItem(i);
            if (stack.isEmpty()) continue;

            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                return false;
            }            
            if (item instanceof TomeItem) {
                if (foundTome) return false;
                foundTome = true;
            }
            else if (isTarget(stack)) {
                if (foundTarget) return false;
                foundTarget = true;
            }
            else return false;
        }

        return foundTome && foundTarget;
    }

    @Override
    public ItemStack assemble(CraftingInventory crafting) {
        ItemStack tome = ItemStack.EMPTY;
        ItemStack target = ItemStack.EMPTY;

        for (int i = 0; i < crafting.getContainerSize(); i++) {
            ItemStack stack = crafting.getItem(i);
            if (stack.isEmpty()) continue;
            
            if (stack.getItem() instanceof TomeItem) tome = stack;
            else target = stack;
        }

        tome = tome.copy();

        return Tome.attach(tome, target);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public boolean isTarget(ItemStack stack) {
        String mod = ModName.from(stack);
        if (mod.equals("minecraft")) return false;

        if (Configuration.allItems()) return true;

        if (Configuration.exclude().contains(mod)) return false;

        ResourceLocation location = stack.getItem().getRegistryName();
        String locationString = location.toString();
        String locationDamage = locationString + ":" + stack.getDamageValue();

        if (Configuration.excludeItems().contains(locationString) || Configuration.excludeItems().contains(locationDamage)) return false;

        if (Configuration.items().contains(locationString) || Configuration.items().contains(locationDamage)) return true;

        String path = location.getPath();
        for (String name : Configuration.names()) {
            if (path.contains(name)) return true;
        }

        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory crafting) {
        return NonNullList.withSize(crafting.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}