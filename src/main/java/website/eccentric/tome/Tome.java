package website.eccentric.tome;

import java.util.ArrayList;
import java.util.stream.Collectors;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class Tome {
    public static ItemStack convert(ItemStack tome, ItemStack book) {
        var modsBooks = Tag.getModsBooks(tome);
        var mod = ModName.from(book);
        var books = modsBooks.get(mod);
        var registry = ForgeRegistries.ITEMS.getKey(book.getItem());
        books = books.stream().filter(b -> !ForgeRegistries.ITEMS.getKey(b.getItem()).equals(registry)).collect(Collectors.toList());
        modsBooks.put(mod, books);
        Tag.setModsBooks(tome, modsBooks);

        var name = book.getHoverName().getString();
        Tag.copyMods(tome, book);
        Tag.fill(book, true);

        setHoverName(book, name);
        
        return book;
    }

    public static ItemStack revert(ItemStack book) {
        var tome = createStack();
        Tag.copyMods(book, tome);
        Tag.clear(book);

        book.resetHoverName();

        return tome;
    }

    public static ItemStack attach(ItemStack tome, ItemStack book) {
        var mod = ModName.from(book);
        var modsBooks = Tag.getModsBooks(tome);

        var books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());
        books.add(book);
        modsBooks.put(mod, books);
        
        Tag.setModsBooks(tome, modsBooks);
        return tome;
    }

    private static ItemStack createStack() {
        return Tag.initialize(new ItemStack(EccentricTome.TOME.get()));
    }

    private static void setHoverName(ItemStack book, String name) {
        var bookName = Component.literal(name).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
        book.setHoverName(Component.translatable("eccentrictome.name", bookName));
    }
}
