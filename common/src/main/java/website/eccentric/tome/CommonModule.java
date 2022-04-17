package website.eccentric.tome;

import com.google.inject.AbstractModule;

public class CommonModule extends AbstractModule {
    protected void configure() {
        bind(TomeItem.class).asEagerSingleton();
        bind(AttachmentSerializer.class).asEagerSingleton();
    }
}