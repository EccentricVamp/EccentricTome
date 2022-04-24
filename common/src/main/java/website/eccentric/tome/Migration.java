package website.eccentric.tome;

import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundTag;

public class Migration {

    public static final String VERSION = "eccentrictome:version";
    public static final int CURRENT_VERSION = 1;

    public static void Apply(CompoundTag tag) {
        while (getVersion(tag) < CURRENT_VERSION) {
            var version = getVersion(tag) + 1;
            Steps.get(version).accept(tag);
            setVersion(tag, version);
        }
    }

    public static int getVersion(CompoundTag tag) {
        return tag.contains(VERSION) ? tag.getInt(VERSION) : 0;
    }

    public static void setVersion(CompoundTag tag, int version) {
        tag.putInt(VERSION, version);
    }

    public static void setCurrentVersion(CompoundTag tag) {
        tag.putInt(VERSION, CURRENT_VERSION);
    }

    public static Map<Integer, Consumer<CompoundTag>> Steps = Map.of(
        Integer.valueOf(1), Migration::One
    );

    public static void One(CompoundTag tag) {
        // Remove unused tags
        tag.remove("eccentrictome:name");
        tag.remove("eccentrictome:mod");

        // Get old mods (books) tag
        var books = "eccentrictome:books";
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
        tag.put("eccentrictome:mods", mods);
    }
}
