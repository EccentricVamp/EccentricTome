package website.eccentric.tome;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

public class UntransformMessage {

    public static UntransformMessage decode(final FriendlyByteBuf buffer) {
        buffer.readByte();
        return new UntransformMessage();
    }

    public static void encode(final UntransformMessage message, final FriendlyByteBuf buffer) {
        buffer.writeByte(0);
    }
    
    @SuppressWarnings("resource")
    public static void handle(final UntransformMessage message, final Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();            
            var stack = player.getMainHandItem();
            var hand = InteractionHand.MAIN_HAND;

            var hasTome = TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem);
            if (!hasTome) {
                stack = player.getOffhandItem();
                hasTome = TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem);
                hand = InteractionHand.OFF_HAND;
            }

            if (hasTome) {
                player.setItemInHand(hand, TomeItem.revert(stack));
                if (player.level.isClientSide) {
                    Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(hand);
                }
            }

            context.get().setPacketHandled(true);
        });
    }

}