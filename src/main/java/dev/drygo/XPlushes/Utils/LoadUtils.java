package dev.drygo.XPlushes.Utils;

import dev.drygo.XPlushes.Commands.XPlushesCommand;
import dev.drygo.XPlushes.Commands.XPlushesTabCompleter;
import dev.drygo.XPlushes.Managers.ConfigManager;
import dev.drygo.XPlushes.Managers.PlushManager;
import dev.drygo.XPlushes.Menu.Listeners.MenuListener;
import dev.drygo.XPlushes.XPlushes;
import org.bukkit.command.PluginCommand;

public class LoadUtils {

    private static XPlushes plugin;

    public static void init(XPlushes plugin) {
        LoadUtils.plugin = plugin;
    }

    public static void loadFeatures() {
        loadFiles();
        loadCommands();
        loadListeners();
    }

    public static void loadFiles() {
        ConfigManager.loadConfig();
        ConfigManager.reloadMessages();
        PlushManager.loadPlushes(plugin);
    }

    private static void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    private static void loadCommands() {
        PluginCommand cmd = plugin.getCommand("xplushes");
        if (cmd == null) {
            plugin.getLogger().severe("❌ Error: /xplushes command is not registered in plugin.yml");
            return;
        }
        cmd.setExecutor(new XPlushesCommand(plugin));
        cmd.setTabCompleter(new XPlushesTabCompleter());
        plugin.getLogger().info("✅ /xplushes command was successfully loaded.");
    }
}
