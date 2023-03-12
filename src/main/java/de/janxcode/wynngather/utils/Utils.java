package de.janxcode.wynngather.utils;

import de.janxcode.wynngather.handlers.NodeMinedEvent;
import de.janxcode.wynngather.inforenderer.Node;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static void parseXpInfo(String input, Node node) {
        int xp = 0;
        int nexLevel = 0;

        System.out.println(input);

        // Regular expression to match the xp and xp type
        String xpRegex = "\\[(\\+?-?\\d+)\\s+[ⒷⓀⒿⒸ]\\s+(\\w+)\\s+XP]";

        // Regular expression to match the next level percentage
        String nexLevelRegex = "\\[(\\d+)%]";

        // Compile the regular expressions
        Pattern xpPattern = Pattern.compile(xpRegex);
        Pattern nexLevelPattern = Pattern.compile(nexLevelRegex);

        // Match the regular expressions against the input string
        Matcher xpMatcher = xpPattern.matcher(input);
        Matcher nexLevelMatcher = nexLevelPattern.matcher(input);

        // Extract the xp and xp type if there is a match
        if (xpMatcher.find()) {
            xp = Integer.parseInt(xpMatcher.group(1));
            String xpType = xpMatcher.group(2);
            //System.out.println("XP Type: " + xpType);
        }

        // Extract the next level percentage if there is a match
        if (nexLevelMatcher.find()) {
            nexLevel = Integer.parseInt(nexLevelMatcher.group(1));
        }

        MinecraftForge.EVENT_BUS.post(new NodeMinedEvent(xp, nexLevel, node));
    }

    public static String formatValue(long value) {
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

    public static AxisAlignedBB getAabb(int radius){
        Minecraft mc = Minecraft.getMinecraft();

        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;

        return new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z+ radius);
    }
}
