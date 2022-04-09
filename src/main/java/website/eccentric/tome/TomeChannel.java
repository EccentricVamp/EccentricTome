package website.eccentric.tome;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TomeChannel {

    public static final ResourceLocation name = new ResourceLocation(EccentricTome.MODID, "general");
    public static final String version = new ResourceLocation(EccentricTome.MODID, "1").toString();

    public static SimpleChannel register() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(name)
            .clientAcceptedVersions(version -> true)
            .serverAcceptedVersions(version -> true)
            .networkProtocolVersion(() -> version)
            .simpleChannel();
        
        channel.registerMessage(1, ConvertMessage.class, ConvertMessage::encode, ConvertMessage::decode, ConvertMessage::handle);
        channel.registerMessage(2, RevertMessage.class, RevertMessage::encode, RevertMessage::decode, RevertMessage::handle);

        return channel;
    }
    
}
