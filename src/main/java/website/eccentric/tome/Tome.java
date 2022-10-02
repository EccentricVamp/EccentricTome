package website.eccentric.tome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public class Tome {
    public static ItemStack convert(ItemStack tome, ItemStack book) {
        var modsBooks = getModsBooks(tome);
        var mod = ModName.from(book);
        var books = modsBooks.get(mod);
        var registry = book.getItem().getRegistryName();
        books.removeIf(b -> {
            var location = b.getItem().getRegistryName();
            if (location == null)
                return false;

            return location.equals(registry);
        });

        setModsBooks(book, modsBooks);
        Migration.setVersion(book);
        book.getOrCreateTag().putBoolean(Tag.IS_TOME, true);
        setHoverName(book);

        return book;
    }

    public static ItemStack revert(ItemStack book) {
        Migration.apply(book);

        var tome = new ItemStack(EccentricTome.TOME.get());
        copyMods(book, tome);
        Migration.setVersion(tome);
        clear(book);

        return tome;
    }

    public static ItemStack attach(ItemStack tome, ItemStack book) {
        var mod = ModName.from(book);
        var modsBooks = getModsBooks(tome);

        var books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());
        books.add(book);
        modsBooks.put(mod, books);

        setModsBooks(tome, modsBooks);
        return tome;
    }

    public static Map<String, List<ItemStack>> getModsBooks(ItemStack stack) {
        return Tag.deserialize(stack.getOrCreateTagElement(Tag.MODS));
    }

    public static void setModsBooks(ItemStack stack, Map<String, List<ItemStack>> modsBooks) {
        stack.getOrCreateTag().put(Tag.MODS, Tag.serialize(modsBooks));
    }

    public static boolean isTome(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        else if (stack.getItem() instanceof TomeItem)
            return true;
        else {
            var tag = stack.getTag();
            if (tag == null)
                return false;

            return tag.getBoolean(Tag.IS_TOME);
        }
    }

    @Nullable
    public static InteractionHand inHand(Player player) {
        var hand = InteractionHand.MAIN_HAND;
        var stack = player.getItemInHand(hand);
        if (isTome(stack))
            return hand;

        hand = InteractionHand.OFF_HAND;
        stack = player.getItemInHand(hand);
        if (isTome(stack))
            return hand;

        return null;
    }

    private static void copyMods(ItemStack source, ItemStack target) {
        var tag = target.getOrCreateTag();
        tag.put(Tag.MODS, source.getTagElement(Tag.MODS));
    }

    private static void clear(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null)
            return;

        tag.remove(Tag.MODS);
        tag.remove(Tag.IS_TOME);
        tag.remove(Tag.VERSION);
        stack.resetHoverName();
    }

    private static void setHoverName(ItemStack book) {
        var name = book.getHoverName().copy().withStyle(ChatFormatting.GREEN);
        book.setHoverName(new TranslatableComponent("eccentrictome.name", name));
    }
}
