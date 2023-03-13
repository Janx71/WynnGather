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

    public static int renderDistance = 50;

    public static String[] infoLines = {
            "{YELLOW}Resource type: {AQUA}{BOLD}{type}",
            "{YELLOW}Time spent: {AQUA}{BOLD}{time}",
            "{YELLOW}Nodes mined: {AQUA}{BOLD}{nodesMined}",
            "{YELLOW}Nodes per minute: {AQUA}{BOLD}{nodesPerMinute}",
            "{YELLOW}Percentage to next Level: {AQUA}{BOLD}{nextLevel}%",
            "{YELLOW}XP per hour: {AQUA}{BOLD}{xpPerHour}",
            "{progress}",
            "",
            "",
            "",
            ""
    };
}
