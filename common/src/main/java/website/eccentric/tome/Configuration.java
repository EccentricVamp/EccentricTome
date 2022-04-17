package website.eccentric.tome;

import java.util.HashMap;
import java.util.List;

public interface Configuration {
        
    public boolean allItems();
    public boolean disableOverlay();
    public List<? extends String> items();
    public List<? extends String> names();
    public HashMap<String, String> aliases();
    public List<? extends String> exclude();
    public List<? extends String> excludeItems();

    // public static void refresh() {
    //     ALL_ITEMS = CommonConfiguration.ALL_ITEMS.get();
    //     DISABLE_OVERLAY = CommonConfiguration.DISABLE_OVERLAY.get();
    //     ITEMS = CommonConfiguration.ITEMS.get();
    //     NAMES = CommonConfiguration.NAMES.get();

    //     ALIASES = new HashMap<String, String>();
    //     for (var alias : CommonConfiguration.ALIASES.get()) {
    //         var tokens = alias.split("=");
    //         ALIASES.put(tokens[0], tokens[1]);
    //     }

    //     EXCLUDE = CommonConfiguration.EXCLUDE.get();
    //     EXCLUDE_ITEMS = CommonConfiguration.EXCLUDE_ITEMS.get();
    // }
}
