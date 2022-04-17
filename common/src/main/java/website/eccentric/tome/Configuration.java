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
}
