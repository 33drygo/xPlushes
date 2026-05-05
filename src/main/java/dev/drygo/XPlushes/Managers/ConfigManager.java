package dev.drygo.XPlushes.Managers;

import dev.drygo.XPlushes.Utils.ChatUtils;
import dev.drygo.XPlushes.XPlushes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    private static final String DEFAULT_PREFIX = "#B3A3FF&lx&f&lPlushes &8»&r";

    private static XPlushes plugin;
    private static FileConfiguration messagesConfig;

    public static void init(XPlushes plugin) {
        ConfigManager.plugin = plugin;
    }

    public static void loadConfig() {
        try {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
            plugin.getLogger().info("✅ The config.yml file successfully loaded.");
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed on loading config.yml: " + e.getMessage());
        }
    }

    public static void reloadMessages() {
        try {
            File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
            if (!messagesFile.exists()) {
                plugin.saveResource("messages.yml", false);
                plugin.getLogger().info("✅ The messages.yml file did not exist, it has been created.");
            } else {
                plugin.getLogger().info("✅ The messages.yml file has been loaded successfully.");
            }

            messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

            String configured = messagesConfig.getString("prefix");
            if (configured == null || configured.isBlank()) configured = DEFAULT_PREFIX;
            plugin.prefix = ChatUtils.formatColor(configured);
        } catch (Exception e) {
            plugin.getLogger().severe("❌ Failed to load messages configuration: " + e.getMessage());
            plugin.prefix = ChatUtils.formatColor(DEFAULT_PREFIX);
        }
    }

    public static String getPrefix() { return plugin.prefix; }
    public static void setPrefix(String prefix) {
        plugin.prefix = (prefix == null || prefix.isBlank())
                ? ChatUtils.formatColor(DEFAULT_PREFIX)
                : ChatUtils.formatColor(prefix);
    }
    public static FileConfiguration getMessageConfig() {
        return messagesConfig;
    }
}
