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
        var tag = getOrSetTag(stack);
        Migration.setCurrentVersion(tag);
        tag.put(MODS, new CompoundTag());
        return stack;
    }

    public static Map<String, List<ItemStack>> getModsBooks(ItemStack stack) {
        var tag = getOrSetTag(stack);

        Migration.Apply(tag);

        var books = new HashMap<String, List<ItemStack>>();
        var mods = tag.getCompound(MODS);
        for (var mod : mods.getAllKeys()) {
            var booksTag = mods.getCompound(mod);
            var booksList = books.getOrDefault(mod, new ArrayList<ItemStack>());
            for (var book : booksTag.getAllKeys()) {
                booksList.add(ItemStack.of(booksTag.getCompound(book)));
            }
            books.put(mod, booksList);
        }

        return books;
    }

    public static void setModsBooks(ItemStack stack, Map<String, List<ItemStack>> modsBooks) {
        var tag = getOrSetTag(stack);

        Migration.Apply(tag);

        var mods = new CompoundTag();
        for (var mod : modsBooks.keySet()) {
            var booksTag = new CompoundTag();
            var booksList = modsBooks.get(mod);
            for (var i = 0; i < booksList.size(); i++) {
                var key = Integer.toString(i);
                var bookTag = booksList.get(i).save(new CompoundTag());
                booksTag.put(key, bookTag);
            }
            if (!booksList.isEmpty()) mods.put(mod, booksTag);
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
        var mods = getOrSetMods(getOrSetTag(source)).copy();
        getOrSetTag(target).put(MODS, mods);
    }

    public static boolean isTome(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(IS_TOME);
    }

    public static void clear(ItemStack stack) {
        var tag = stack.getTag();

        tag.remove(MODS);
        tag.remove(IS_TOME);
        if (tag.isEmpty()) stack.setTag(null);
    }

    public static void fill(ItemStack stack, boolean isTome) {
        var tag = stack.getTag();

        tag.putBoolean(Tag.IS_TOME, isTome);
    }
}
