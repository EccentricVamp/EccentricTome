package website.eccentric.tome;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import website.eccentric.tome.services.Configuration;
import website.eccentric.tome.services.ModName;
import website.eccentric.tome.services.Services;
import website.eccentric.tome.services.Tome;

public class AttachmentRecipe extends CustomRecipe {

    public static SimpleRecipeSerializer<AttachmentRecipe> SERIALIZER;

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

        return Services.load(Tome.class).attach(tome, target);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public boolean isTarget(ItemStack stack) {
        var configuration = Services.load(Configuration.class);
        var modName = Services.load(ModName.class);

        if (stack.isEmpty() || TomeItem.isTome(stack)) return false;

        var mod = modName.from(stack);
        if (mod.equals("minecraft")) return false;

        if (configuration.allItems()) return true;

        if (configuration.exclude().contains(mod)) return false;

        var location = Registry.ITEM.getKey(stack.getItem());
        var locationString = location.toString();
        var locationDamage = locationString + ":" + stack.getDamageValue();

        if (configuration.excludeItems().contains(locationString) || configuration.excludeItems().contains(locationDamage)) return false;

        if (configuration.items().contains(locationString) || configuration.items().contains(locationDamage)) return true;

        var path = location.getPath();
        for (var name : configuration.names()) {
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
        return SERIALIZER;
    }
}