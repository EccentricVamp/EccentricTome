package website.eccentric.tome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class Tag {
    public static final String MODS = key("mods");
    public static final String IS_TOME = key("is_tome");
    public static final String VERSION = key("version");

    public class Patchouli {
        public static final String BOOK = key(ModName.PATCHOULI, "book");
    }

    public static CompoundTag serialize(Map<String, List<ItemStack>> modsBooks) {
        var tag = new CompoundTag();

        for (var mod : modsBooks.keySet()) {
            var booksTag = new CompoundTag();
            var booksList = modsBooks.get(mod);

            for (var i = 0; i < booksList.size(); i++) {
                var key = Integer.toString(i);
                var bookTag = booksList.get(i).save(new CompoundTag());
                booksTag.put(key, bookTag);
            }

            if (!booksList.isEmpty())
                tag.put(mod, booksTag);
        }

        return tag;
    }

    public static Map<String, List<ItemStack>> deserialize(CompoundTag mods) {
        var modsBooks = new HashMap<String, List<ItemStack>>();

        for (var mod : mods.getAllKeys()) {
            var booksTag = mods.getCompound(mod);
            var books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());

            for (var book : booksTag.getAllKeys()) {
                books.add(ItemStack.of(booksTag.getCompound(book)));
            }

            modsBooks.put(mod, books);
        }

        return modsBooks;
    }

    public static String key(String path) {
        return key(EccentricTome.ID, path);
    }

    public static String key(String namespace, String path) {
        return namespace + ":" + path;
    }
}
