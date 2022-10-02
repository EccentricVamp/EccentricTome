package website.eccentric.tome.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import website.eccentric.tome.EccentricTome;

public class TomeChannel {
    public static final ResourceLocation name = new ResourceLocation(EccentricTome.ID, "general");
    public static final String version = new ResourceLocation(EccentricTome.ID, "1").toString();

    public static SimpleChannel register() {
        final var channel = NetworkRegistry.ChannelBuilder.named(name)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> version)
                .simpleChannel();

        channel.registerMessage(1, ConvertMessage.class, ConvertMessage::encode, ConvertMessage::decode,
                ConvertMessage::handle);
        channel.registerMessage(2, RevertMessage.class, RevertMessage::encode, RevertMessage::decode,
                RevertMessage::handle);

        return channel;
    }
}
