package website.eccentric.tome;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

public class TransformMessage {

	public String mod;

	public TransformMessage(String mod) {
		this.mod = mod;
	}

    public static TransformMessage decode(final FriendlyByteBuf buffer) {
        return new TransformMessage(new String(buffer.readByteArray(), StandardCharsets.UTF_8));
    }

    public static void encode(final TransformMessage message, final FriendlyByteBuf buffer) {
        buffer.writeByteArray(message.mod.getBytes(StandardCharsets.UTF_8));
    }

    public static void handle(final TransformMessage message, final Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();
            if (player == null) return;

            var stack = player.getMainHandItem();
            var hand = InteractionHand.MAIN_HAND;

            var hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
            if (!hasTome) {
                stack = player.getOffhandItem();
                hasTome = !stack.isEmpty() && stack.getItem() instanceof TomeItem;
                hand = InteractionHand.OFF_HAND;
            }

            if (hasTome) {
                var newStack = GetMod.shiftStack(stack, message.mod);
                player.setItemInHand(hand, newStack);
            }
    
            context.get().setPacketHandled(true);
        });
    }

}