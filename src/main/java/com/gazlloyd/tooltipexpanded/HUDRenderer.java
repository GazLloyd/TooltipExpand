package com.gazlloyd.tooltipexpanded;

import codechicken.lib.config.ConfigTag;
import codechicken.nei.KeyManager;
import codechicken.nei.KeyManager.IKeyStateTracker;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static codechicken.lib.gui.GuiDraw.*;

public class HUDRenderer implements IKeyStateTracker
{
    @Override
    public void tickKeyStates() {
        if (KeyManager.keyStates.get("world.highlight_tips").down) {
            ConfigTag tag = NEIClientConfig.getSetting("world.highlight_tips");
            tag.setBooleanValue(!tag.getBooleanValue());
        }
    }

    public static void renderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null &&
                mc.theWorld != null &&
                !mc.gameSettings.keyBindPlayerList.getIsKeyPressed() &&
                NEIClientConfig.getBooleanSetting("world.highlight_tips") &&
                mc.thePlayer.getHeldItem() != null ) {
            ItemStack item = mc.thePlayer.getHeldItem();
            if (item == null)
                return;

            ArrayList<String> text = new ArrayList<String>();
            text.add(item.getDisplayName());

            //if the item is damageable, add another line for remaining durability
            if (item.isItemStackDamageable()) {

                int dur = item.getMaxDamage() - item.getItemDamage();
                int expDur;

                //if the item is enchantable, check for unbreaking, add another line to have expected uses
                int unbrLv = 0;
                if (item.isItemEnchanted()) {
                    unbrLv = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, item);

                    //if unbrLv > 0, unbreaking enchantment of that level
                    if (unbrLv > 0) {
                        //if armour...
                        if (item.getItem() instanceof ItemArmor) {
                            //durability increased by 25/36/43%
                            expDur = (int)(dur/(0.6+0.4/(unbrLv+1)));
                        }
                        //if not armour (i.e. a tool)...
                        else {
                            //durability increased by 100/200/300%
                            expDur = dur*(unbrLv+1);
                        }
                        text.add("Expected uses: "+expDur);
                        text.add("(Minimum: "+dur+")");
                    }

                    //no unbreaking
                    else {
                        text.add("Uses left: "+dur);
                    }

                }
                //else item is not enchantable but is damageable
                else {

                    text.add("Uses left: "+dur);
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
                100,
                5000);
    }

    public static void load() {
        KeyManager.trackers.add(new HUDRenderer());
    }
}