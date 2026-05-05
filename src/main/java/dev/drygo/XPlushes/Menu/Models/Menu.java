package dev.drygo.XPlushes.Menu.Models;

public enum Menu {
    PLUSH;

    public static boolean validMenu(String name) {
        if (name == null) return false;
        try {
            Menu.valueOf(name.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
