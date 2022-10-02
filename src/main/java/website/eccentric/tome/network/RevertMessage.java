package website.eccentric.tome.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import website.eccentric.tome.EccentricTome;
import website.eccentric.tome.Tome;

public class RevertMessage {
    public static RevertMessage decode(final PacketBuffer buffer) {
        buffer.readByte();
        return new RevertMessage();
    }

    public static void encode(final RevertMessage message, final PacketBuffer buffer) {
        buffer.writeByte(0);
    }

    @SuppressWarnings("resource")
    public static void handle(final RevertMessage message, final Supplier<NetworkEvent.Context> context) {
        EccentricTome.LOGGER.debug("Received revert message.");

        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            Hand hand = Tome.inHand(player);

            if (hand != null) {
                ItemStack stack = player.getItemInHand(hand);
                ItemStack tome = Tome.revert(stack);
                player.setItemInHand(hand, Tome.attach(tome, stack));

                if (player.level.isClientSide) {
                    Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(hand);
                }
            }

            context.get().setPacketHandled(true);
        });
    }
}