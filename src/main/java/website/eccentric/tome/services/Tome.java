package website.eccentric.tome.services;

import java.util.ArrayList;
import java.util.stream.Collectors;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.Tag;

public class TomeImpl implements Tome {

    public ItemStack convert(ItemStack tome, ItemStack book) {
        var modsBooks = Tag.getModsBooks(tome);
        var mod = Services.load(ModName.class).from(book);
        var books = modsBooks.get(mod);
        var registry = book.getItem().getRegistryName();
        books = books.stream().filter(b -> !b.getItem().getRegistryName().equals(registry)).collect(Collectors.toList());
        modsBooks.put(mod, books);
        Tag.setModsBooks(tome, modsBooks);

        var name = book.getHoverName().getString();
        Tag.copyMods(tome, book);
        Tag.fill(book, true);

        setHoverName(book, name);
        
        return book;
    }

    public ItemStack revert(ItemStack book) {
        var tome = createStack();
        Tag.copyMods(book, tome);
        Tag.clear(book);

        book.resetHoverName();

        return tome;
    }

    public ItemStack attach(ItemStack tome, ItemStack book) {
        var mod = Services.load(ModName.class).from(book);
        var modsBooks = Tag.getModsBooks(tome);

        var books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());
        books.add(book);
        modsBooks.put(mod, books);
        
        Tag.setModsBooks(tome, modsBooks);
        return tome;
    }

    private ItemStack createStack() {
        return Tag.initialize(new ItemStack(EccentricTome.TOME.get()));
    }

    private void setHoverName(ItemStack book, String name) {
        var bookName = new TextComponent(name).setStyle(Style.EMPTY.applyFormats(ChatFormatting.GREEN));
        book.setHoverName(new TranslatableComponent("eccentrictome.name", bookName));
    }
}
