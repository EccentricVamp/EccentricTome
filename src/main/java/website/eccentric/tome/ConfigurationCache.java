package website.eccentric.tome;

import java.util.HashMap;
import java.util.List;

public class ConfigurationCache {
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
