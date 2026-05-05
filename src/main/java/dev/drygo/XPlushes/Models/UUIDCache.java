package dev.drygo.XPlushes.Models;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.drygo.XPlushes.Utils.SkullUtils;
import dev.drygo.XPlushes.XPlushes;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UUIDCache {
    private static final ConcurrentHashMap<String, UUID> cache = new ConcurrentHashMap<>();
    private static XPlushes plugin;
    private static volatile boolean isLoaded = false;

    public static void init(XPlushes p) {
        plugin = p;
    }

    public static void loadUUIDs(List<String> playerNames) {
        isLoaded = false;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            int loaded = 0;
            java.util.List<String> failedNames = new java.util.ArrayList<>();

            for (String playerName : playerNames) {
                if (playerName == null || playerName.isEmpty()) {
                    failedNames.add("<empty entry>");
                    continue;
                }
                try {
                    PlayerProfile profile = Bukkit.createProfile(playerName);
                    boolean completed = profile.complete();
                    UUID uuid = profile.getId();

                    if (completed && uuid != null) {
                        cache.put(playerName.toLowerCase(), uuid);
                        SkullUtils.prefetchTexture(uuid);
                        loaded++;
                    } else {
                        failedNames.add(playerName);
                    }

                    Thread.sleep(50);

                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    failedNames.add(playerName);
                }
            }

            final int finalLoaded = loaded;
            final java.util.List<String> finalFailed = failedNames;

            Bukkit.getScheduler().runTask(plugin, () -> {
                isLoaded = true;
                plugin.getLogger().info("UUID Cache loaded: " + finalLoaded + " players, " + finalFailed.size() + " failed");
                if (!finalFailed.isEmpty()) {
                    plugin.getLogger().warning("Plushes that could not be resolved (will render as default head): " + String.join(", ", finalFailed));
                }
            });
        });
    }

    public static UUID getUUIDFromCache(String playerName) {
        if (playerName == null) return null;
        return cache.get(playerName.toLowerCase());
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static void clearCache() {
        cache.clear();
        SkullUtils.clearCache();
        isLoaded = false;
    }

    public static int getCacheSize() {
        return cache.size();
    }
}
