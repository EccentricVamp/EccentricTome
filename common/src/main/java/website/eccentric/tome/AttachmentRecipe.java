package website.eccentric.tome;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import website.eccentric.tome.util.IModName;
import com.google.inject.Inject;

public class AttachmentRecipe extends CustomRecipe {

    @Inject
    private IConfiguration configuration;

    @Inject
    private IModName modName;

    @Inject
    private AttachmentSerializer serializer;

    @Inject
    private TomeItem tomeItem;

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

        return tomeItem.attach(tome, target);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public boolean isTarget(ItemStack stack) {
        if (stack.isEmpty() || TomeItem.isTome(stack)) return false;

        var mod = modName.from(stack);
        if (mod.equals("minecraft")) return false;

        if (configuration.AllItems()) return true;

        if (configuration.Exclude().contains(mod)) return false;

        var location = Registry.ITEM.getKey(stack.getItem());
        var locationString = location.toString();
        var locationDamage = locationString + ":" + stack.getDamageValue();

        if (configuration.ExcludeItems().contains(locationString) || configuration.ExcludeItems().contains(locationDamage)) return false;

        if (configuration.Items().contains(locationString) || configuration.Items().contains(locationDamage)) return true;

        var path = location.getPath();
        for (var name : configuration.Names()) {
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
        return serializer;
    }

}