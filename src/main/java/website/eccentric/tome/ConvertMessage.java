package website.eccentric.tome;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

public class ConvertMessage {

    public String mod;

    public ConvertMessage(String mod) {
        this.mod = mod;
    }

    public static ConvertMessage decode(final FriendlyByteBuf buffer) {
        return new ConvertMessage(new String(buffer.readByteArray(), StandardCharsets.UTF_8));
    }

    public static void encode(final ConvertMessage message, final FriendlyByteBuf buffer) {
        buffer.writeByteArray(message.mod.getBytes(StandardCharsets.UTF_8));
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
                player.setItemInHand(hand, TomeItem.convert(stack, message.mod));
            }
    
            context.get().setPacketHandled(true);
        });
    }

}