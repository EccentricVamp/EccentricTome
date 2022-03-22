package website.eccentric.tome;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class Tag {

    public static final String BOOKS = "eccentrictome:books";
    public static final String MOD = "eccentrictome:mod";
    public static final String NAME = "eccentrictome:name";
    public static final String IS_TOME = "eccentrictome:is_tome";

    public static boolean hasBooks(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(BOOKS);
    }

    public static boolean hasBook(ItemStack stack, String mod) {
        return hasBooks(stack) && getBooks(stack).contains(mod);
    }

    public static CompoundTag getBooks(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        var tag = stack.getTag();

        if (!tag.contains(BOOKS)) tag.put(BOOKS, new CompoundTag());
        return tag.getCompound(BOOKS);
    }

    public static CompoundTag getBook(ItemStack stack, String mod) {
        return getBooks(stack).getCompound(mod);
    }

    public static void setBooks(ItemStack stack, CompoundTag books) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        stack.getTag().put(BOOKS, books);
    }

    public static void addBook(ItemStack stack, ItemStack book) {
        if (!hasBooks(stack)) setBooks(stack, new CompoundTag());
        
        var books = getBooks(stack);
		books.put(Mod.from(book), book.save(new CompoundTag()));
    }

    public static CompoundTag popBook(ItemStack stack, String mod) {
        var book = getBook(stack, mod);
        var books = getBooks(stack);
        books.remove(mod);
        return book;
    }

    public static void copyBooks(ItemStack source, ItemStack target) {
        var books = getBooks(source).copy();
        setBooks(target, books);
    }

    public static String getMod(ItemStack stack) {
        return stack.getTag().getString(MOD);
    }

    public static boolean isTome(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(IS_TOME);
    }

    public static boolean hasName(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(NAME);
    }

    public static void clear(ItemStack stack) {
        var tag = stack.getTag();

        tag.remove(BOOKS);
        tag.remove(MOD);
        tag.remove(NAME);
        tag.remove(IS_TOME);
        if (tag.isEmpty()) stack.setTag(null);
    }

    public static void fill(ItemStack stack, String mod, String name, boolean isTome) {
        var tag = stack.getTag();

        tag.putString(Tag.MOD, mod);
        tag.putString(Tag.NAME, name);
		tag.putBoolean(Tag.IS_TOME, isTome);
    }

}
