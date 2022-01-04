package me.rebelmythik.antivillagerlag.utils;

import com.google.common.util.concurrent.AbstractScheduledService;
import jdk.javadoc.internal.doclint.HtmlTag;
import me.rebelmythik.antivillagerlag.AntiVillagerLag;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class timer implements Listener {
    Plugin plugin;
    int seconds;
    public timer(Plugin plugin) {
        this.plugin = plugin;
        seconds = plugin.getConfig().getInt("block-check");
    }
    
    public void createScheduler() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage("The Schedule is working.");
                List<World> worlds = plugin.getServer().getWorlds();
                for (World w : worlds) {
                    List<Entity> entities = w.getEntities();
                    Bukkit.broadcastMessage("Got worlds");
                    for (Entity e : entities) {
                        if (e.getType() == EntityType.VILLAGER) {
                            Bukkit.broadcastMessage("Found a villager");
                            int x = (int) e.getLocation().getX();
                            int y = (int) e.getLocation().getY();
                            int z = (int) e.getLocation().getZ();
                            Block blk = w.getBlockAt(x, y - 1, z);
                            if (blk.getType() == Material.getMaterial(plugin.getConfig().getString("disable-block"))) {
                                Bukkit.broadcastMessage("Found the correct block under a villager");
                                //make brain no work
                            }
                        }
                    }
                }
            }
        }, seconds * 20);
    }
}
