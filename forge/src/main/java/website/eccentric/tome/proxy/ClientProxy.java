package website.eccentric.tome.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import website.eccentric.tome.TomeScreen;

public class ClientProxy extends Proxy {

    @Override
    public void tomeScreen(ItemStack tome) {
        Minecraft.getInstance().setScreen(new TomeScreen(tome));
    }
}
