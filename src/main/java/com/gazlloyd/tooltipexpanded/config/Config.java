package com.gazlloyd.tooltipexpanded.config;

import codechicken.core.CommonUtils;
import codechicken.lib.config.ConfigFile;
import codechicken.lib.config.ConfigTag;
import codechicken.lib.config.ConfigTagParent;
import codechicken.nei.api.API;
import codechicken.nei.config.*;

import java.io.File;

public class Config {

    public static File configDir = new File(CommonUtils.getMinecraftDir(), "config/NEI");

    public static ConfigSet config = new ConfigSet(new File("saves/NEI/client.dat"), new ConfigFile(new File(configDir,"client.cfg")));
    //private static OptionList opl;

    static {
        //opl = new OptionList("tooltipexpanded");

        linkOptionList();
        setDefaults();
    }


    private static void setDefaults() {
        ConfigTagParent tag = config.config;
        //global enable/disable - turn off to completely deactivate mod
        tag.getTag("tooltipexpanded.global").getBooleanValue(true);
        API.addOption(new OptionToggleButton("tooltipexpanded.global",true));


        //for items that are not tools or armour
        tag.getTag("tooltipexpanded.other").getBooleanValue(true);
        API.addOption(new OptionToggleButton("tooltipexpanded.other", true));

        //for not-unbreaking tools
        tag.getTag("tooltipexpanded.tools").getIntValue(2);
        API.addOption(new OptionCycled("tooltipexpanded.tools", 3, true));

        //for not-unbreaking armour
        tag.getTag("tooltipexpanded.armour").getIntValue(2);
        API.addOption(new OptionCycled("tooltipexpanded.armour", 3, true));

        //for unbreaking tools
        tag.getTag("tooltipexpanded.unbrtools").getIntValue(4);
        API.addOption(new OptionCycled("tooltipexpanded.unbrtools", 5, true));

        //for unbreaking armour
        tag.getTag("tooltipexpanded.unbrarmour").getIntValue(4);
        API.addOption(new OptionCycled("tooltipexpanded.unbrarmour", 5, true));

        tag.getTag("tooltipexpanded.pos").getBooleanValue(true);
        tag.getTag("tooltipexpanded.pos.x").getIntValue(100);
        tag.getTag("tooltipexpanded.pos.y").getIntValue(5000);
        API.addOption(new OptionTooltipExpand("tooltipexpanded.pos","tooltipexpanded.global"));

    }


    private static void linkOptionList() {
        getOptionList().bindConfig(new IConfigSetHolder()
        {
            @Override
            public ConfigSet worldConfigSet() {
                return config;
            }

            @Override
            public ConfigSet globalConfigSet() {
                return config;
            }
        });
    }

    public static OptionList getOptionList() {
        return OptionList.getOptionList("nei.options");
    }

    public static ConfigTag getSetting(String s) {
        return config.config.getTag(s);
    }

    public static boolean getBoolean(String s) {
        return getSetting(s).getBooleanValue();
    }

    public static int getInt(String s) {
        return getSetting(s).getIntValue();
    }

    public static int getPosX() {
        return getInt("tooltipexpanded.pos.x");
    }

    public static int getPosY() {
        return getInt("tooltipexpanded.pos.y");
    }

    public static boolean isEnabled() {
        return getBoolean("tooltipexpanded.global");
    }


}
