package website.eccentric.tome;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import website.eccentric.tome.CommonConfiguration.Cache;

public class AttachmentRecipe extends CustomRecipe {

    public AttachmentRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    public boolean matches(CraftingContainer crafting, Level level) {
        var foundTome = false;
        var foundTarget = false;

        for (var i = 0; i < crafting.getContainerSize(); i++) {
            var stack = crafting.getItem(i);
            if (stack.isEmpty()) continue;
            
            if (isTarget(stack)) {
                if (foundTarget) return false;
                foundTarget = true;
            }
            else if (stack.getItem() instanceof TomeItem) {
                if (foundTome) return false;
                foundTome = true;
            }
            else return false;
        }

        return foundTome && foundTarget;
    }

    @Override
    public ItemStack assemble(CraftingContainer crafting) {
        var tome = ItemStack.EMPTY;
        var target = ItemStack.EMPTY;

        for (var i = 0; i < crafting.getContainerSize(); i++) {
            var stack = crafting.getItem(i);
            if (stack.isEmpty()) continue;
            
            if (stack.getItem() instanceof TomeItem) tome = stack;
            else target = stack;
        }

        tome = tome.copy();

        return TomeItem.attach(tome, target);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public boolean isTarget(ItemStack stack) {
        if (stack.isEmpty() || TomeItem.isTome(stack)) return false;

        var mod = Mod.from(stack);
        if (mod.equals(Mod.MINECRAFT)) return false;

        if (Cache.ALL_ITEMS) return true;

        if (Cache.EXCLUDE.contains(mod)) return false;

        var location = stack.getItem().getRegistryName();
        var locationString = location.toString();
        var locationDamage = locationString + ":" + stack.getDamageValue();

        if (Cache.EXCLUDE_ITEMS.contains(locationString) || Cache.EXCLUDE_ITEMS.contains(locationDamage)) return false;

        if (Cache.ITEMS.contains(locationString) || Cache.ITEMS.contains(locationDamage)) return true;

        var path = location.getPath();
        for (var name : Cache.NAMES) {
            if (path.contains(name)) return true;
        }

        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer crafting) {
        return NonNullList.withSize(crafting.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EccentricTome.ATTACHMENT.get();
    }

}