package website.eccentric.tome.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class OpenTomeEvent extends Event {
    public final ItemStack tome;

    public OpenTomeEvent(ItemStack tome) {
        this.tome = tome;
    }
}
