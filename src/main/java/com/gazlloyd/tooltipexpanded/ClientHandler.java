package com.gazlloyd.tooltipexpanded;

    import codechicken.nei.*;
    import cpw.mods.fml.common.FMLCommonHandler;
    import cpw.mods.fml.common.eventhandler.SubscribeEvent;
    import cpw.mods.fml.common.gameevent.TickEvent;
    import net.minecraftforge.common.MinecraftForge;

public class ClientHandler  {

    private static ClientHandler instance;

    public static void load() {

        instance = new ClientHandler();

        FMLCommonHandler.instance().bus().register(instance);
        MinecraftForge.EVENT_BUS.register(instance);

        HUDRenderer.load();
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.END && NEIClientConfig.isEnabled())
            HUDRenderer.renderOverlay();
    }


    public static ClientHandler instance() {
        return instance;
    }


    }

