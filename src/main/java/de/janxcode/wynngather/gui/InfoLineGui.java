package de.janxcode.wynngather.gui;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.inforenderer.DrawInfoPanel;
import de.janxcode.wynngather.utils.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class InfoLineGui extends GuiScreen {
    private final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
    private final List<GuiTextField> textFields = new LinkedList<>();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        //title
        String title = "Edit GUI Lines";
        double TMultiplier = (width / (double) fr.getStringWidth(title)) / 2.8;

        GlStateManager.pushMatrix();
        GlStateManager.scale(TMultiplier, TMultiplier, 1);
        int tPosX = (int) Math.round((width / 2d - fr.getStringWidth(title) * TMultiplier / 2) / TMultiplier);
        int tPosY = (int) Math.round(((height / 8d - fr.FONT_HEIGHT * TMultiplier) / TMultiplier));
        fr.drawString(title, tPosX, tPosY, ModConfig.TextColor);
        GlStateManager.popMatrix();

        for (GuiTextField field : textFields){
            field.drawTextBox();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        WynnGather.GUI = true;
        textFields.clear();

        int index = 0;
        for (String line : ModConfig.infoLines){
            index++;

            GuiTextField field = new GuiTextField(index, fr, width / 30, height / 10 + height / 16 * index, width - width / 15, 20);
            field.setMaxStringLength(200);
            field.setText(line);
            textFields.add(field);
        }

        super.initGui();
    }

    @Override
    public void updateScreen() {
        for (GuiTextField field : textFields){
            field.updateCursorCounter();
        }

        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        System.out.println(typedChar);

        for (GuiTextField field : textFields){
            if(!field.isFocused()) continue;

            field.textboxKeyTyped(typedChar, keyCode);
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (GuiTextField field : textFields){
            if(field.isFocused()) field.setFocused(false);

            //Ah yes it fucking sucks
            if(mouseX > field.x && mouseX < field.x + field.width && mouseY > field.y && mouseY < field.y + field.height){
                field.setFocused(true);

                double averageCharSize = fr.getStringWidth(field.getText())  / (double) field.getText().length();
                if(mouseX - field.x < averageCharSize * (field.getText().length())) {
                    field.setCursorPosition((int) Math.floor((mouseX - field.x) / averageCharSize));
                } else {
                    field.setCursorPosition(field.getText().length());
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        WynnGather.GUI = false;

        ModConfig.infoLines = textFields.stream()
                .map(GuiTextField::getText)
                .toArray(String[]::new);

        ConfigManager.sync(WynnGather.MODID, Config.Type.INSTANCE);
        DrawInfoPanel.getPanel().update();

        super.onGuiClosed();
    }
}
