package website.eccentric.tome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class Tag {

    public static final String MODS = "eccentrictome:mods";
    public static final String IS_TOME = "eccentrictome:is_tome";

    public static ItemStack initialize(ItemStack stack) {
        CompoundNBT tag = getOrSetTag(stack);
        Migration.setCurrentVersion(tag);
        tag.put(MODS, new CompoundTag());
        return stack;
    }

    public static Map<String, List<ItemStack>> getModsBooks(ItemStack stack) {
        CompoundNBT tag = getOrSetTag(stack);

        Migration.Apply(tag);

        Map<String, List<ItemStack>> books = new HashMap<String, List<ItemStack>>();
        CompoundNBT mods = tag.getCompound(MODS);
        for (String mod : mods.getAllKeys()) {
            CompoundNBT booksTag = mods.getCompound(mod);
            List<ItemStack> booksList = books.getOrDefault(mod, new ArrayList<ItemStack>());
            for (String book : booksTag.getAllKeys()) {
                booksList.add(ItemStack.of(booksTag.getCompound(book)));
            }
            books.put(mod, booksList);
        }

        return books;
    }

    public static void setModsBooks(ItemStack stack, Map<String, List<ItemStack>> modsBooks) {
        CompoundNBT tag = getOrSetTag(stack);

        Migration.Apply(tag);

        CompoundNBT mods = new CompoundNBT();
        for (String mod : modsBooks.keySet()) {
            CompoundNBT booksTag = new CompoundNBT();
            List<ItemStack> booksList = modsBooks.get(mod);
            for (int i = 0; i < booksList.size(); i++) {
                String key = Integer.toString(i);
                CompoundNBT bookTag = booksList.get(i).save(new CompoundNBT());
                booksTag.put(key, bookTag);
            }
            mods.put(mod, booksTag);
        }

        tag.put(MODS, mods);
    }

    public static CompoundTag getOrSetTag(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        return stack.getTag();
    }

    public static CompoundTag getOrSetMods(CompoundTag tag) {
        if (!tag.contains(MODS)) tag.put(MODS, new CompoundTag());
        return tag.getCompound(MODS);
    }

    public static void setMods(ItemStack stack, CompoundTag mods) {
        getOrSetTag(stack).put(MODS, mods);
    }

    public static void copyMods(ItemStack source, ItemStack target) {
        CompoundNBT mods = getOrSetMods(getOrSetTag(source)).copy();
        getOrSetTag(target).put(MODS, mods);
    }

    public static boolean isTome(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(IS_TOME);
    }

    public static void clear(ItemStack stack) {
        CompoundNBT tag = stack.getTag();

        tag.remove(MODS);
        tag.remove(IS_TOME);
        if (tag.isEmpty()) stack.setTag(null);
    }

    public static void fill(ItemStack stack, boolean isTome) {
        CompoundNBT tag = stack.getTag();

        tag.putBoolean(Tag.IS_TOME, isTome);
    }

}
