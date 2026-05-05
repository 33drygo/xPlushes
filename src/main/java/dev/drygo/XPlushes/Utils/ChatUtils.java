package dev.drygo.XPlushes.Utils;

import dev.drygo.XPlushes.Managers.ConfigManager;
import dev.drygo.XPlushes.XPlushes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    private static XPlushes plugin;

    public static void init(XPlushes plugin) {
        ChatUtils.plugin = plugin;
    }

    public static String formatColor(String message) {
        message = replaceHexColors(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String replaceHexColors(String message) {
        Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            StringBuilder color = new StringBuilder("&x");
            for (char c : hexColor.toCharArray()) {
                color.append("&").append(c);
            }
            matcher.appendReplacement(buffer, color.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
    public static String getMessage(String path, Player player) {

        String message = ConfigManager.getMessageConfig().isList(path)
                ? String.join("\n", ConfigManager.getMessageConfig().getStringList(path))
                : ConfigManager.getMessageConfig().getString(path);

        if (message == null || message.isEmpty()) {
            plugin.getLogger().warning("[WARNING] Message not found: " + path);
            return ChatUtils.formatColor("&r" + ConfigManager.getPrefix() + " #FF0000&l[ERROR] #FF3535Message not found: " + path);
        }

        message = message.replace("%player%", player != null ? player.getName() : "Unknown");

        /*
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
         */

        message = message.replace("%prefix%", ConfigManager.getPrefix());

        return ChatUtils.formatColor(message);
    }
}
