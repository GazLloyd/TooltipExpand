package com.gazlloyd.tooltipexpanded;

    import com.gazlloyd.tooltipexpanded.config.Config;
    import cpw.mods.fml.common.FMLCommonHandler;
    import cpw.mods.fml.common.eventhandler.SubscribeEvent;
    import cpw.mods.fml.common.gameevent.TickEvent;
    import net.minecraft.util.StringTranslate;
    import net.minecraftforge.common.MinecraftForge;

    import java.io.InputStream;

public class ClientHandler  {

    private static ClientHandler instance;

    public static void load() {

        instance = new ClientHandler();

        FMLCommonHandler.instance().bus().register(instance);
        MinecraftForge.EVENT_BUS.register(instance);


        InputStream inputstream = ClientHandler.class.getResourceAsStream("/en_US.lang");
        StringTranslate.inject(inputstream);

        HUDRenderer.load();
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.END && Config.isEnabled())
            HUDRenderer.renderOverlay();
    }


    public static ClientHandler instance() {
        return instance;
    }

}

