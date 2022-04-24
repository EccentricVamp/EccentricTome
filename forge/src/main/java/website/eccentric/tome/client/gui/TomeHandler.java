package website.eccentric.tome.client.gui;

import net.minecraft.client.Minecraft;
import website.eccentric.tome.TomeScreen;
import website.eccentric.tome.events.OpenTomeEvent;

public class TomeHandler {
    
    public static void onOpenTome(OpenTomeEvent event) {
        Minecraft.getInstance().setScreen(new TomeScreen(event.tome));
    }
}
