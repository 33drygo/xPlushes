package dev.drygo.XPlushes.Menu.Managers;

import dev.drygo.XPlushes.Managers.PlushManager;
import dev.drygo.XPlushes.Menu.Models.Menu;
import dev.drygo.XPlushes.Menu.Models.MenuPlayer;
import dev.drygo.XPlushes.Utils.ChatUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MenuManager {
    static final Map<UUID, MenuPlayer> players = new HashMap<>();

    public static MenuPlayer getMenuPlayer(Player p) {
        if (p == null) return null;
        return players.get(p.getUniqueId());
    }

    public static void registerPlayer(MenuPlayer m) {
        players.put(m.getPlayer().getUniqueId(), m);
    }

    public static void removePlayer(Player p) {
        if (p == null) return;
        players.remove(p.getUniqueId());
    }

    public static Collection<MenuPlayer> getPlayers() {
        return players.values();
    }

    public static void menuClick(MenuPlayer m, int slot) {
        switch (m.getMenu()) {
            case PLUSH -> {
                Player player = m.getPlayer();

                if (slot == 40) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 2f, 0.5f);
                    return;
                }

                if (slot == 44) {
                    ItemStack own = PlushManager.getPlush(player.getName());
                    if (own == null) return;
                    giveOrDrop(player, own);
                    player.sendMessage(ChatUtils.getMessage("broadcast.buy.own", null));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 2f, 1.75f);
                    openMenu(m.getMenu(), player);
                    return;
                }

                int plushIndex = -1;
                for (int i = 0; i < PlushManager.plushSlots.length; i++) {
                    if (PlushManager.plushSlots[i] == slot) {
                        plushIndex = i;
                        break;
                    }
                }
                if (plushIndex == -1) return;

                List<String> plushes = PlushManager.getPlushes();
                if (plushIndex >= plushes.size()) return;

                String plushName = plushes.get(plushIndex);
                ItemStack item = PlushManager.getPlush(plushIndex);
                if (item == null) return;

                giveOrDrop(player, item);
                player.sendMessage(ChatUtils.getMessage("broadcast.buy.plush", null)
                        .replace("%name%", plushName));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 2f, 1.75f);
                openMenu(m.getMenu(), player);
            }
        }
    }

    public static void openMenu(Menu menu, Player p) {
        switch (menu) {
            case PLUSH -> PlushMenuManager.open(new MenuPlayer(p, menu));
        }
    }

    private static void giveOrDrop(Player player, ItemStack item) {
        Map<Integer, ItemStack> leftover = player.getInventory().addItem(item);
        if (leftover.isEmpty()) return;
        for (ItemStack rest : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), rest);
        }
        player.sendMessage(ChatUtils.getMessage("broadcast.buy.inventory_full", null));
    }
}
