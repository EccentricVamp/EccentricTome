package website.eccentric.tome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.data.DataGenerator;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import website.eccentric.tome.client.RenderGameOverlayHandler;
import website.eccentric.tome.client.TomeHandler;
import website.eccentric.tome.network.RevertMessage;
import website.eccentric.tome.network.TomeChannel;

@Mod(EccentricTome.ID)
public class EccentricTome {
    public static final String ID = "eccentrictome";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ID);

    public static final RegistryObject<IRecipeSerializer<?>> ATTACHMENT = RECIPES.register("attachment", EccentricTome::registerSerializer);
    public static final RegistryObject<Item> TOME = ITEMS.register("tome", TomeItem::new);

    public static SimpleChannel CHANNEL;

    public EccentricTome() {
        IEventBus modEvent = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEvent);
        RECIPES.register(modEvent);

        modEvent.addListener(this::onClientSetup);
        modEvent.addListener(this::onCommonSetup);
        modEvent.addListener(this::onGatherData);
        modEvent.addListener(this::onModConfig);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.SPEC);

        IEventBus minecraftEvent = MinecraftForge.EVENT_BUS;
        minecraftEvent.addListener(this::onPlayerLeftClick);
        minecraftEvent.addListener(EventPriority.LOW, this::onItemDropped);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, RenderGameOverlayHandler::onRender);
        MinecraftForge.EVENT_BUS.addListener(TomeHandler::onOpenTome);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        CHANNEL = TomeChannel.register();
    }

    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new TomeRecipe(generator));
    }

    private void onModConfig(ModConfigEvent event) {
        Configuration.ALIAS_MAP.clear();
        for (String alias : Configuration.ALIASES.get()) {
            String[] tokens = alias.split("=");
            Configuration.ALIAS_MAP.put(tokens[0], tokens[1]);
        }
    }

    private void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack stack = event.getItemStack();
        if (Tome.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            CHANNEL.sendToServer(new RevertMessage());
        }
    }

    private void onItemDropped(ItemTossEvent event) {
        if (!event.getPlayer().isShiftKeyDown()) return;

        ItemEntity entity = event.getEntityItem();
        ItemStack stack = entity.getItem();

        if (Tome.isTome(stack) && !(stack.getItem() instanceof TomeItem)) {
            ItemStack detatchment = Tome.revert(stack);
            World level = entity.getCommandSenderWorld();

            if (!level.isClientSide) {
                level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), detatchment));
            }

            entity.setItem(stack);
        }
    }

    private static IRecipeSerializer<?> registerSerializer() {
        AttachmentRecipe.SERIALIZER = new SpecialRecipeSerializer<>(AttachmentRecipe::new);
        return AttachmentRecipe.SERIALIZER;
    }
}
