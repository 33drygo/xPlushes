package dev.drygo.XPlushes.Commands;

import dev.drygo.XPlushes.Managers.ConfigManager;
import dev.drygo.XPlushes.Menu.Managers.MenuManager;
import dev.drygo.XPlushes.Menu.Models.Menu;
import dev.drygo.XPlushes.Models.UUIDCache;
import dev.drygo.XPlushes.Utils.ChatUtils;
import dev.drygo.XPlushes.Utils.LoadUtils;
import dev.drygo.XPlushes.XPlushes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class XPlushesCommand implements CommandExecutor {
    public static final String PERMISSION_USE = "xplushes.use";
    public static final String PERMISSION_ADMIN = "xplushes.admin";

    private final XPlushes plugin;

    public XPlushesCommand(XPlushes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            String action = args[0].toLowerCase(Locale.ROOT);
            switch (action) {
                case "reload" -> {
                    if (!hasAdmin(sender)) {
                        sender.sendMessage(ChatUtils.getMessage("commands.no_permission", null));
                        return true;
                    }
                    handleReload(sender);
                    return true;
                }
                case "help" -> {
                    List<String> helpMessage = ConfigManager.getMessageConfig().getStringList("commands.help");
                    for (String line : helpMessage) {
                        sender.sendMessage(ChatUtils.formatColor(line));
                    }
                    return true;
                }
                default -> {
                    if (!hasAdmin(sender)) {
                        sender.sendMessage(ChatUtils.getMessage("commands.no_permission", null));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target == null) {
                        sender.sendMessage(ChatUtils.getMessage("commands.open.player_not_found", null));
                        return true;
                    }
                    if (!UUIDCache.isLoaded()) {
                        sender.sendMessage(ChatUtils.getMessage("commands.open.cache_not_loaded", null));
                        return true;
                    }
                    MenuManager.openMenu(Menu.PLUSH, target);
                    if (!sender.equals(target)) {
                        sender.sendMessage(ChatUtils.getMessage("commands.open.opened_for", null)
                                .replace("%target%", target.getName()));
                    }
                    return true;
                }
            }
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtils.getMessage("commands.open.only_player", null));
            return true;
        }
        if (!hasUse(sender)) {
            sender.sendMessage(ChatUtils.getMessage("commands.no_permission", null));
            return true;
        }
        if (!UUIDCache.isLoaded()) {
            sender.sendMessage(ChatUtils.getMessage("commands.open.cache_not_loaded", null));
            return true;
        }
        MenuManager.openMenu(Menu.PLUSH, player);
        return true;
    }

    private boolean hasUse(CommandSender sender) {
        return sender.hasPermission(PERMISSION_USE) || sender.isOp();
    }

    private boolean hasAdmin(CommandSender sender) {
        return sender.hasPermission(PERMISSION_ADMIN) || sender.isOp();
    }

    private void handleReload(CommandSender sender) {
        Player target = (sender instanceof Player) ? (Player) sender : null;
        try {
            UUIDCache.clearCache();
            LoadUtils.loadFiles();
        } catch (Exception e) {
            plugin.getLogger().severe("Error reloading: " + e.getMessage());
            sender.sendMessage(ChatUtils.getMessage("commands.reload.error", target));
            return;
        }
        sender.sendMessage(ChatUtils.getMessage("commands.reload.success", target));
    }
}
