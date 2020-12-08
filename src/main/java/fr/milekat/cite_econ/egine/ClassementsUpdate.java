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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.upperlevel.spigot.book.BookUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ClassementsUpdate {
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
                    HashMap<Integer, Team> classementTeam = new HashMap<>();
                    while (q.getResultSet().next()) {
                        classementTeam.put(q.getResultSet().getRow(), MainCore.teamHashMap.getOrDefault
                                (q.getResultSet().getInt("team_id"),null));
                    }
                    q.close();
                    Bukkit.getScheduler().runTask(MainEcon.getInstance(), ()->{
                        updateSkins(classementTeam, 5);
                        updateBook("Équipes", classementTeam);
                    });
                    q = connection.prepareStatement("SELECT a.`team_id` as duo_id, " +
                            "`team_name` as duo_name, `money` as duo_money FROM `balkoura_team` a " +
                            "LEFT JOIN `balkoura_player` b ON b.`team_id` = a.`team_id` " +
                            "GROUP BY `team_name` HAVING COUNT(b.`name`) = 2 OR COUNT(b.`name`) = 3 " +
                            "ORDER BY `money` DESC;");
                    q.execute();
                    HashMap<Integer, Team> classementDuo = new HashMap<>();
                    while (q.getResultSet().next()) {
                        classementDuo.put(q.getResultSet().getRow(), MainCore.teamHashMap.getOrDefault
                                (q.getResultSet().getInt("duo_id"),null));
                    }
                    q.close();
                    Bukkit.getScheduler().runTask(MainEcon.getInstance(), ()-> {
                                updateSkins(classementDuo, 8);
                                updateBook("Duo/Trio", classementDuo);
                    });
                    q = connection.prepareStatement("SELECT a.`team_id` as solo_id, " +
                            "`team_name` as solo_name, `money` as solo_money FROM `balkoura_team` a " +
                            "LEFT JOIN `balkoura_player` b ON b.`team_id` = a.`team_id` " +
                            "GROUP BY `team_name` HAVING COUNT(b.`name`) = 1 ORDER BY `money` DESC;");
                    q.execute();
                    HashMap<Integer, Team> classementSolo = new HashMap<>();
                    while (q.getResultSet().next()) {
                        classementSolo.put(q.getResultSet().getRow(), MainCore.teamHashMap.getOrDefault
                                (q.getResultSet().getInt("solo_id"),null));
                    }
                    q.close();
                    Bukkit.getScheduler().runTask(MainEcon.getInstance(), ()-> {
                        updateSkins(classementSolo, 11);
                        updateBook("Solo", classementSolo);
                    });
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (NullPointerException ignore) {
                    Bukkit.getLogger().warning("[MainEcon] update des scores skip, erreur inconnue.");
                }
                MainEcon.nextUpdate = DateUtils.addMinutes(new Date(), 5);
            }
        }.runTaskTimerAsynchronously(MainEcon.getInstance(),0L,6000L);
    }

    @SuppressWarnings("deprecation")
    private void updateSkins(HashMap<Integer, Team> classement, int firstNPCid) {
        for (int rank = 1; rank<=3; rank++) {
            NPC npc = CitizensAPI.getNPCRegistry().getById(firstNPCid + (rank-1));
            if (classement.getOrDefault(rank,null)==null) {
                npc.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA, "ewogICJ0aW1lc3RhbXAiIDogMTU5OTY3OTUwODk2NSwKICAicHJvZmlsZUlkIiA6ICI1MDQyOWM5YzY2MjY0OTZlOWFmMDA5NzMxMTJhZWNiMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaWxlS2F0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M1NDA1MzM4Mjk2MzNjNDYwYjcwNzk1YThiYmNhZDIwNDdjOGI0Y2UzZjQ4NWFhYjNjNjJjZTk4Y2U5YTJjM2MiCiAgICB9CiAgfQp9");
                npc.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA, "yS7kxlPY/a88v7Bo13MisLsYhuYuCtqqIKHpSzYCFX2AKKG+BFVqD2gjJLU3laehY2357o4LH2JNRQRPViLLOSBzxd2kiKacttHjkOUwSJWlVMfAYFnVEQrvpXiuy+nWsqIitdUK5VdnavkJm33yJVNXPcL35rALFO5w4OsmlJdSo6VJwdcSt6yt7Btlm/lif6lz5shnBtRESU3tMw9U48yzUrH3bq0j4q71Dyq6WCNPTknJ2UtVjDNISJhWeCYeWtvw/LxnBcBM/mkY3bVRtEsBahWoVl8clcm4LTkBh8kbvY2lCTxb3wP1T8BX23/+yKuvlaRm4mvjOALsPabcivoU7OdMwC4P495mzrpoccKsacMIdm8jN569R59rCKzBZf5aPDZj6NGmKQlYkGACyos7S2BEtSBTCRpTdMNv/PcyDLH9TvAdzZlNYAhmuks1/Ua2KI+nX6EFyAJ4IfRvjJmcrR2/PR6fghbwMsrUYNISkYAkiV6wCiLJWWk2xjTjo0tfMr4pTVpyJGPhxHM9VYjKSeVliSMtO2R5QdzWzX4C/NAts7LpIjBwCwm1rxG2xz7a5BsDL/cHCwLZBAqI3di+4PlqyQIr1ezlxBhIhgcX8i4ow7vTeozMLyrmTJRs86fP3lvtq7NPgsUtitW4z9+CkcGwI7axbXfVgJuH6Wg=");
                updateSign(firstNPCid + (rank-1), null, rank);
            } else {
                Team team = classement.getOrDefault(rank,null);
                try {
                    npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, getRandomIntFromList(team.getMembers()).getName());
                } catch (NullPointerException ignore) {
                    npc.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA, "ewogICJ0aW1lc3RhbXAiIDogMTU5OTY3OTUwODk2NSwKICAicHJvZmlsZUlkIiA6ICI1MDQyOWM5YzY2MjY0OTZlOWFmMDA5NzMxMTJhZWNiMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaWxlS2F0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M1NDA1MzM4Mjk2MzNjNDYwYjcwNzk1YThiYmNhZDIwNDdjOGI0Y2UzZjQ4NWFhYjNjNjJjZTk4Y2U5YTJjM2MiCiAgICB9CiAgfQp9");
                    npc.data().setPersistent(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA, "yS7kxlPY/a88v7Bo13MisLsYhuYuCtqqIKHpSzYCFX2AKKG+BFVqD2gjJLU3laehY2357o4LH2JNRQRPViLLOSBzxd2kiKacttHjkOUwSJWlVMfAYFnVEQrvpXiuy+nWsqIitdUK5VdnavkJm33yJVNXPcL35rALFO5w4OsmlJdSo6VJwdcSt6yt7Btlm/lif6lz5shnBtRESU3tMw9U48yzUrH3bq0j4q71Dyq6WCNPTknJ2UtVjDNISJhWeCYeWtvw/LxnBcBM/mkY3bVRtEsBahWoVl8clcm4LTkBh8kbvY2lCTxb3wP1T8BX23/+yKuvlaRm4mvjOALsPabcivoU7OdMwC4P495mzrpoccKsacMIdm8jN569R59rCKzBZf5aPDZj6NGmKQlYkGACyos7S2BEtSBTCRpTdMNv/PcyDLH9TvAdzZlNYAhmuks1/Ua2KI+nX6EFyAJ4IfRvjJmcrR2/PR6fghbwMsrUYNISkYAkiV6wCiLJWWk2xjTjo0tfMr4pTVpyJGPhxHM9VYjKSeVliSMtO2R5QdzWzX4C/NAts7LpIjBwCwm1rxG2xz7a5BsDL/cHCwLZBAqI3di+4PlqyQIr1ezlxBhIhgcX8i4ow7vTeozMLyrmTJRs86fP3lvtq7NPgsUtitW4z9+CkcGwI7axbXfVgJuH6Wg=");
                }
                updateSign(firstNPCid + (rank-1), team, rank);
            }
        }
    }

    /**
     *      Récupère un profil au hasard dans la liste des membres
     */
    private Profil getRandomIntFromList(ArrayList<Profil> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    /**
     *      Mise à jour des panneaux
     */
    private void updateSign(int signnb, Team team, int rank) {
        Sign sign = (Sign) MainEcon.scoresSigns.get(signnb).getBlock().getState();
        if (team==null) {
            sign.setLine(1,"");
            sign.setLine(2,"");
        } else {
            String[] teamName = team.getName().split("(?<=\\G.{11}.)");
            sign.setLine(1,/*display + */"§6" + rank + ".§c:§f" + teamName[0]);
            sign.setLine(2,"§2Ém§c: §f" + team.getMoney());
        }
        sign.update();
    }

    /**
     *      Utilise Book-API https://github.com/upperlevel/book-api/wiki
     */
    private void updateBook(String type, HashMap<Integer, Team> classement) {
        ArrayList<BaseComponent[]> pages = new ArrayList<>();
        LuckPerms api = LuckPermsProvider.get();
        // Page 1
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
        // Pages des équipes
        for (Map.Entry<Integer, Team> team : classement.entrySet()) {
            Collection<BaseComponent> membres = new ArrayList<>();
            for (Profil profil : team.getValue().getMembers()) {
                try {
                    String prefix = "";
                    User user = api.getUserManager().loadUser(profil.getUuid()).get();
                    if (user!=null) {
                        CachedMetaData cachedMetaData = user.getCachedData().getMetaData();
                        if (cachedMetaData.getPrimaryGroup()!=null) {
                            prefix = getRank(cachedMetaData.getPrimaryGroup());
                        }
                    }
                    membres.add(BookUtil.TextBuilder.of("\n§3-§r" + prefix + profil.getName()).build());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            String[] teamName = team.getValue().getName().split("(?<=\\G.{13}.)");
            pages.add(new BookUtil.PageBuilder()
                    .newLine()
                    .add(new TextComponent("  §7§m----§7[ §6Top §c" + team.getKey() +" §7]§7§m----"))
                    .newLine().newLine().newLine()
                    .add(BookUtil.TextBuilder.of("§8Nom§c: §3" + teamName[0]).build())
                    .newLine()
                    .add(BookUtil.TextBuilder.of("§8Émeraudes§c: §2" + team.getValue().getMoney()).build())
                    .newLine().newLine()
                    .add(BookUtil.TextBuilder.of("§8Membres§c:").build())
                    .add(membres)
                    .build()
            );
        }
        // Création du livre
        MainEcon.books.put(type, BookUtil.writtenBook()
                .author("MileKat")
                .generation(BookMeta.Generation.TATTERED)
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
