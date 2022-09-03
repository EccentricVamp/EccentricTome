package website.eccentric.tome;

import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundTag;

public class Migration {
    public static final int CURRENT_VERSION = 1;

    public static void apply(CompoundTag tag) {
        while (getVersion(tag) < CURRENT_VERSION) {
            var version = getVersion(tag) + 1;
            steps.get(version).accept(tag);
            setVersion(tag, version);
        }
    }

    public static int getVersion(CompoundTag tag) {
        return tag.contains(Tag.VERSION) ? tag.getInt(Tag.VERSION) : 0;
    }

    public static void setVersion(CompoundTag tag, int version) {
        tag.putInt(Tag.VERSION, version);
    }

    public static void setCurrentVersion(CompoundTag tag) {
        tag.putInt(Tag.VERSION, CURRENT_VERSION);
    }

    public static Map<Integer, Consumer<CompoundTag>> steps = Map.of(
        Integer.valueOf(1), Migration::one
    );

    public static void one(CompoundTag tag) {
        // Remove unused tags
        tag.remove(Tag.key("name"));
        tag.remove(Tag.key("mod"));

        // Get old mods (books) tag
        var books = Tag.key("books");
        var oldMods = tag.getCompound(books);
        if (oldMods == null) oldMods = new CompoundTag();
        tag.remove(books);

        // Convert to new format and preserve existing books
        var mods = new CompoundTag();
        for (var modName : oldMods.getAllKeys()) {
            var modTag = new CompoundTag();
            modTag.put(Integer.toString(0), oldMods.get(modName));
            mods.put(modName, modTag);
        }
        tag.put(Tag.MODS, mods);
    }
}
