package de.janxcode.wynngather.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemUtils {
    private static final Pattern DURABILITY_PATTERN = Pattern.compile("(\\d+/\\d+)\\sDurability");

    public static String getGatherToolDurability(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return null;

        NBTTagCompound tag = itemStack.getTagCompound();

        assert tag != null;
        NBTTagCompound display = tag.getCompoundTag("display");

        NBTTagList lore = display.getTagList("Lore", 8);
        for (int i = 0; i < lore.tagCount(); i++) {
            String loreLine = lore.getStringTagAt(i);

            if (!loreLine.contains("Gathering Tool")) continue;

            Matcher matcher = DURABILITY_PATTERN.matcher(loreLine);
            if (matcher.find()) return matcher.group(1);
        }

        return null;
    }
}
