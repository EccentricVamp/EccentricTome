package website.eccentric.tome;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundNBT;

public class Migration {
    public static final int CURRENT_VERSION = 1;

    public static void apply(CompoundNBT tag) {
        while (getVersion(tag) < CURRENT_VERSION) {
            int version = getVersion(tag) + 1;
            steps.get(version).accept(tag);
            setVersion(tag, version);
        }
    }

    public static int getVersion(CompoundNBT tag) {
        return tag.contains(Tag.VERSION) ? tag.getInt(Tag.VERSION) : 0;
    }

    public static void setVersion(CompoundNBT tag, int version) {
        tag.putInt(Tag.VERSION, version);
    }

    public static void setCurrentVersion(CompoundNBT tag) {
        tag.putInt(Tag.VERSION, CURRENT_VERSION);
    }

    public static Map<Integer, Consumer<CompoundNBT>> steps;
    static {
    steps = new HashMap<>();
    steps.put(
        Integer.valueOf(1), Migration::one
    );
    }

    public static void one(CompoundNBT tag) {
        // Remove unused tags
        tag.remove(Tag.key("name"));
        tag.remove(Tag.key("mod"));

        // Get old mods (books) tag
        String books = Tag.key("books");
        CompoundNBT oldMods = tag.getCompound(books);
        if (oldMods == null) oldMods = new CompoundNBT();
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
