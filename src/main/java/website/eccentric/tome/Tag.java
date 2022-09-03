package website.eccentric.tome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class Tag {
    public static final String MODS = key("mods");
    public static final String IS_TOME = key("is_tome");
    public static final String VERSION = key("version");

    public static class Patchouli {
        public static final String BOOK = key(ModName.PATCHOULI, "book");
    }

    public static CompoundNBT serialize(Map<String, List<ItemStack>> modsBooks) {
        CompoundNBT tag = new CompoundNBT();

        for (String mod : modsBooks.keySet()) {
            CompoundNBT booksTag = new CompoundNBT();
            List<ItemStack> booksList = modsBooks.get(mod);

            for (int i = 0; i < booksList.size(); i++) {
                String key = Integer.toString(i);
                CompoundNBT bookTag = booksList.get(i).save(new CompoundNBT());
                booksTag.put(key, bookTag);
            }
            if (!booksList.isEmpty()) tag.put(mod, booksTag);
        }

        return tag;
    }

    public static Map<String, List<ItemStack>> deserialize(CompoundNBT mods) {
        Map<String, List<ItemStack>> modsBooks = new HashMap<String, List<ItemStack>>();

        for (String mod : mods.getAllKeys()) {
            CompoundNBT booksTag = mods.getCompound(mod);
            List<ItemStack> books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());

            for (String book : booksTag.getAllKeys()) {
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
