package website.eccentric.tome;

import com.google.inject.AbstractModule;

public class ForgeModule extends AbstractModule {
    protected void configure() {
        bind(Configuration.class).to(ConfigurationImpl.class);
    }
}
