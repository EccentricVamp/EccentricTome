package website.eccentric.tome;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

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
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();            
            ItemStack stack = player.getMainHandItem();
            Hand hand = Hand.MAIN_HAND;

            boolean hasTome = TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem);
            if (!hasTome) {
                stack = player.getOffhandItem();
                hasTome = TomeItem.isTome(stack) && !(stack.getItem() instanceof TomeItem);
                hand = Hand.OFF_HAND;
            }

            if (hasTome) {
                ItemStack tome = TomeItem.revert(stack);
                player.setItemInHand(hand, TomeItem.attach(tome, stack));

                if (player.level.isClientSide) {
                    Minecraft.getInstance().gameRenderer.itemInHandRenderer.itemUsed(hand);
                }
            }

            context.get().setPacketHandled(true);
        });
    }

}