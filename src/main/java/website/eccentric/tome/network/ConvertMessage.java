package website.eccentric.tome.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import website.eccentric.tome.TomeItem;

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
            var stack = player.getMainHandItem();
            var hand = InteractionHand.MAIN_HAND;

            var hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
            if (!hasTome) {
                stack = player.getOffhandItem();
                hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
                hand = InteractionHand.OFF_HAND;
            }

            if (hasTome) {
                player.setItemInHand(hand, TomeItem.convert(stack, message.book));
            }
    
            context.get().setPacketHandled(true);
        });
    }

}