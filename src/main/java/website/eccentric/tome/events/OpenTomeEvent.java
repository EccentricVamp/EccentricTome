package website.eccentric.tome.events;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class OpenTomeEvent extends Event {
    public final ItemStack tome;

    public OpenTomeEvent(ItemStack tome) {
        this.tome = tome;
    }
}
