package website.eccentric.tome.services;

import net.minecraft.world.item.ItemStack;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.network.ConvertMessage;

public class NetworkImpl implements Network {

    @Override
    public void convert(ItemStack book) {
        EccentricTome.CHANNEL.sendToServer(new ConvertMessage(book));
    }
}
