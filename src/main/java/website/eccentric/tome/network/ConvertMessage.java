package website.eccentric.tome.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import website.eccentric.tome.TomeItem;
import website.eccentric.tome.services.Services;
import website.eccentric.tome.services.Tome;

public class ConvertMessage {

    public ItemStack book;

    public ConvertMessage(ItemStack book) {
        this.book = book;
    }

    public static ConvertMessage decode(final FriendlyByteBuf buffer) {
        var book = buffer.readItem();
        return new ConvertMessage(book);
    }

    public static void encode(final ConvertMessage message, final FriendlyByteBuf buffer) {
        buffer.writeItem(message.book);
    }

    public static void handle(final ConvertMessage message, final Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();
            var hand = TomeItem.inHand(player);

            if (hand != null) {
                var tome = player.getItemInHand(hand);
                player.setItemInHand(hand, Services.load(Tome.class).convert(tome, message.book));
            }
    
            context.get().setPacketHandled(true);
        });
    }
}