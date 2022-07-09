package website.eccentric.tome.services;

import net.minecraft.world.item.ItemStack;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.network.ConvertMessage;

public class Network {
    public static void convert(ItemStack book) {
        EccentricTome.CHANNEL.sendToServer(new ConvertMessage(book));
    }
}
