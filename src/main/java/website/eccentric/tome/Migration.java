package website.eccentric.tome;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.ItemStack;

public class Migration {
    public static final int CURRENT_VERSION = 1;

    public static void apply(ItemStack stack) {
        while (getVersion(stack) < CURRENT_VERSION) {
            int version = getVersion(stack) + 1;
            steps.get(version).accept(stack);
            setVersion(stack, version);
        }
    }

    public static int getVersion(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag == null)
            return 0;

        if (!tag.contains(Tag.VERSION))
            return 0;

        return tag.getInt(Tag.VERSION);
    }

    public static void setVersion(ItemStack stack, int version) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt(Tag.VERSION, version);
    }

    public static void setVersion(ItemStack stack) {
        setVersion(stack, CURRENT_VERSION);
    }

    public static Map<Integer, Consumer<ItemStack>> steps;
    static {
        steps = new HashMap<>();
        steps.put(
                Integer.valueOf(1), Migration::one);
    }

    public static void one(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();

        // Remove unused tags
        tag.remove(Tag.key("name"));
        tag.remove(Tag.key("mod"));

        // Get old mods (books) tag
        String books = Tag.key("books");
        CompoundNBT oldMods = tag.getCompound(books);
        if (oldMods == null)
            oldMods = new CompoundNBT();
        tag.remove(books);

        // Convert to new format and preserve existing books
        CompoundNBT mods = new CompoundNBT();
        for (String modName : oldMods.getAllKeys()) {
            CompoundNBT modTag = new CompoundNBT();
            modTag.put(Integer.toString(0), oldMods.get(modName));
            mods.put(modName, modTag);
        }
        tag.put(Tag.MODS, mods);
    }
}
