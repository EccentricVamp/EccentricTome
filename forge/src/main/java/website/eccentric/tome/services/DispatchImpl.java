package website.eccentric.tome.services;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import website.eccentric.tome.events.OpenTomeEvent;

public class DispatchImpl implements Dispatch {

    @Override
    public void openTome(ItemStack tome) {
        MinecraftForge.EVENT_BUS.post(new OpenTomeEvent(tome));
    }
}
