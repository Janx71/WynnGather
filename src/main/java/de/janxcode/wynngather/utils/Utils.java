package de.janxcode.wynngather.utils;

import de.janxcode.wynngather.handlers.GatherNode;
import de.janxcode.wynngather.handlers.NodeProgressUpdatedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    // Regular expression to match the xp and xp type
    // \[(\+?-?\d+)\s+[ⒷⓀⒿⒸ]\s+(\w+)\s+XP]
    final static String xpRegex = "\\[(\\+?-?\\d+)\\s+[\\u24b7\\u24c0\\u24bf\\u24b8]\\s+(\\w+)\\s+XP]";
    final static Pattern xpPattern = Pattern.compile(xpRegex);

    // Regular expression to match the next level percentage
    final static String nexLevelRegex = "\\[(\\d+)%]";
    final static Pattern nexLevelPattern = Pattern.compile(nexLevelRegex);


    public static void postNodeMined(String wynnNodeMinedText, GatherNode node) {  // todo: refactor
        int xp = 0;
        // Extract the xp and xp type if there is a match

        // Match the regular expressions against the input string
        Matcher xpMatcher = xpPattern.matcher(wynnNodeMinedText);
        boolean b = xpMatcher.find();
        if (b) {
            xp = Integer.parseInt(xpMatcher.group(1));
            String xpType = xpMatcher.group(2);  // todo: probably post this too (after passing to enum)
            //System.out.println("XP Type: " + xpType);
        }

        int nexLevel = 0;

        // Match the regular expressions against the input string
        Matcher nexLevelMatcher = nexLevelPattern.matcher(wynnNodeMinedText);

        // Extract the next level percentage if there is a match
        if (nexLevelMatcher.find()) {
            nexLevel = Integer.parseInt(nexLevelMatcher.group(1));
        }

        MinecraftForge.EVENT_BUS.post(new NodeProgressUpdatedEvent.MinedEvent(xp, nexLevel, node));
    }

    public static String formatValue(long value) {
        // todo: refactor
        if (value < 1000) {
            return String.valueOf(value);
        }

        double result;
        String suffix;

        if (value < 1000000L) { // Thousands range
            result = ((double) value) / 1000.0;
            suffix = "k";
        } else if (value < 1000000000L) { // Millions range
            result = ((double) value) / 1000000.0;
            suffix = "m";
        } else { // Billions range
            result = ((double) value) / 1000000000.0;
            suffix = "b";
        }

        // Create a NumberFormat object to format the result with comma separator
        NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(0);
        nf.setGroupingUsed(true);

        // Format the result as a String with the appropriate suffix
        return nf.format(result) + suffix;
    }

    public static AxisAlignedBB getAabb(int radius) {
        Minecraft mc = Minecraft.getMinecraft();

        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;

        return new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
    }
}
