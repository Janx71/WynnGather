package de.janxcode.wynngather.utils;

import de.janxcode.wynngather.WynnGather;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;

import java.awt.*;

@Config(modid = WynnGather.MODID)
@Config.LangKey("wynngather")
public class ModConfig {
    @Config.Comment("Colors are represented in decimal format")
    public static int TextColor = 0xFFFFFF;

    public static TextFormatting highlight = TextFormatting.AQUA;

    @Config.Comment("Left Corner of the text info")
    public static int guiPosX = 40;
    public static int guiPosY = 60;
}
