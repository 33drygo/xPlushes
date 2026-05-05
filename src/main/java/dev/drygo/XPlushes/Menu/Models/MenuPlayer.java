package dev.drygo.XPlushes.Menu.Models;

import org.bukkit.entity.Player;

public class MenuPlayer {
    private Player player;
    private Menu menu;

    public MenuPlayer(Player player, Menu menu) {
        this.player = player;
        this.menu = menu;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
