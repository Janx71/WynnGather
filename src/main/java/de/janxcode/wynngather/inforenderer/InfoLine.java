package de.janxcode.wynngather.inforenderer;

import net.minecraft.util.text.TextFormatting;

public class InfoLine {
    private final Info info = Info.getInstance();
    private final String line;

    public InfoLine(String line) {
        this.line = line;
    }

    public String getOutputLine() {
        StringBuilder output = new StringBuilder();
        String remainingLine = line;
        int start;
        int end = 0;
        while ((start = remainingLine.indexOf('{', end)) != -1 && (end = remainingLine.indexOf('}', start)) != -1) {
            output.append(remainingLine, 0, start); // append everything before the opening brace
            String textInsideBraces = remainingLine.substring(start + 1, end);
            String newTextInsideBraces = getValue(textInsideBraces);
            output.append(newTextInsideBraces); // append the modified text inside the braces
            remainingLine = remainingLine.substring(end + 1); // set the remainingLine to everything after the closing brace, excluding the closing brace itself
            end = 0; // reset the end variable to 0 so it starts searching for the next opening brace from the beginning of remainingLine
        }
        output.append(remainingLine); // append anything that comes after the last closing brace
        return output.toString();
    }


    private String getValue(String v) {
        // todo: refactor with enum
        switch (v) {
            case "nodesMined":
                return String.valueOf(info.getNodesMined());

            case "nodesPerMinute":
                return String.valueOf(info.getNodesPerMinute());

            case "xpPerHour":
                return String.valueOf(info.getXpPerHour());

            case "nextLevel":
                return String.valueOf(info.getNextLevel());

            case "type":
                return info.getType();

            case "time":
                return info.getTime();

            case "progress":
                return info.getProgress();

        }

        for (TextFormatting value : TextFormatting.values()) {
            try {
                TextFormatting formatting = TextFormatting.valueOf(v);
                if (value.equals(formatting)) {
                    return value.toString();
                }
            } catch (IllegalArgumentException e) {
                return "{" + v + "}";
            }
        }

        return "{" + v + "}";
    }
}
