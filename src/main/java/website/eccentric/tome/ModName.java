package website.eccentric.tome;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

public class ModName {
    private static final Map<String, String> modNames = new HashMap<>();

    public static final String MINECRAFT = "minecraft";
    public static final String PATCHOULI = "patchouli";

    static {
        for (ModInfo mod : ModList.get().getMods()) {
            modNames.put(mod.getModId(), mod.getDisplayName());
        }
    }

    public static String from(BlockState state) {
        return orAlias(state.getBlock().getRegistryName().getNamespace());
    }

    public static String from(ItemStack stack) {
        String minecraft = "minecraft";
        String patchouli = "patchouli";
        String patchouliBook = patchouli + ":book";

        if (stack.isEmpty()) return minecraft;

        String mod = stack.getItem().getCreatorModId(stack);
        if (mod.equals(PATCHOULI)) {
            String book = stack.getTag().getString(Tag.Patchouli.BOOK);
            mod = new ResourceLocation(book).getNamespace();
        }

        return orAlias(mod);
    }

    public static String orAlias(String mod) {
        return Configuration.ALIAS_MAP.getOrDefault(mod, mod);
    }

    public static String name(String mod) {
        return modNames.getOrDefault(mod, mod);
    }
}
