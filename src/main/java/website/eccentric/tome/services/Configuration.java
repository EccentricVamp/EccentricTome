package website.eccentric.tome.services;

import java.util.HashMap;
import java.util.List;

import website.eccentric.tome.CommonConfiguration;

public class Configuration {
    private static boolean allItems;
    private static boolean disableOverlay;
    private static List<? extends String> items;
    private static List<? extends String> names;
    private static HashMap<String, String> aliases;
    private static List<? extends String> exclude;
    private static List<? extends String> excludeItems;
        
    public static boolean allItems() {
        return allItems;
    }

    public static boolean disableOverlay() {
        return disableOverlay;
    }

    public static List<? extends String> items() {
        return items;
    }

    public static List<? extends String> names() {
        return names;
    }

    public static HashMap<String, String> aliases() {
        return aliases;
    }

    public static List<? extends String> exclude() {
        return exclude;
    }

    public static List<? extends String> excludeItems() {
        return excludeItems;
    }

    public static void refresh() {
        allItems = CommonConfiguration.ALL_ITEMS.get();
        disableOverlay = CommonConfiguration.DISABLE_OVERLAY.get();
        items = CommonConfiguration.ITEMS.get();
        names = CommonConfiguration.NAMES.get();

        aliases = new HashMap<String, String>();
        for (String alias : CommonConfiguration.ALIASES.get()) {
            String[] tokens = alias.split("=");
            aliases.put(tokens[0], tokens[1]);
        }

        exclude = CommonConfiguration.EXCLUDE.get();
        excludeItems = CommonConfiguration.EXCLUDE_ITEMS.get();
    }
}
