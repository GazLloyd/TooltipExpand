package com.gazlloyd.tooltipexpanded.config;

import codechicken.nei.config.OptionButton;
import net.minecraft.client.Minecraft;

public class OptionTooltipExpand extends OptionButton {

    private String enabledName;

    public OptionTooltipExpand(String name, String enabledName)
    {
        super(name, null, name, null);
        this.enabledName = enabledName;
    }

    @Override
    public void copyGlobals()
    {
        copyGlobal(name);
        copyGlobal(name+".x");
        copyGlobal(name+".y");
    }

    @Override
    public boolean onClick(int button)
    {
        if(renderDefault())
            return false;

        Minecraft.getMinecraft().displayGuiScreen(new GuiTooltipExpand(this, enabledName));
        return true;
    }
}
