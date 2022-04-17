package website.eccentric.tome;

import java.util.ServiceLoader;

import website.eccentric.tome.util.ModName;

public class Services {
    public static final ModName MOD = load(ModName.class);
    public static final Configuration CONFIGURATION = load(Configuration.class);

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader
            .load(clazz)
            .findFirst()
            .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
