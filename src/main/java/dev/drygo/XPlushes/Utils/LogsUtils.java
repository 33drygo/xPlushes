package dev.drygo.XPlushes.Utils;

import dev.drygo.XPlushes.XPlushes;
import org.bukkit.Bukkit;

public class LogsUtils {
    private static XPlushes plugin;

    public static void init(XPlushes plugin) {
        LogsUtils.plugin = plugin;
    }

    public static void sendStartupMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&9&lx&r&lPlushes #a0ff72plugin enabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#ffe94fVersion: #ffffff" + plugin.version));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#ffe94fDeveloped by: #ffffff" + String.join(", ", plugin.getDescription().getAuthors())));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
    }
    public static void sendShutdownMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&9&lx&r&lPlushes #ff7272plugin disabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#ffe94fVersion: #ffffff" + plugin.version));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#ffe94fDeveloped by: #ffffff" + String.join(", ", plugin.getDescription().getAuthors())));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
    }
}
