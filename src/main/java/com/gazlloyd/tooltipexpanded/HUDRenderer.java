package com.gazlloyd.tooltipexpanded;

import codechicken.nei.guihook.GuiContainerManager;
import com.gazlloyd.tooltipexpanded.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.*;

public class HUDRenderer
{

    private static int checkItem(ItemStack item) {
        int i=0;

        //no held item == 0
        if (item == null)
            return 0;

        //item stack is not damageable == 1
        if (!item.isItemStackDamageable())
            return 1;

        //item is not enchanted with unbreaking == 2
        if (!item.isItemEnchanted() || EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, item) == 0)
            i=2;

        //item is unbreaking == 3
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, item) > 0)
            i=4;

        //if item is armour, add one
        if (item.getItem() instanceof ItemArmor)
            i++;

        //something went wrong == 0
        return i;
    }

    public static int checkDisplay(int itemtype) {
        switch (itemtype){
            case 0: return 0;
            case 1: return Config.getBoolean("tooltipexpanded.other") ? 1 : 0;
            case 2: return Config.getInt("tooltipexpanded.tools");
            case 3: return Config.getInt("tooltipexpanded.armour");
            case 4: return Config.getInt("tooltipexpanded.unbrtools");
            case 5: return Config.getInt("tooltipexpanded.unbrarmour");
        }
        return 0;

    }


    public static void renderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.currentScreen == null &&
                mc.theWorld != null &&
                !mc.gameSettings.keyBindPlayerList.getIsKeyPressed() &&
                mc.thePlayer.getHeldItem() != null ) {
            ItemStack item = mc.thePlayer.getHeldItem();

            int itemtype = checkItem(item);
            if (itemtype == 0)
                return;

            int displaytype = checkDisplay(itemtype);
            if (displaytype == 0)
                return;

            ArrayList<String> text = new ArrayList<String>();
            text.add(item.getDisplayName());

            if (itemtype > 1 && displaytype > 1) {
                int uses = item.getMaxDamage() - item.getItemDamage();
                int unbrlv;

                switch (itemtype) {
                    //normal tools and armour
                    case 2:
                    case 3:
                        text.add("Uses left: "+uses);
                        break;
                    //unbreaking tools
                    case 4:
                        unbrlv = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, item);
                        switch (displaytype) {
                            case 2:
                                text.add("Minimum uses: "+uses);
                                break;
                            case 3:
                                text.add("Expected uses: "+uses*(unbrlv+1));
                                break;
                            case 4:
                            default:
                                text.add("Expected uses: "+uses*(unbrlv+1));
                                text.add("(Minimum uses: "+uses+")");
                                break;
                        }
                        break;
                    //unbreaking armour
                    case 5:
                        unbrlv = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, item);
                        switch (displaytype) {
                            case 2:
                                text.add("Minimum uses: "+uses);
                                break;
                            case 3:
                                text.add("Expected uses: "+(int)(uses/(0.6+0.4/(unbrlv+1))));
                                break;
                            case 4:
                            default:
                                text.add("Expected uses: "+(int)(uses/(0.6+0.4/(unbrlv+1))));
                                text.add("(Minimum uses: "+uses+")");
                                break;
                        }
                }
            }

            renderOverlay(item, text, getPositioning());
        }
    }

    public static void renderOverlay(ItemStack stack, List<String> textData, Point pos) {
        int w = 0;
        for (String s : textData)
            w = Math.max(w, getStringWidth(s) + 29);
        int h = Math.max(24, 10 + 10 * textData.size());

        Dimension size = displaySize();
        int x = (size.width - w - 1) * pos.x / 10000;
        int y = (size.height - h - 1) * pos.y / 10000;

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        drawTooltipBox(x, y, w, h);

        int ty = (h - 8 * textData.size()) / 2;
        for (int i = 0; i < textData.size(); i++)
            drawString(textData.get(i), x + 24, y + ty + 10 * i, 0xFFA0A0A0, true);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if (stack.getItem() != null)
            GuiContainerManager.drawItem(x + 5, y + h / 2 - 8, new ItemStack(stack.getItem()));
    }

    private static Point getPositioning() {
        return new Point(
                Config.getPosX(),
                Config.getPosY()
        );
    }

    public static void load() {
    }
}