package dev.drygo.XPlushes.Menu.Listeners;

import dev.drygo.XPlushes.Menu.Managers.MenuManager;
import dev.drygo.XPlushes.Menu.Models.MenuPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity who = event.getWhoClicked();
        if (!(who instanceof Player p)) return;

        MenuPlayer m = MenuManager.getMenuPlayer(p);
        if (m == null) return;

        // Block any cross-inventory action (shift-click, hotbar-swap, collect-to-cursor, etc.)
        // even if the click happened in the player's own inventory.
        InventoryAction action = event.getAction();
        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY
                || action == InventoryAction.COLLECT_TO_CURSOR
                || action == InventoryAction.HOTBAR_SWAP) {
            event.setCancelled(true);
            return;
        }

        Inventory clicked = event.getClickedInventory();
        Inventory top = event.getView().getTopInventory();
        if (clicked == null) return;

        // If they clicked their own inventory, just ignore (don't drive menu logic).
        if (clicked != top) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null) return;

        MenuManager.menuClick(m, event.getSlot());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player p)) return;
        if (MenuManager.getMenuPlayer(p) == null) return;

        Inventory top = event.getView().getTopInventory();
        int topSize = top.getSize();
        for (int slot : event.getRawSlots()) {
            if (slot < topSize) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player p)) return;
        MenuManager.removePlayer(p);
    }
}
