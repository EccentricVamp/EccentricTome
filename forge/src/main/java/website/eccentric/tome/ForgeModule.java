package website.eccentric.tome;

import com.google.inject.AbstractModule;

import website.eccentric.tome.util.ModName;

public class ForgeModule extends AbstractModule {
    protected void configure() {
        bind(Configuration.class).to(ConfigurationImpl.class).asEagerSingleton();
        bind(ModName.class).to(ModNameImpl.class).asEagerSingleton();
        
    }
}
