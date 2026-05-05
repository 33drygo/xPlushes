package dev.drygo.XPlushes.API;

import dev.drygo.XPlushes.Managers.PlushManager;
import dev.drygo.XPlushes.Menu.Managers.MenuManager;
import dev.drygo.XPlushes.Menu.Models.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class XPlushesAPI {

    public static List<String> getPlushes() {
        return PlushManager.getPlushes();
    }

    public static int getPlushCount() {
        return PlushManager.size();
    }

    public static boolean isPlush(String name) {
        return indexOfPlush(name) >= 0;
    }

    public static int indexOfPlush(String name) {
        if (name == null) return -1;
        List<String> list = PlushManager.getPlushes();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    public static ItemStack getPlushItem(String name) {
        int idx = indexOfPlush(name);
        return idx == -1 ? null : PlushManager.getPlush(idx);
    }

    public static ItemStack getPlushItem(int index) {
        return PlushManager.getPlush(index);
    }

    public static ItemStack getOwnPlushItem(Player player) {
        return player == null ? null : PlushManager.getInvOwnPlush(player);
    }
    public static boolean givePlush(Player player, String name) {
        if (player == null) return false;
        return giveOrDrop(player, getPlushItem(name));
    }

    public static boolean givePlush(Player player, int index) {
        if (player == null) return false;
        return giveOrDrop(player, getPlushItem(index));
    }

    public static void openMenu(Player player) {
        if (player == null) return;
        MenuManager.openMenu(Menu.PLUSH, player);
    }

    public static boolean hasMenuOpen(Player player) {
        return player != null && MenuManager.getMenuPlayer(player) != null;
    }

    private static boolean giveOrDrop(Player player, ItemStack item) {
        if (item == null) return false;
        Map<Integer, ItemStack> leftover = player.getInventory().addItem(item);
        if (leftover.isEmpty()) return true;
        for (ItemStack rest : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), rest);
        }
        return false;
    }
}
