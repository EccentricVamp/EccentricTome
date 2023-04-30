package website.eccentric.tome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class Configuration {
    public static final ForgeConfigSpec.BooleanValue ALL_ITEMS;
    public static final ForgeConfigSpec.BooleanValue DISABLE_OVERLAY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> NAMES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALIASES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUDE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUDE_ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> INCLUDE_ITEM_TAGS;

    public static final HashMap<String, String> ALIAS_MAP = new HashMap<>();

    public static final ForgeConfigSpec SPEC;

    static {
        Builder BUILDER = new ForgeConfigSpec.Builder()
                .comment("Common configuration settings")
                .push("common");

        ALL_ITEMS = BUILDER
                .comment("Allow all items to be added")
                .define("allitems", false);

        DISABLE_OVERLAY = BUILDER
                .comment("Disable overlay previewing tome conversion")
                .define("disable_overlay", false);

        ITEMS = BUILDER
                .comment("Whitelisted items")
                .defineListAllowEmpty(
                        Lists.newArrayList("items"),
                        () -> Lists.newArrayList(
                                "tconstruct:materials_and_you",
                                "tconstruct:puny_smelting",
                                "tconstruct:mighty_smelting",
                                "tconstruct:fantastic_foundry",
                                "tconstruct:tinkers_gadgetry",
                                "integrateddynamics:on_the_dynamics_of_integration",
                                "evilcraft:origins_of_darkness",
                                "cookingforblockheads:no_filter_edition",
                                "alexsmobs:animal_dictionary",
                                "occultism:dictionary_of_spirits",
                                "theoneprobe:probenote",
                                "compactmachines:personal_shrinking_device",
                                "draconicevolution:info_tablet",
                                "iceandfire:bestiary",
                                "rootsclassic:runic_tablet",
                                "enigmaticlegacy:the_acknowledgment"),
                        Validator::isString);

        NAMES = BUILDER
                .comment("Whitelisted names")
                .defineListAllowEmpty(
                        Lists.newArrayList("names"),
                        () -> Lists.newArrayList(
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
                                "enchiridion",
                                "grimoire"),
                        Validator::isString);

        ALIASES = BUILDER
                .comment("Mod aliases")
                .defineListAllowEmpty(
                        Lists.newArrayList("aliases"),
                        () -> Lists.newArrayList(
                                "mythicbotany=botania",
                                "integratedtunnels=integrateddynamics",
                                "integratedterminals=integrateddynamics",
                                "integratedcrafting=integrateddynamics",
                                "rftoolsbuilder=rftoolsbase",
                                "rftoolscontrol=rftoolsbase",
                                "rftoolsdim=rftoolsbase",
                                "rftoolspower=rftoolsbase",
                                "rftoolsstorage=rftoolsbase",
                                "rftoolsutility=rftoolsbase",
                                "rftoolspower=rftoolsbase",
                                "deepresonance=rftoolsbase",
                                "xnet=rftoolsbase",
                                "mysticalaggraditions=mysticalagriculture"),
                        Validator::isString);

        EXCLUDE = BUILDER
                .comment("Blacklisted mods")
                .defineListAllowEmpty(
                        Lists.newArrayList("exclude"),
                        ArrayList<String>::new,
                        Validator::isString);

        EXCLUDE_ITEMS = BUILDER
                .comment("Blacklisted items")
                .defineListAllowEmpty(
                        Lists.newArrayList("exclude_items"),
                        () -> Lists.newArrayList(
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
                                "ars_nouveau:annotated_codex",
                                "blue_skies:blue_journal",
                                "darkutils:book_galactic",
                                "darkutils:book_runelic",
                                "darkutils:book_restore",
                                "darkutils:tome_enchanting",
                                "darkutils:tome_illager",
                                "darkutils:tome_pigpen",
                                "darkutils:tome_runelic",
                                "darkutils:tome_sga",
                                "darkutils:tome_shadows",
                                "minecolonies:ancienttome",
                                "minecraft:enchanted_book",
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
                                "occultism:book_of_calling_foliot_transport_items",
                                "projecte:tome",
                                "quark:ancient_tome",
                                "tombstone:book_of_disenchantment",
                                "tombstone:book_of_recycling",
                                "tombstone:book_of_repairing",
                                "tombstone:book_of_magic_impregnation"),
                        Validator::isString);

        INCLUDE_ITEM_TAGS = BUILDER
                .comment("Whitelisted item tags")
                .defineListAllowEmpty(
                        Lists.newArrayList("include_item_tags"),
                        ArrayList<String>::new,
                        Validator::isString);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static class Validator {

        public static boolean isString(Object object) {
            return object instanceof String;
        }
    }
}
