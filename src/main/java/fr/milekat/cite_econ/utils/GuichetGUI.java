package fr.milekat.cite_econ.utils;

import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Team;
import fr.milekat.cite_libs.utils_tools.DateMilekat;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuichetGUI extends FastInv {
    private final ItemStack emeraude = new ItemStack(Material.EMERALD);
    private final ItemStack block = new ItemStack(Material.EMERALD_BLOCK);

    public GuichetGUI() {
        super(36, ChatColor.DARK_AQUA + "[Banque] Effectuer un dépot");
        // Ajout des glass panes vertes.
        for (int i=0; i<35; i++) {
            setItem(i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(ChatColor.DARK_GREEN + "Dépot").build());
        }
        //  Émeraudes
        setItem(10, new ItemBuilder(Material.EMERALD).name(ChatColor.DARK_GREEN + "Déposer 1")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(emeraude,1)) {
                process((Player) e.getWhoClicked(),1,Material.EMERALD, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(11, new ItemBuilder(Material.EMERALD).amount(5).name(ChatColor.DARK_GREEN + "Déposer 5")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(emeraude,5)) {
                process((Player) e.getWhoClicked(),5,Material.EMERALD, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(12, new ItemBuilder(Material.EMERALD).amount(10).name(ChatColor.DARK_GREEN + "Déposer 10")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(emeraude,10)) {
                process((Player) e.getWhoClicked(),10,Material.EMERALD, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(19, new ItemBuilder(Material.EMERALD).amount(16).name(ChatColor.DARK_GREEN + "Déposer 16")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(emeraude,16)) {
                process((Player) e.getWhoClicked(),16,Material.EMERALD, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(20, new ItemBuilder(Material.EMERALD).amount(32).name(ChatColor.DARK_GREEN + "Déposer 32")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(emeraude,32)) {
                process((Player) e.getWhoClicked(),32,Material.EMERALD, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(21, new ItemBuilder(Material.EMERALD).amount(64).name(ChatColor.DARK_GREEN + "Déposer 64")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(emeraude,64)) {
                process((Player) e.getWhoClicked(),64,Material.EMERALD, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        //  Blocks
        setItem(14, new ItemBuilder(Material.EMERALD_BLOCK).amount(1).name(ChatColor.DARK_GREEN + "Déposer 1")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(block,1)) {
                process((Player) e.getWhoClicked(),1,Material.EMERALD_BLOCK, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(15, new ItemBuilder(Material.EMERALD_BLOCK).amount(5).name(ChatColor.DARK_GREEN + "Déposer 5")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(block,5)) {
                process((Player) e.getWhoClicked(),5,Material.EMERALD_BLOCK, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(16, new ItemBuilder(Material.EMERALD_BLOCK).amount(10).name(ChatColor.DARK_GREEN + "Déposer 10")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(block,10)) {
                process((Player) e.getWhoClicked(),10,Material.EMERALD_BLOCK, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(23, new ItemBuilder(Material.EMERALD_BLOCK).amount(16).name(ChatColor.DARK_GREEN + "Déposer 16")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(block,16)) {
                process((Player) e.getWhoClicked(),16,Material.EMERALD_BLOCK, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(24, new ItemBuilder(Material.EMERALD_BLOCK).amount(32).name(ChatColor.DARK_GREEN + "Déposer 32")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(block,32)) {
                process((Player) e.getWhoClicked(),32,Material.EMERALD_BLOCK, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(25, new ItemBuilder(Material.EMERALD_BLOCK).amount(64).name(ChatColor.DARK_GREEN + "Déposer 64")
                .build(), e -> {
            if (e.getWhoClicked().getInventory().containsAtLeast(block,64)) {
                process((Player) e.getWhoClicked(),64,Material.EMERALD_BLOCK, true);
            } else {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§bVous n'avez pas assez d'émeraude.");
            }
        });
        setItem(31, new ItemBuilder(Material.HOPPER_MINECART).name(ChatColor.DARK_GREEN + "Tout déposer").build(), e -> {
            int e_amount = getAmount((Player) e.getWhoClicked(),Material.EMERALD);
            int b_amount = getAmount((Player) e.getWhoClicked(),Material.EMERALD_BLOCK);
            if (e_amount>0) {
                process((Player) e.getWhoClicked(),e_amount,Material.EMERALD, false);
            }
            if (b_amount>0) {
                process((Player) e.getWhoClicked(),b_amount,Material.EMERALD_BLOCK, true);
            }
            if (e_amount==0 && b_amount==0) {
                e.getWhoClicked().sendMessage(MainCore.prefixCmd + "§cAucune émeraude trouvée.");
            }
        });
        setItem(35, new ItemBuilder(Material.BARRIER).name(ChatColor.RED + "Sortir du compte").build(), e -> {
            e.getWhoClicked().closeInventory();
        });
    }

    /**
     *      Effectuer la récupération des émeraudes, et ajouter les émeraudes dans le SQL
     */
    private void process(Player player, int amount, Material material, boolean msg) {
        ItemStack remove = new ItemStack(material);
        remove.setAmount(amount);
        player.getInventory().removeItem(remove);
        Team team = MainCore.teamHashMap.get(MainCore.profilHashMap.get(player.getUniqueId()).getTeam());
        Connection connection = MainCore.getSQL().getConnection();
        try {
            PreparedStatement q = connection.prepareStatement(
                    "SELECT `money` FROM `" + MainCore.SQLPREFIX + "team` WHERE `team_id` = ?;" +
                    "UPDATE `" + MainCore.SQLPREFIX + "team` SET `money` = `money` + ? WHERE `team_id` = ?;" +
                    "INSERT INTO `" + MainCore.SQLPREFIX + "transactions`(`player_id`, `amount`, `date`) VALUES " +
                    "((SELECT `player_id` FROM `" + MainCore.SQLPREFIX + "player` WHERE `uuid` = '" +
                    player.getUniqueId().toString() + "'),?,?);");
            if (material.equals(Material.EMERALD_BLOCK)) amount = amount * 9;
            q.setInt(1, team.getId());
            q.setInt(2, amount);
            q.setInt(3, team.getId());
            q.setInt(4, amount);
            q.setString(5, DateMilekat.setDateNow());
            q.execute();
            q.getResultSet().last();
            team.setMoney(q.getResultSet().getInt("money") + amount);
            q.close();
            if (msg) player.sendMessage(MainCore.prefixCmd + "§6Ton équipe à §b" + team.getMoney() + " §bémeraudes§c.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *      Mini lib pour récupérer le nombre de X items dans l'inventaire.
     */
    private int getAmount(Player player, Material material) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int has = 0;
        for (ItemStack item : items)
        {
            if ((item != null) && (item.getType() == material) && (item.getAmount() > 0))
            {
                has += item.getAmount();
            }
        }
        return has;
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
