package website.eccentric.tome;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ClientProxy extends Proxy {

    @Override
    public void tomeScreen(ItemStack tome) {
        Minecraft.getInstance().setScreen(new TomeScreen(tome));
    }

}
