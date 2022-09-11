package website.eccentric.tome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class Tome {
    public static ItemStack convert(ItemStack tome, ItemStack book) {
        EccentricTome.LOGGER.debug("Converting. Tag: " + tome.getTag().toString());

        Map<String, List<ItemStack>> modsBooks = getModsBooks(tome);
        String mod = ModName.from(book);
        List<ItemStack> books = modsBooks.get(mod);
        ResourceLocation registry = book.getItem().getRegistryName();
        books.removeIf(b -> b.getItem().getRegistryName().equals(registry));

        setModsBooks(book, modsBooks);
        Migration.setVersion(book);
        book.getTag().putBoolean(Tag.IS_TOME, true);
        setHoverName(book);
        
        return book;
    }

    public static ItemStack revert(ItemStack book) {
        EccentricTome.LOGGER.debug("Reverting. Tag: " + book.getTag().toString());

        Migration.apply(book);

        ItemStack tome = new ItemStack(EccentricTome.TOME.get());
        copyMods(book, tome);
        Migration.setVersion(tome);
        clear(book);

        return tome;
    }

    public static ItemStack attach(ItemStack tome, ItemStack book) {
        String mod = ModName.from(book);
        Map<String, List<ItemStack>> modsBooks = getModsBooks(tome);

        List<ItemStack> books = modsBooks.getOrDefault(mod, new ArrayList<ItemStack>());
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
        if (stack.isEmpty()) return false;
        else if (stack.getItem() instanceof TomeItem) return true;
        else return stack.hasTag() && stack.getTag().getBoolean(Tag.IS_TOME);
    }

    public static Hand inHand(PlayerEntity player) {
        Hand hand = Hand.MAIN_HAND;
        ItemStack stack = player.getItemInHand(hand);
        if (isTome(stack)) return hand;
        
        hand = Hand.OFF_HAND;
        stack = player.getItemInHand(hand);
        if (isTome(stack)) return hand;

        return null;
    }

    private static void copyMods(ItemStack source, ItemStack target) {
        CompoundNBT targetTag = target.getOrCreateTag();
        targetTag.put(Tag.MODS, source.getTagElement(Tag.MODS));
    }

    private static void clear(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        tag.remove(Tag.MODS);
        tag.remove(Tag.IS_TOME);
        tag.remove(Tag.VERSION);
        stack.resetHoverName();
    }

    private static void setHoverName(ItemStack book) {
        IFormattableTextComponent name = book.getHoverName().copy().withStyle(TextFormatting.GREEN);
        book.setHoverName(new TranslationTextComponent("eccentrictome.name", name));
    }
}
