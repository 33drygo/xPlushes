package dev.drygo.XPlushes.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class XPlushesTabCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return Collections.emptyList();

        String input = args[0].toLowerCase(Locale.ROOT);
        List<String> options = new ArrayList<>();

        boolean isAdmin = sender.hasPermission(XPlushesCommand.PERMISSION_ADMIN) || sender.isOp();
        if (isAdmin) {
            options.add("reload");
            for (Player p : Bukkit.getOnlinePlayers()) {
                options.add(p.getName());
            }
        }
        options.add("help");

        List<String> completions = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase(Locale.ROOT).startsWith(input)) {
                completions.add(option);
            }
        }
        return completions;
    }
}
