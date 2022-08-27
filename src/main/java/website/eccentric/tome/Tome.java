package website.eccentric.tome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class Tome {
    public static ItemStack convert(ItemStack tome, ItemStack book) {
        Map<String, List<ItemStack>> modsBooks = Tag.getModsBooks(tome);
        String mod = ModName.from(book);
        List<ItemStack> books = modsBooks.get(mod);
        ResourceLocation registry = book.getItem().getRegistryName();
        books = books.stream().filter(b -> !b.getItem().getRegistryName().equals(registry)).collect(Collectors.toList());
        modsBooks.put(mod, books);
        Tag.setModsBooks(tome, modsBooks);

        String name = book.getHoverName().getString();
        Tag.copyMods(tome, book);
        Tag.fill(book, true);

        setHoverName(book, name);
        
        return book;
    }

    public static ItemStack revert(ItemStack book) {
        ItemStack tome = createStack();
        Tag.copyMods(book, tome);
        Tag.clear(book);

        book.resetHoverName();

        return tome;
    }

    public static ItemStack attach(ItemStack tome, ItemStack book) {
        String mod = ModName.from(book);
        Map<String, List<ItemStack>> modsBooks = Tag.getModsBooks(tome);

        List<ItemStack> books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());
        books.add(book);
        modsBooks.put(mod, books);
        
        Tag.setModsBooks(tome, modsBooks);
        return tome;
    }

    private static ItemStack createStack() {
        return Tag.initialize(new ItemStack(EccentricTome.TOME.get()));
    }

    private static void setHoverName(ItemStack book, String name) {
        IFormattableTextComponent bookName = new StringTextComponent(name).setStyle(Style.EMPTY.applyFormats(TextFormatting.GREEN));
        book.setHoverName(new TranslationTextComponent("eccentrictome.name", bookName));
    }
}
