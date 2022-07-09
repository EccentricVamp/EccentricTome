package website.eccentric.tome.services;

import java.util.HashMap;
import java.util.List;

import website.eccentric.tome.CommonConfiguration;

public class ConfigurationImpl implements Configuration {

    private static boolean allItems;
    private static boolean disableOverlay;
    private static List<? extends String> items;
    private static List<? extends String> names;
    private static HashMap<String, String> aliases;
    private static List<? extends String> exclude;
    private static List<? extends String> excludeItems;
        
    public boolean allItems() {
        return allItems;
    }

    public boolean disableOverlay() {
        return disableOverlay;
    }

    public List<? extends String> items() {
        return items;
    }

    public List<? extends String> names() {
        return names;
    }

    public HashMap<String, String> aliases() {
        return aliases;
    }

    public List<? extends String> exclude() {
        return exclude;
    }

    public List<? extends String> excludeItems() {
        return excludeItems;
    }
    

    public void refresh() {
        allItems = CommonConfiguration.ALL_ITEMS.get();
        disableOverlay = CommonConfiguration.DISABLE_OVERLAY.get();
        items = CommonConfiguration.ITEMS.get();
        names = CommonConfiguration.NAMES.get();

        aliases = new HashMap<String, String>();
        for (var alias : CommonConfiguration.ALIASES.get()) {
            var tokens = alias.split("=");
            aliases.put(tokens[0], tokens[1]);
        }

        exclude = CommonConfiguration.EXCLUDE.get();
        excludeItems = CommonConfiguration.EXCLUDE_ITEMS.get();
    }
}
