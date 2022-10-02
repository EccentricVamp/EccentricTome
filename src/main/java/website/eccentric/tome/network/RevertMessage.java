package website.eccentric.tome.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.Tome;

public class RevertMessage {
    public static RevertMessage decode(final FriendlyByteBuf buffer) {
        buffer.readByte();
        return new RevertMessage();
    }

    public static void encode(final RevertMessage message, final FriendlyByteBuf buffer) {
        buffer.writeByte(0);
    }

    @SuppressWarnings("resource")
    public static void handle(final RevertMessage message, final Supplier<NetworkEvent.Context> context) {
        EccentricTome.LOGGER.debug("Received revert message.");

        context.get().enqueueWork(() -> {
            var player = context.get().getSender();
            var hand = Tome.inHand(player);

            if (hand != null) {
                var stack = player.getItemInHand(hand);
                var tome = Tome.revert(stack);
                player.setItemInHand(hand, Tome.attach(tome, stack));

                if (player.level.isClientSide) {
                    Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(hand);
                }
            }

            context.get().setPacketHandled(true);
        });
    }
}