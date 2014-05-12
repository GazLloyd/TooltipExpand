package com.gazlloyd.tooltipexpanded;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = TooltipExpanded.MODID, version = TooltipExpanded.VERSION, dependencies = "required-after:NotEnoughItems")
public class TooltipExpanded
{
    public static final String MODID = "tooltipexpanded";
    public static final String VERSION = "1.1.12";

    @Mod.Instance
    public TooltipExpanded instance;


    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        instance = this;
        Logger log = FMLLog.getLogger();
        log.info("Initialising TooltipExpand...");

        ClientHandler.load();

        log.info("TooltipExpand initialised!");
    }


}
