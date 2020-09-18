package fr.milekat.cite_econ.commands;

import fr.milekat.cite_core.MainCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class MoneyTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            return MainCore.getPlayersList(args[0]);
        }
        return null;
    }
}
