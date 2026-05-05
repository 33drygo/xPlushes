package dev.drygo.XPlushes;

import dev.drygo.XPlushes.Managers.ConfigManager;
import dev.drygo.XPlushes.Models.UUIDCache;
import dev.drygo.XPlushes.Utils.ChatUtils;
import dev.drygo.XPlushes.Utils.DebugUtils;
import dev.drygo.XPlushes.Utils.LoadUtils;
import dev.drygo.XPlushes.Utils.LogsUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class XPlushes extends JavaPlugin {
    public String prefix;
    public String version;

    @Override
    public void onEnable() {
        version = getDescription().getVersion();
        DebugUtils.init(this);
        ConfigManager.init(this);
        ChatUtils.init(this);
        UUIDCache.init(this);
        LogsUtils.init(this);
        LoadUtils.init(this);
        LoadUtils.loadFeatures();
        LogsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        LogsUtils.sendShutdownMessage();
    }
}
