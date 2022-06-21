package rebelmythik.antivillagerlag.events;

import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import rebelmythik.antivillagerlag.AntiVillagerLag;

public class TradeRestocks implements Listener {

    public AntiVillagerLag plugin;

    public TradeRestocks(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }


    @Override
    public void run() {
        World world = plugin.getServer().getWorld("world");
        long time = world.getTime();


    }



}
