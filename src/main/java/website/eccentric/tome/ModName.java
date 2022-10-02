package website.eccentric.tome;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
        ResourceLocation location = state.getBlock().getRegistryName();
        if (location == null)
            return MINECRAFT;

        return orAlias(location.getNamespace());
    }

    public static String from(ItemStack stack) {
        if (stack.isEmpty())
            return MINECRAFT;

        String mod = stack.getItem().getCreatorModId(stack);
        if (mod == null)
            return MINECRAFT;

        if (mod.equals(PATCHOULI)) {
            CompoundNBT tag = stack.getTag();
            if (tag == null)
                return PATCHOULI;

            String book = tag.getString(Tag.Patchouli.BOOK);
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
