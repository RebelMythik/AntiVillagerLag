package rebelmythik.antivillagerlag.autoevents;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;
import rebelmythik.antivillagerlag.AntiVillagerLag;

import java.util.List;

public class VillagerScanner  {

    private AntiVillagerLag plugin; // Reference to the main plugin class
    private int taskId; // Task ID of the running task
    List<String> blocksThatDisable;

    public VillagerScanner(AntiVillagerLag plugin) {
        this.plugin = plugin;
        blocksThatDisable = plugin.getConfig().getStringList("BlocksThatDisable");
    }

    public void startTaskTimer() {
        // Use BukkitRunnable to create a repeating task that runs every 30 seconds
        int simulationDistance = Bukkit.getServer().getSimulationDistance();
        int scanRadius = simulationDistance * 16;
        taskId = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    for (Villager villager : player.getWorld().getEntitiesByClass(Villager.class)) {
                        if (villager.getLocation().distance(player.getLocation()) <= scanRadius) {
                            if (blocksThatDisable.contains(villager.getLocation().add(0, -1, 0).getBlock().getType().name())) {
                                villager.setAware(false); // Set the villager's aware attribute to false
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 600).getTaskId(); // 0 ticks initial delay, 600 ticks (30 seconds) between each run
    }

    public void cancelTaskTimer() {
        // Cancel the task timer if it's running
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }


}
