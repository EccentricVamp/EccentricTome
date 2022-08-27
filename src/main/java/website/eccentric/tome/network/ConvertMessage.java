package website.eccentric.tome.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import website.eccentric.tome.TomeItem;
import website.eccentric.tome.Tome;

public class ConvertMessage {
    public ItemStack book;

    public ConvertMessage(ItemStack book) {
        this.book = book;
    }

    public static ConvertMessage decode(final PacketBuffer buffer) {
        ItemStack book = buffer.readItem();
        return new ConvertMessage(book);
    }

    public static void encode(final ConvertMessage message, final PacketBuffer buffer) {
        buffer.writeItem(message.book);
    }

    public static void handle(final ConvertMessage message, final Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            Hand hand = TomeItem.inHand(player);

            if (hand != null) {
                ItemStack tome = player.getItemInHand(hand);
                player.setItemInHand(hand, Tome.convert(tome, message.book));
            }
    
            context.get().setPacketHandled(true);
        });
    }
}