package fr.milekat.cite_econ.commands;

import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Team;
import fr.milekat.cite_libs.utils_tools.MojangNames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Money implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((args.length==0 || !sender.hasPermission("modo.econ.command.money.other")) && sender instanceof Player) {
            sender.sendMessage(MainCore.prefixCmd + "§6Votre équipe a §b" + MainCore.teamHashMap.get(
                    MainCore.profilHashMap.get(((Player)sender).getUniqueId()).getTeam()).getMoney()+ " §6émeraudes§c.");
        } else if (args.length==1 && sender.hasPermission("modo.econ.command.money.other")) {
            String target = MojangNames.getUuid(args[0]);
            if (target.equalsIgnoreCase("invalid name")) {
                sender.sendMessage(MainCore.prefixCmd + "§cJoueur introuvable.");
            } else {
                Team team = MainCore.teamHashMap.get(MainCore.profilHashMap.get(UUID.fromString(target)).getTeam());
                if (team == null) {
                    sender.sendMessage(MainCore.prefixCmd + "§cJoueur introuvable.");
                } else {
                    sender.sendMessage(MainCore.prefixCmd + "§6L'équipe §b" + team.getName() + " §6a §b" +
                            team.getMoney() + " §6émeraudes§c.");
                }
            }
        } else {
            sender.sendMessage(MainCore.prefixCmd + "§cLa commande est §b/" + label + " <Player>§c.");
        }
        return true;
    }
}
