package dev.drygo.XPlushes.Utils;

import dev.drygo.XPlushes.XPlushes;

import java.util.HashMap;
import java.util.Map;

public class DebugUtils {
    private static XPlushes plugin;
    private static final Map<String, Long> lastDebugTimes = new HashMap<>();
    private static final long COOLDOWN_MILLIS = 250;

    public static void debug(String message) {
        if (!plugin.getConfig().getBoolean("settings.debug", false)) return;

        long now = System.currentTimeMillis();
        long last = lastDebugTimes.getOrDefault(message, 0L);

        if (now - last >= COOLDOWN_MILLIS) {
            lastDebugTimes.put(message, now);
            plugin.getLogger().info("[DEBUG] " + message);
        }
    }

    public static boolean isDebug() {
        return plugin.getConfig().getBoolean("settings.debug", false);
    }

    public static void init(XPlushes plugin) {
        DebugUtils.plugin = plugin;
    }
}