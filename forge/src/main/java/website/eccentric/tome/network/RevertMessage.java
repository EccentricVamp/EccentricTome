package website.eccentric.tome.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import website.eccentric.tome.TomeItem;
import website.eccentric.tome.services.Services;
import website.eccentric.tome.services.Tome;

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
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();
            var hand = TomeItem.inHand(player);

            if (hand != null) {
                var tomeService = Services.load(Tome.class);
                var stack = player.getItemInHand(hand);
                var tome = tomeService.revert(stack);
                player.setItemInHand(hand, tomeService.attach(tome, stack));

                if (player.level.isClientSide) {
                    Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(hand);
                }
            }

            context.get().setPacketHandled(true);
        });
    }
}