package website.eccentric.tome;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfiguration {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ALL_ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NAMES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALIASES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUDE;

    static {
        BUILDER.comment("Common configuration settings").push("common");

        ALL_ITEMS = BUILDER
            .comment("Allow all items to be added")
            .define(
                "allitems",
                false
            );

        ITEMS = BUILDER
            .comment("Whitelisted items")
            .defineList(
                "items",
                List.of(
                    "roots:runedtablet",
                    "opencomputers:tool:4",
                    "immersiveengineering:tool:3",
                    "integrateddynamics:on_the_dynamics_of_integration",
                    "theoneprobe:probenote",
                    "evilcraft:origins_of_darkness",
                    "draconicevolution:info_tablet",
                    "charset:tablet",
                    "antiqueatlas:antique_atlas",
                    "occultism:dictionary_of_spirits"
                ),
                Validator::isStringResource
            );

        NAMES = BUILDER
            .comment("Whitelisted names")
            .defineList(
                "names",
                List.of(
                    "book",
                    "tome",
                    "lexicon",
                    "nomicon",
                    "manual",
                    "knowledge",
                    "pedia",
                    "compendium",
                    "guide",
                    "codex",
                    "journal"
                ),
                Validator::isString
            );

        

        ALIASES = BUILDER
            .comment("Mod aliases")
            .defineList(
                "aliases",
                List.of(
                    "nautralpledge=botania",
                    "thermalexpansion=thermalfoundation",
                    "thermaldynamics=thermalfoundation",
                    "thermalcultivation=thermalfoundation",
                    "redstonearsenal=thermalfoundation",
                    "rftoolsdim=rftools",
                    "rftoolspower=rftools",
                    "rftoolscontrol=rftools",
                    "deepresonance=rftools",
                    "xnet=rftools",
                    "ae2stuff=appliedenergistics2",
                    "animus=bloodmagic",
                    "integrateddynamics=integratedtunnels",
                    "mekanismgenerators=mekanism",
                    "mekanismtools=mekanism"
                ),
                Validator::isString
            );

        EXCLUDE = BUILDER
            .comment("Blacklisted mods")
            .defineList(
                "exclude",
                Lists.newArrayList(),
                Validator::isString
            );

        BUILDER.pop();
    }

    public static class Validator {

        public static boolean isString(Object object) {
            return object instanceof String;
        }

        public static boolean isStringResource(Object object) {
            return isString(object) && ResourceLocation.isValidResourceLocation((String) object);
        }

    }

    public class Cache {
        public static boolean ALL_ITEMS;
        public static List<? extends String> ITEMS;
        public static List<? extends String> NAMES;
        public static HashMap<String, String> ALIASES;
        public static List<? extends String> EXCLUDE;

        public static void Refresh() {
            ALL_ITEMS = CommonConfiguration.ALL_ITEMS.get();
            ITEMS = CommonConfiguration.ITEMS.get();
            NAMES = CommonConfiguration.NAMES.get();

            ALIASES = new HashMap<String, String>();
            for (var alias : CommonConfiguration.ALIASES.get()) {
                var tokens = alias.split("=");
                ALIASES.put(tokens[0], tokens[1]);
            }

            EXCLUDE = CommonConfiguration.EXCLUDE.get();
        }
    }
}