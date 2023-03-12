package de.janxcode.wynngather.gui;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.utils.ModConfig;
import de.janxcode.wynngather.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import java.awt.*;
import java.io.IOException;

public class ModMenuGui extends GuiScreen {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = mc.fontRenderer;
    private boolean toggled = false;
    private int mouseLastX;
    private int mouseLastY;
    private boolean mouse;
    Rectangle rect = new Rectangle(ModConfig.guiPosX, ModConfig.guiPosY, ModConfig.guiPosX + fr.getStringWidth("Percentage to next Level: 98%"), ModConfig.guiPosY + fr.FONT_HEIGHT * 7);
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        handleMouse(mouseX, mouseY);

        //title
        String title = "Gathering Helper";
        double TMultiplier = (width / (double) fr.getStringWidth(title)) / 2.8;

        GlStateManager.pushMatrix();
        GlStateManager.scale(TMultiplier, TMultiplier, 1);
        int tPosX = (int) Math.round((width / 2d - fr.getStringWidth(title) * TMultiplier / 2) / TMultiplier);
        int tPosY = (int) Math.round(((height / 12d - fr.FONT_HEIGHT * TMultiplier) / TMultiplier));
        fr.drawString(title, tPosX, tPosY, ModConfig.TextColor);
        GlStateManager.popMatrix();

        String[] text = {
                "Resource Type: " + ModConfig.highlight + TextFormatting.BOLD + "Dernic",
                "Time spent: " + ModConfig.highlight + TextFormatting.BOLD + "00:23:18",
                "Nodes Mined: " + ModConfig.highlight + TextFormatting.BOLD + "1294",
                "Nodes Per Minute: " + ModConfig.highlight + TextFormatting.BOLD + "16",
                "Percentage to next Level: " + ModConfig.highlight + TextFormatting.BOLD + "98%",
                "XP per Hour: " + ModConfig.highlight + TextFormatting.BOLD + "22m",
                TextFormatting.GREEN + "[|||Mining|||]"
        };

        drawRect(rect.x - 5, rect.y - 5, rect.width + 5, rect.height + 5, 0x66000000);
        RenderUtils.drawLayeredString(text, rect.x, rect.y, ModConfig.TextColor, fontRenderer);

        if(toggled){
            //horizontal
            drawHorizontalLine(rect.x - 8, rect.width + 8, rect.y - 8, Color.BLUE.getRGB());
            drawHorizontalLine(rect.x - 8, rect.width + 8, rect.height + 8, Color.BLUE.getRGB());

            drawVerticalLine(rect.x - 8, rect.y - 8, rect.height + 8 , Color.BLUE.getRGB());
            drawVerticalLine( rect.width + 8, rect.y - 8, rect.height + 8, Color.BLUE.getRGB());
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        WynnGather.GUI = true;
        super.initGui();
    }

    private void handleMouse(int x, int y){
        if(mouse && toggled && mouseLastX > rect.x - 5 && mouseLastX < rect.width + 5 && mouseLastY > rect.y - 5 && mouseLastY < rect.height + 5){
            ModConfig.guiPosX += x - mouseLastX;
            ModConfig.guiPosY += y - mouseLastY;

            rect.x = ModConfig.guiPosX;
            rect.y = ModConfig.guiPosY;
            rect.width = ModConfig.guiPosX + fr.getStringWidth("Percentage to next Level: 98%");
            rect.height = ModConfig.guiPosY + fr.FONT_HEIGHT * 7;
        }

        mouseLastX = x;
        mouseLastY = y;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mouse = false;

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseX > rect.x - 5 && mouseX < rect.width + 5 && mouseY > rect.y - 5 && mouseY < rect.height + 5){
            toggled = true;
            mouse = true;
            return;
        }

        toggled = false;

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        ConfigManager.sync(WynnGather.MODID, Config.Type.INSTANCE);
        WynnGather.GUI = false;

        super.onGuiClosed();
    }
}
