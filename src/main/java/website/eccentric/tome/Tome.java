package website.eccentric.tome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Tome.MOD_ID)
public class Tome {

	public static final String MOD_ID = "tome";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public Tome() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
	}

    private void onCommonSetup(final FMLCommonSetupEvent event) {
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
    }
}
