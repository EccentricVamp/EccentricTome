package website.eccentric.tome;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

import website.eccentric.tome.util.IModName;

public final class ModName implements IModName {

    private static final Map<String, String> modNames = new HashMap<String, String>();

    public static final String MINECRAFT = "minecraft";
    public static final String PATCHOULI = "patchouli";
    public static final String PATCHOULI_BOOK = PATCHOULI + ":book";

    static {
        for (var mod : ModList.get().getMods()) {
            modNames.put(mod.getModId(), mod.getDisplayName());
        }
    }

    public String from(BlockState state) {
        return orAlias(state.getBlock().getRegistryName().getNamespace());
    }

    public String from(ItemStack stack) {
        if (stack.isEmpty()) return MINECRAFT;

        var mod = stack.getItem().getCreatorModId(stack);
        if (mod.equals(PATCHOULI)) {
            var book = stack.getTag().getString(PATCHOULI_BOOK);
            mod = new ResourceLocation(book).getNamespace();
        }

        return orAlias(mod);
    }

    public String orAlias(String mod) {
        return CommonConfiguration.Cache.ALIASES.getOrDefault(mod, mod);
    }

    public String name(String mod) {
        return modNames.getOrDefault(mod, mod);
    }
    
}
