package rebelmythik.antiVillagerLag.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class SchedulerUtils {
    
    private static boolean isFolia;
    
    static {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
    }
    
    public static boolean isFolia() {
        return isFolia;
    }
    
    public static void runAsync(JavaPlugin plugin, Runnable runnable) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(plugin, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }
    
    public static void runSync(JavaPlugin plugin, Runnable runnable) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().run(plugin, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }
    
    public static void runAtEntity(Entity entity, JavaPlugin plugin, Runnable runnable) {
        if (isFolia) {
            entity.getScheduler().run(plugin, task -> runnable.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }
    
    public static void runAtLocation(Location location, JavaPlugin plugin, Runnable runnable) {
        if (isFolia) {
            Bukkit.getRegionScheduler().run(plugin, location, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }
    
    public static void runAtEntityLater(Entity entity, JavaPlugin plugin, Runnable runnable, long delay) {
        if (isFolia) {
            entity.getScheduler().runDelayed(plugin, task -> runnable.run(), null, delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }
    
    public static void runAtLocationLater(Location location, JavaPlugin plugin, Runnable runnable, long delay) {
        if (isFolia) {
            Bukkit.getRegionScheduler().runDelayed(plugin, location, task -> runnable.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }
}
