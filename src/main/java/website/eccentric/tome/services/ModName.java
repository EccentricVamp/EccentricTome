package website.eccentric.tome.services;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

public class ModName {
    private static final Map<String, String> modNames = new HashMap<>();

    static {
        for (var mod : ModList.get().getMods()) {
            modNames.put(mod.getModId(), mod.getDisplayName());
        }
    }

    public static String from(BlockState state) {
        return orAlias(state.getBlock().getRegistryName().getNamespace());
    }

    public static String from(ItemStack stack) {
        var minecraft = "minecraft";
        var patchouli = "patchouli";
        var patchouliBook = patchouli + ":book";

        if (stack.isEmpty()) return minecraft;

        var mod = stack.getItem().getCreatorModId(stack);
        if (mod.equals(patchouli)) {
            var book = stack.getTag().getString(patchouliBook);
            mod = new ResourceLocation(book).getNamespace();
        }

        return orAlias(mod);
    }

    public static String orAlias(String mod) {
        return Configuration.aliases().getOrDefault(mod, mod);
    }

    public static String name(String mod) {
        return modNames.getOrDefault(mod, mod);
    }
}
