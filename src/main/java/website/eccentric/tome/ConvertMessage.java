package website.eccentric.tome;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

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
            ItemStack stack = player.getMainHandItem();
            Hand hand = Hand.MAIN_HAND;

            boolean hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
            if (!hasTome) {
                stack = player.getOffhandItem();
                hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
                hand = Hand.OFF_HAND;
            }

            if (hasTome) {
                player.setItemInHand(hand, TomeItem.convert(stack, message.book));
            }
    
            context.get().setPacketHandled(true);
        });
    }

}