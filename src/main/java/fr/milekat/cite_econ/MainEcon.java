package fr.milekat.cite_econ;

import fr.milekat.cite_econ.commands.Money;
import fr.milekat.cite_econ.commands.MoneyTab;
import fr.milekat.cite_econ.egine.Updater;
import fr.milekat.cite_econ.event.EconEvents;
import fr.mrmicky.fastinv.FastInvManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Date;
import java.util.HashMap;

public class MainEcon extends JavaPlugin {
    public static Date nextUpdate;
    public static HashMap<Integer, Location> scoresSigns = new HashMap<>();
    public static HashMap<String, Location> lecternsPos = new HashMap<>();
    public static HashMap<String, ItemStack> books = new HashMap<>();
    private BukkitTask updater;
    private static MainEcon mainEcon;

    @Override
    public void onEnable() {
        mainEcon = this;
        FastInvManager.register(this);
        setSignsPos();
        setLecternsPos();
        getServer().getPluginManager().registerEvents(new EconEvents(),this);
        // Commandes
        getCommand("money").setExecutor(new Money());
        //  Tab
        getCommand("money").setTabCompleter(new MoneyTab());
        updater = new Updater().runTask();
    }

    @Override
    public void onDisable() {
        updater.cancel();
    }

    public static MainEcon getInstance(){
        return mainEcon;
    }

    private void setSignsPos() {
        scoresSigns.put(5,new Location(Bukkit.getWorld("world"),-78,161,23));
        scoresSigns.put(6,new Location(Bukkit.getWorld("world"),-78,161,25));
        scoresSigns.put(7,new Location(Bukkit.getWorld("world"),-78,161,21));
        scoresSigns.put(8,new Location(Bukkit.getWorld("world"),-83,162,15));
        scoresSigns.put(9,new Location(Bukkit.getWorld("world"),-81,162,15));
        scoresSigns.put(10,new Location(Bukkit.getWorld("world"),-85,162,15));
        scoresSigns.put(11,new Location(Bukkit.getWorld("world"),-83,162,31));
        scoresSigns.put(12,new Location(Bukkit.getWorld("world"),-85,162,31));
        scoresSigns.put(13,new Location(Bukkit.getWorld("world"),-81,162,31));

    }

    private void setLecternsPos() {
        lecternsPos.put("team",new Location(Bukkit.getWorld("world"),-79,160,23));
        lecternsPos.put("duo",new Location(Bukkit.getWorld("world"),-83,161,16));
        lecternsPos.put("solo",new Location(Bukkit.getWorld("world"),-83,161,30));
    }
}
