package website.eccentric.tome;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

// TODO: handle migrations
public class Tag {

    public static final String MODS = "eccentrictome:books";
    public static final String NAME = "eccentrictome:name";
    public static final String IS_TOME = "eccentrictome:is_tome";

    public static boolean hasMods(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(MODS);
    }

    public static boolean hasMod(ItemStack stack, String mod) {
        return hasMods(stack) && getMods(stack).contains(mod);
    }

    public static CompoundTag getMods(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        var tag = stack.getTag();

        if (!tag.contains(MODS)) tag.put(MODS, new CompoundTag());
        return tag.getCompound(MODS);
    }

    public static CompoundTag getBooks(ItemStack stack, String mod) {
        var books = getMods(stack).getCompound(mod);
        if (books != null) return books;
        else return new CompoundTag();
    }

    public static CompoundTag getBook(ItemStack stack, String mod, String key) {
        var books = getMods(stack).getCompound(mod);
        
        if (books == null) return null;
        else return books.getCompound(key);
    }

    public static void setMods(ItemStack stack, CompoundTag mods) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        stack.getTag().put(MODS, mods);
    }

    public static void addBook(ItemStack stack, ItemStack book) {
        if (!hasMods(stack)) setMods(stack, new CompoundTag());
        
        var mods = getMods(stack);
        var mod = Mod.from(book);

        if (!mods.contains(mod)) mods.put(mod, new CompoundTag());
        var books = mods.getCompound(mod);

        books.put(Integer.toString(books.size()), book.save(new CompoundTag()));
    }

    public static CompoundTag popBook(ItemStack stack, String mod, String key) {
        var book = getBook(stack, mod, key);

        var mods = getMods(stack);
        var books = mods.getCompound(mod);

        mods.remove(mod);
        books.remove(key);

        if (books.size() > 0) {
            var newBooks = new CompoundTag();
            var i = 0;
            for (var oldKey : books.getAllKeys()) {
                newBooks.put(Integer.toString(i), books.getCompound(oldKey));
                i++;
            }
            mods.put(mod, newBooks);
        }

        return book;
    }

    public static void copyMods(ItemStack source, ItemStack target) {
        var mods = getMods(source).copy();
        setMods(target, mods);
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
