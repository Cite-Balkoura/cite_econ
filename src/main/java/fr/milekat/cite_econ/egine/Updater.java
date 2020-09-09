package fr.milekat.cite_econ.egine;

import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_core.core.obj.Profil;
import fr.milekat.cite_core.core.obj.Team;
import fr.milekat.cite_econ.MainEcon;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.upperlevel.spigot.book.BookUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.bukkit.inventory.meta.BookMeta.Generation.TATTERED;

public class Updater {
    public BukkitTask runTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = MainCore.getSQL().getConnection();
                try {
                    PreparedStatement q = connection.prepareStatement("SELECT a.`team_id` as team_id, " +
                            "`team_name` as team_name, `money` as team_money FROM `balkoura_team` a " +
                            "LEFT JOIN `balkoura_player` b ON b.`team_id` = a.`team_id` " +
                            "GROUP BY `team_name` HAVING COUNT(b.`name`) > 3 ORDER BY `money` DESC;");
                    q.execute();
                    HashMap<Integer, Team> classement = new HashMap<>();
                    while (q.getResultSet().next()) {
                        classement.put(q.getResultSet().getRow(), MainCore.teamHashMap.getOrDefault
                                (q.getResultSet().getInt("team_id"),null));
                    }
                    q.close();
                    updateSkins(classement, 5);
                    updateBook("Équipes", classement);
                    q = connection.prepareStatement("SELECT a.`team_id` as duo_id, " +
                            "`team_name` as duo_name, `money` as duo_money FROM `balkoura_team` a " +
                            "LEFT JOIN `balkoura_player` b ON b.`team_id` = a.`team_id` " +
                            "GROUP BY `team_name` HAVING COUNT(b.`name`) = 2 OR COUNT(b.`name`) = 3 " +
                            "ORDER BY `money` DESC;");
                    q.execute();
                    classement = new HashMap<>();
                    while (q.getResultSet().next()) {
                        classement.put(q.getResultSet().getRow(), MainCore.teamHashMap.getOrDefault
                                (q.getResultSet().getInt("duo_id"),null));
                    }
                    q.close();
                    updateSkins(classement, 8);
                    updateBook("Duo/Trio", classement);
                    q = connection.prepareStatement("SELECT a.`team_id` as solo_id, " +
                            "`team_name` as solo_name, `money` as solo_money FROM `balkoura_team` a " +
                            "LEFT JOIN `balkoura_player` b ON b.`team_id` = a.`team_id` " +
                            "GROUP BY `team_name` HAVING COUNT(b.`name`) = 1 ORDER BY `money` DESC;");
                    q.execute();
                    classement = new HashMap<>();
                    while (q.getResultSet().next()) {
                        classement.put(q.getResultSet().getRow(), MainCore.teamHashMap.getOrDefault
                                (q.getResultSet().getInt("solo_id"),null));
                    }
                    q.close();
                    updateSkins(classement, 11);
                    updateBook("Solo", classement);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                MainEcon.nextUpdate = DateUtils.addMinutes(new Date(), 5);
            }
        }.runTaskTimer(MainEcon.getInstance(),200L,6000L);
    }

    private Profil getRandomIntFromList(ArrayList<Profil> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    @SuppressWarnings("deprecation")
    private void updateSkins(HashMap<Integer, Team> classement, int firstNPCid) {
        for (int rank = 1; rank<=3; rank++) {
            NPC npc = CitizensAPI.getNPCRegistry().getById(firstNPCid + (rank-1));
            if (classement.getOrDefault(rank,null)==null) {
                npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "MileKat");
                updateSign(firstNPCid + (rank-1), null, rank);
            } else {
                Team team = classement.getOrDefault(rank,null);
                Profil profil = getRandomIntFromList(team.getMembers());
                try {
                    npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, profil.getName());
                } catch (NullPointerException ignore) {
                    npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "MileKat");
                }
                updateSign(firstNPCid + (rank-1), team, rank);
            }
        }
    }

    private void updateSign(int signnb, Team team, int rank) {
        Sign sign = (Sign) MainEcon.scoresSigns.get(signnb).getBlock().getState();
        if (team==null) {
            sign.setLine(1,"");
            sign.setLine(2,"");
            sign.update();
        } else {
            String display = "";
            switch (rank) {
                case 1:
                {
                    display = "§61er§c: ";
                    break;
                }
                case 2:
                {
                    display = "§62ème§c: ";
                    break;
                }
                case 3:
                {
                    display = "§63ème§c: ";
                    break;
                }
            }
            sign.setLine(1,display + "§f" + team.getName());
            sign.setLine(2,"§2Ém§c: §f" + team.getMoney());
            sign.update();
        }
    }

    /**
     *      Utilise Book-API https://github.com/upperlevel/book-api/wiki
     */
    private void updateBook(String type, HashMap<Integer, Team> classement) {
        ArrayList<BaseComponent[]> pages = new ArrayList<>();
        LuckPerms api = LuckPermsProvider.get();
        Collection<BaseComponent> title = new ArrayList<>();
        if (type.equalsIgnoreCase("équipes")) {
            title.add(BookUtil.TextBuilder.of("       Des Équipes").style().color(ChatColor.DARK_GREEN).build());
        } else if (type.equalsIgnoreCase("duo/trio")) {
            title.add(BookUtil.TextBuilder.of("    Des Duos/Trios").style().color(ChatColor.DARK_GREEN).build());
        } else {
            title.add(BookUtil.TextBuilder.of("   Des joueurs Solo").style().color(ChatColor.DARK_GREEN).build());
        }
        pages.add(new BookUtil.PageBuilder()
                .newLine()
                .add(BookUtil.TextBuilder.of("        " + MainCore.prefixCmd).build())
                .newLine().newLine().newLine()
                .add(BookUtil.TextBuilder.of("       -Annuaire-").style().color(ChatColor.DARK_PURPLE).build())
                .newLine()
                .add(title)
                .newLine().newLine().newLine().newLine().newLine().newLine().newLine()
                .add(BookUtil.TextBuilder.of("      §2§k~§r§3Par MileKat§2§k~").style().build())
                .build());
        for (Map.Entry<Integer, Team> team : classement.entrySet()) {
            Collection<BaseComponent> membres = new ArrayList<>();
            for (Profil profil : team.getValue().getMembers()) {
                try {
                    String prefix = "";
                    try {
                        User user = api.getUserManager().loadUser(profil.getUuid()).get();
                        if (user!=null) {
                            CachedMetaData cachedMetaData = user.getCachedData().getMetaData();
                            if (cachedMetaData.getPrimaryGroup()!=null) {
                                prefix = getRank(cachedMetaData.getPrimaryGroup());
                            }
                        }
                    } catch (NullPointerException ignore) {}
                    membres.add(BookUtil.TextBuilder.of("\n §3- §r" + prefix + profil.getName()).build());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            pages.add(new BookUtil.PageBuilder()
                    .newLine()
                    .add(new TextComponent("  §7§m----§7[ §6Top §c" + team.getKey() +" §7]§7§m----"))
                    .newLine().newLine().newLine()
                    .add(BookUtil.TextBuilder.of("§8Équipe§c: §3" + team.getValue().getName()).build())
                    .newLine()
                    .add(BookUtil.TextBuilder.of("§8Émeraudes§c: §2" + team.getValue().getMoney()).build())
                    .newLine().newLine()
                    .add(BookUtil.TextBuilder.of("§8Membres§c:").build())
                    .add(membres)
                    .build()
            );
        }
        MainEcon.books.put(type, BookUtil.writtenBook()
                .author("MileKat")
                .generation(TATTERED)
                .title("Classements des " + type)
                .pages(pages)
                .build()
        );
    }

    private String getRank(String rank) {
        switch (rank) {
            case "admin":
            {
                rank = "§c[A]";
                break;
            }
            case "modo":
            {
                rank = "§2[M]";
                break;
            }
            case "helper":
            {
                rank = "§9[H]";
                break;
            }
            case "parain":
            {
                rank = "§6[P]";
                break;
            }
            case "donateur":
            {
                rank = "§b[D]";
                break;
            }
            default:
            {
                rank = "";
                break;
            }
        }
        return rank;
    }
}
