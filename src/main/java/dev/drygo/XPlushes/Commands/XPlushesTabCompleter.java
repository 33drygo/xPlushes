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
        boolean isAdmin = sender.hasPermission(XPlushesCommand.PERMISSION_ADMIN) || sender.isOp();

        if (args.length == 1) {
            String input = args[0].toLowerCase(Locale.ROOT);
            List<String> options = new ArrayList<>();
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

        if (args.length == 2 && isAdmin && Bukkit.getPlayerExact(args[0]) != null) {
            String input = args[1].toLowerCase(Locale.ROOT);
            List<String> completions = new ArrayList<>();
            for (String flag : new String[]{"--silent", "-s"}) {
                if (flag.toLowerCase(Locale.ROOT).startsWith(input)) completions.add(flag);
            }
            return completions;
        }

        return Collections.emptyList();
    }
}
