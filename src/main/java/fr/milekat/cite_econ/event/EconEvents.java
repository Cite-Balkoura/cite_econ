package fr.milekat.cite_econ.event;

import fr.milekat.cite_core.MainCore;
import fr.milekat.cite_econ.MainEcon;
import fr.milekat.cite_econ.utils.GuichetGUI;
import fr.milekat.cite_libs.utils_tools.DateMilekat;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.upperlevel.spigot.book.BookUtil;

public class EconEvents implements Listener {
    private final Location HORLOGEBANK = new Location(Bukkit.getWorld("world"),-95,162,27);

    @EventHandler (priority = EventPriority.LOW)
    public void onItemTurnItemFrameBy(PlayerInteractEntityEvent event){
        if (event.getRightClicked().getLocation().getBlock().getLocation().equals(HORLOGEBANK)
                && event.getRightClicked() instanceof ItemFrame) {
            event.setCancelled(true);
            if (MainEcon.nextUpdate==null) {
                event.getPlayer().sendMessage(MainCore.prefixCmd + "§6Prochains calculs dans§c: §b5m§c.");
            } else {
                event.getPlayer().sendMessage(MainCore.prefixCmd + "§6Prochains calculs dans§c: §b"
                        + DateMilekat.reamingToStrig(MainEcon.nextUpdate) + "§c.");
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onClick(PlayerInteractEvent event) {
        if (event.getClickedBlock()==null) return;
        Location location = event.getClickedBlock().getLocation();
        if (MainEcon.lecternsPos.get("team").equals(location)) {
            event.setCancelled(true);
            BookUtil.openPlayer(event.getPlayer(), MainEcon.books.get("Équipes"));
        } else if (MainEcon.lecternsPos.get("duo").equals(location)) {
            event.setCancelled(true);
            BookUtil.openPlayer(event.getPlayer(), MainEcon.books.get("Duo/Trio"));
        } else if (MainEcon.lecternsPos.get("solo").equals(location)) {
            event.setCancelled(true);
            BookUtil.openPlayer(event.getPlayer(), MainEcon.books.get("Solo"));
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onGuichetClick(NPCRightClickEvent event) {
        if (event.getNPC().getId() != 15 && event.getNPC().getId() != 16) return;
        new GuichetGUI().open(event.getClicker());
        event.setCancelled(true);
    }
}
