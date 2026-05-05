package dev.drygo.XPlushes.Menu.Managers;

import dev.drygo.XPlushes.Managers.PlushManager;
import dev.drygo.XPlushes.Menu.Models.MenuPlayer;
import dev.drygo.XPlushes.Utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlushMenuManager {
    public static void open(MenuPlayer menuPlayer) {
        Inventory inv = Bukkit.createInventory(null, 45, ChatUtils.getMessage("menu.plushes.title", null));

        List<String> plushes = PlushManager.getPlushes();
        int max = Math.min(plushes.size(), PlushManager.plushSlots.length);
        for (int i = 0; i < max; i++) {
            ItemStack plushItem = PlushManager.getInvPlush(i);
            if (plushItem != null) inv.setItem(PlushManager.plushSlots[i], plushItem);
        }

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName(ChatUtils.getMessage("menu.items.close", null));
            close.setItemMeta(closeMeta);
        }
        inv.setItem(40, close);

        ItemStack own = PlushManager.getInvOwnPlush(menuPlayer.getPlayer());
        inv.setItem(44, own);

        // Open first — this fires InventoryCloseEvent for whatever the player had
        // open before, which will deregister any previous MenuPlayer entry. Only
        // then do we register the new one so the close listener doesn't wipe it.
        menuPlayer.getPlayer().openInventory(inv);
        MenuManager.registerPlayer(menuPlayer);
    }
}
