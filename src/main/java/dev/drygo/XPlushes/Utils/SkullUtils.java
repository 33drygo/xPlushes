package dev.drygo.XPlushes.Utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkullUtils {
    private static final ConcurrentHashMap<UUID, String> textureCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, Boolean> failedFetches = new ConcurrentHashMap<>();

    /**
     * Applies a cached texture (if available) to the skull, otherwise falls back
     * to {@link SkullMeta#setOwningPlayer(org.bukkit.OfflinePlayer)} which uses
     * the vanilla Steve head until the server resolves it.
     *
     * Never performs blocking I/O — callers should pre-warm the cache via
     * {@link #prefetchTexture(UUID)} on an async thread.
     */
    public static void applyTexture(SkullMeta meta, UUID uuid, String playerName) {
        if (meta == null || uuid == null) return;

        String texture = textureCache.get(uuid);
        if (texture == null || texture.isEmpty()) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            return;
        }

        try {
            PlayerProfile profile = Bukkit.createProfile(uuid, playerName);
            profile.setProperty(new ProfileProperty("textures", texture));
            meta.setPlayerProfile(profile);
        } catch (Throwable t) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Fetches and caches the texture for the given UUID. Performs blocking I/O —
     * MUST be called from an async thread.
     */
    public static void prefetchTexture(UUID uuid) {
        if (uuid == null) return;
        if (textureCache.containsKey(uuid)) return;
        if (Boolean.TRUE.equals(failedFetches.get(uuid))) return;

        String texture = fetchTextureFromMojang(uuid);
        if (texture != null && !texture.isEmpty()) {
            textureCache.put(uuid, texture);
        } else {
            failedFetches.put(uuid, Boolean.TRUE);
        }
    }

    public static void clearCache() {
        textureCache.clear();
        failedFetches.clear();
    }

    private static String fetchTextureFromMojang(UUID uuid) {
        HttpURLConnection connection = null;
        try {
            URI uri = URI.create(
                    "https://sessionserver.mojang.com/session/minecraft/profile/" +
                            uuid.toString().replace("-", "")
            );
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() != 200) return null;

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) response.append(line);
            }

            String json = response.toString();
            int propsStart = json.indexOf("\"properties\"");
            if (propsStart == -1) return null;
            int texturesNameStart = json.indexOf("\"name\" : \"textures\"", propsStart);
            if (texturesNameStart == -1) return null;
            int valueStart = json.indexOf("\"value\" : \"", texturesNameStart);
            if (valueStart == -1) return null;
            valueStart += 11;
            int valueEnd = json.indexOf("\"", valueStart);
            if (valueEnd <= valueStart) return null;
            return json.substring(valueStart, valueEnd);
        } catch (Exception ignored) {
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
