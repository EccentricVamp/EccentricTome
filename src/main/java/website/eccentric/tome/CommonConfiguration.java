package website.eccentric.tome;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfiguration {

    public static final ForgeConfigSpec.BooleanValue ALL_ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NAMES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALIASES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUDE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUDE_ITEMS;

    public static final ForgeConfigSpec SPEC;

    static {
        var BUILDER = new ForgeConfigSpec.Builder()
            .comment("Common configuration settings")
            .push("common");

        ALL_ITEMS = BUILDER
            .comment("Allow all items to be added")
            .define("allitems", false);

        ITEMS = BUILDER
            .comment("Whitelisted items")
            .defineListAllowEmpty(
                List.of("items"),
                () -> List.of(
                    "tconstruct:materials_and_you",
                    "tconstruct:puny_smelting",
                    "tconstruct:mighty_smelting",
                    "tconstruct:fantastic_foundry",
                    "tconstruct:tinkers_gadgetry",
                    "integrateddynamics:on_the_dynamics_of_integration",
                    "cookingforblockheads:no_filter_edition",
                    "alexsmobs:animal_dictionary",
                    "occultism:dictionary_of_spirits",
                    "theoneprobe:probenote",
                    "compactmachines:personal_shrinking_device"
                ),
                Validator::isStringResource
            );

        NAMES = BUILDER
            .comment("Whitelisted names")
            .defineListAllowEmpty(
                List.of("names"),
                () -> List.of(
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
                    "journal",
                    "enchiridion"
                ),
                Validator::isString
            );

        ALIASES = BUILDER
            .comment("Mod aliases")
            .defineListAllowEmpty(
                List.of("aliases"),
                () -> List.of(
                    "thermalexpansion=thermalfoundation",
                    "thermaldynamics=thermalfoundation",
                    "thermalcultivation=thermalfoundation",
                    "redstonearsenal=thermalfoundation",
                    "rftoolsdim=rftools",
                    "rftoolspower=rftools",
                    "rftoolscontrol=rftools",
                    "xnet=rftools"
                ),
                Validator::isString
            );

        EXCLUDE = BUILDER
            .comment("Blacklisted mods")
            .defineListAllowEmpty(
                List.of("exclude"),
                () -> Lists.newArrayList(),
                Validator::isString
            );

        EXCLUDE_ITEMS = BUILDER
            .comment("Blacklisted items")
            .defineListAllowEmpty(
                List.of("exclude_items"),
                () -> List.of(
                    "apotheosis:boots_tome",
                    "apotheosis:bow_tome",
                    "apotheosis:chestplate_tome",
                    "apotheosis:fishing_tome",
                    "apotheosis:helmet_tome",
                    "apotheosis:leggings_tome",
                    "apotheosis:other_tome",
                    "apotheosis:pickaxe_tome",
                    "apotheosis:scrap_tome",
                    "apotheosis:weapon_tome",
                    "darkutils:tome_enchanting",
                    "darkutils:tome_illager",
                    "darkutils:tome_pigpen",
                    "darkutils:tome_runelic",
                    "darkutils:tome_sga",
                    "darkutils:tome_shadows",
                    "minecolonies:ancienttome",
                    "occultism:book_of_binding_afrit",
                    "occultism:book_of_binding_bound_afrit",
                    "occultism:book_of_binding_bound_djinni",
                    "occultism:book_of_binding_bound_foliot",
                    "occultism:book_of_binding_bound_marid",
                    "occultism:book_of_binding_djinni",
                    "occultism:book_of_binding_foliot",
                    "occultism:book_of_binding_marid",
                    "occultism:book_of_calling_djinni_manage_machine",
                    "occultism:book_of_calling_foliot_cleaner",
                    "occultism:book_of_calling_foliot_lumberjack",
                    "occultism:book_of_calling_foliot_transport_items"
                ),
                Validator::isStringResource
            );

        BUILDER.pop();

        SPEC = BUILDER.build();
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
        public static List<? extends String> EXCLUDE_ITEMS;

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
            EXCLUDE_ITEMS = CommonConfiguration.EXCLUDE_ITEMS.get();
        }
        
    }
    
}