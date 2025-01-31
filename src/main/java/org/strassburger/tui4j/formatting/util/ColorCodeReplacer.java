package org.strassburger.tui4j.formatting.util;

import org.strassburger.colorlab4j.color.spaces.RGBColor;
import org.strassburger.tui4j.formatting.TextColor;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorCodeReplacer {
    private static final Map<String, TextColor> colorMap = new HashMap<>();

    static {
        colorMap.put("&0", TextColor.BLACK);
        colorMap.put("&1", TextColor.BLUE);
        colorMap.put("&2", TextColor.GREEN);
        colorMap.put("&3", TextColor.CYAN);
        colorMap.put("&4", TextColor.RED);
        colorMap.put("&5", TextColor.MAGENTA);
        colorMap.put("&6", TextColor.YELLOW);
        colorMap.put("&7", TextColor.WHITE);

        colorMap.put("&8", TextColor.BRIGHT_BLACK);
        colorMap.put("&9", TextColor.BRIGHT_BLUE);
        colorMap.put("&a", TextColor.BRIGHT_GREEN);
        colorMap.put("&b", TextColor.BRIGHT_CYAN);
        colorMap.put("&c", TextColor.BRIGHT_RED);
        colorMap.put("&d", TextColor.BRIGHT_MAGENTA);
        colorMap.put("&e", TextColor.BRIGHT_YELLOW);
        colorMap.put("&f", TextColor.BRIGHT_WHITE);

        colorMap.put("&l", TextColor.BOLD);
        colorMap.put("&n", TextColor.UNDERLINE);
        colorMap.put("&r", TextColor.RESET);

        colorMap.put("&x0", TextColor.BG_BLACK);
        colorMap.put("&x1", TextColor.BG_BLUE);
        colorMap.put("&x2", TextColor.BG_GREEN);
        colorMap.put("&x3", TextColor.BG_CYAN);
        colorMap.put("&x4", TextColor.BG_RED);
        colorMap.put("&x5", TextColor.BG_MAGENTA);
        colorMap.put("&x6", TextColor.BG_YELLOW);
        colorMap.put("&x7", TextColor.BG_WHITE);

        colorMap.put("&x8", TextColor.BG_BRIGHT_BLACK);
        colorMap.put("&x9", TextColor.BG_BRIGHT_BLUE);
        colorMap.put("&xa", TextColor.BG_BRIGHT_GREEN);
        colorMap.put("&xb", TextColor.BG_BRIGHT_CYAN);
        colorMap.put("&xc", TextColor.BG_BRIGHT_RED);
        colorMap.put("&xd", TextColor.BG_BRIGHT_MAGENTA);
        colorMap.put("&xe", TextColor.BG_BRIGHT_YELLOW);
        colorMap.put("&xf", TextColor.BG_BRIGHT_WHITE);
    }

    public static String replacePrefabColorCodes(String input) {
        for (Map.Entry<String, TextColor> entry : colorMap.entrySet()) {
            input = input.replaceAll("(?<!\\\\)" + entry.getKey(), entry.getValue().toString());
            input = input.replace("\\" + entry.getKey(), entry.getKey()); // Remove escape character
        }
        return input;
    }

    public static String replaceHexCodes(String input) {
        Pattern pattern = Pattern.compile("&#([a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String ansiCode = RGBColor.fromHex(matcher.group(1)).toAnsi();
            matcher.appendReplacement(sb, ansiCode);
        }
        matcher.appendTail(sb);

        sb.append(TextColor.RESET);
        return sb.toString();
    }

    public static String replaceBgHexCodes(String input) {
        Pattern pattern = Pattern.compile("&x#([a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String ansiCode = RGBColor.fromHex(matcher.group(1)).toAnsiBackground();
            matcher.appendReplacement(sb, ansiCode);
        }
        matcher.appendTail(sb);

        sb.append(TextColor.RESET);
        return sb.toString();
    }
}