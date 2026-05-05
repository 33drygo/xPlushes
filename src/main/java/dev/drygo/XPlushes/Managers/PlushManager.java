package dev.drygo.XPlushes.Managers;

import dev.drygo.XPlushes.Models.UUIDCache;
import dev.drygo.XPlushes.Utils.ChatUtils;
import dev.drygo.XPlushes.Utils.SkullUtils;
import dev.drygo.XPlushes.XPlushes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlushManager {
    private static List<String> plushes = new ArrayList<>();

    public static final int[] plushSlots = {
                1,  2,  3,  4,  5,  6,  7,
             9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
                28, 29, 30, 31, 32, 33, 34
    };

    public static void loadPlushes(XPlushes plugin) {
        List<String> raw = plugin.getConfig().getStringList("settings.plush_list");
        List<String> cleaned = new ArrayList<>(raw.size());
        for (String name : raw) {
            if (name != null && !name.isBlank()) cleaned.add(name.trim());
        }
        plushes = Collections.unmodifiableList(cleaned);
        UUIDCache.loadUUIDs(plushes);
        plugin.getLogger().info("[Plushes] Loaded " + plushes.size() + " plushes.");
    }

    public static List<String> getPlushes() {
        return plushes;
    }

    public static int size() {
        return plushes.size();
    }

    public static ItemStack getInvPlush(int id) {
        if (id < 0 || id >= plushes.size()) return null;
        String name = plushes.get(id);
        return buildSkull(
                name,
                "menu.plushes.plush.name",
                "menu.plushes.plush.lore"
        );
    }

    public static ItemStack getInvOwnPlush(Player p) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta == null) return skull;

        String name = p.getName();
        meta.setOwningPlayer(p);
        meta.setDisplayName(ChatUtils.getMessage("menu.plushes.own.name", null)
                .replace("%name%", name));

        List<String> lore = new ArrayList<>();
        for (String line : ConfigManager.getMessageConfig().getStringList("menu.plushes.own.lore")) {
            lore.add(ChatUtils.formatColor(line.replace("%name%", name)));
        }
        meta.setLore(lore);

        skull.setItemMeta(meta);
        return skull;
    }

    public static ItemStack getPlush(int id) {
        if (id < 0 || id >= plushes.size()) return null;
        return buildSkull(
                plushes.get(id),
                "items.plush.name",
                "items.plush.lore"
        );
    }

    public static ItemStack getPlush(String name) {
        if (name == null || name.isEmpty()) return null;
        return buildSkull(name, "items.plush.name", "items.plush.lore");
    }

    public static UUID getUUIDFromName(String playerName) {
        return UUIDCache.getUUIDFromCache(playerName);
    }

    private static ItemStack buildSkull(String name, String nameKey, String loreKey) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta == null) return skull;

        Player online = Bukkit.getPlayerExact(name);
        if (online != null) {
            meta.setOwningPlayer(online);
        } else {
            UUID uuid = UUIDCache.getUUIDFromCache(name);
            if (uuid != null) {
                SkullUtils.applyTexture(meta, uuid, name);
            }
            // No UUID resolved: leave the head without owner (default skin) so the
            // menu still renders. We intentionally do NOT call Bukkit.getOfflinePlayer(String)
            // here — on modern Paper it can block the main thread and warn loudly.
        }

        meta.setDisplayName(ChatUtils.getMessage(nameKey, null).replace("%name%", name));

        List<String> lore = new ArrayList<>();
        for (String line : ConfigManager.getMessageConfig().getStringList(loreKey)) {
            lore.add(ChatUtils.formatColor(line.replace("%name%", name)));
        }
        meta.setLore(lore);

        skull.setItemMeta(meta);
        return skull;
    }
}
