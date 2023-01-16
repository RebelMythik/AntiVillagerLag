package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.List;

import static rebelmythik.antivillagerlag.utils.VillagerUtilities.replaceText;
import static rebelmythik.antivillagerlag.utils.VillagerUtilities.restock;

public class RestockVillager {
    private final AntiVillagerLag plugin;
    private final List<Long> restockTimes;
    ColorCode colorCodes = new ColorCode();

    public RestockVillager(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.restockTimes = plugin.getConfig().getLongList("RestockTimes.times");
    }

    public void restockMessage(long timeTillNextRestock, Player player) {

        long totalsec = timeTillNextRestock / 20;
        long sec = totalsec % 60;
        long min = (totalsec - sec) / 60;
        String message = plugin.getConfig().getString("messages.next-restock");

        if (message.contains("%avlrestockmin%")) {
            message = replaceText(message, "%avlrestockmin%", Long.toString(min));
        }
        message = replaceText(message, "%avlrestocksec%", Long.toString(sec));
        player.sendMessage(colorCodes.cm(message));
    }

    public boolean handleRestock(Villager vil, long currDayTimeTick, AntiVillagerLag plugin) {

        long curTick = vil.getWorld().getFullTime();

        // get the tick time of the current day
        long currentDay = curTick - currDayTimeTick;

        // get last time the villager was restocked
        long vilTick = VillagerUtilities.getTime(vil, plugin);

        // Check if the villager should be restocked and restock
        for (long restockTime : restockTimes) {
            long todayRestock = currentDay + restockTime;
            if (curTick >= todayRestock && vilTick < todayRestock) {
                restock(vil);
                VillagerUtilities.setNewTime(vil, plugin);
                return true;
            }
        }
        return false;
    }

    public void call(Villager vil, Player player) {
        long currDayTimeTick = vil.getWorld().getTime();

        // Permission to Bypass restock cooldown
        if (player.hasPermission("avl.restockcooldown.bypass")) {
            restock(vil);
            VillagerUtilities.setNewTime(vil, plugin);
            return;
        }

        // If he doesn't have a time, restock
        if (!VillagerUtilities.hasTime(vil, plugin)) {
            restock(vil);
            VillagerUtilities.setNewTime(vil, plugin);
            return;
        }
        // if successfully restocked, exit
        if (handleRestock(vil, currDayTimeTick, plugin))
            return;

        // check if he gets to see cool-down time
        if (player.hasPermission("avl.message.nextrestock")) {
            long timeTillNextRestock = Long.MAX_VALUE;
            long currentDay = vil.getWorld().getFullTime() - currDayTimeTick;

            for (long restockTime : restockTimes) {
                long restockTick = currentDay + restockTime;
                if (vil.getWorld().getFullTime() < restockTick) {
                    timeTillNextRestock = Math.min(timeTillNextRestock, restockTick - vil.getWorld().getFullTime());
                }
            }

            if (timeTillNextRestock == Long.MAX_VALUE) {
                timeTillNextRestock = (24000 - currentDay) + restockTimes.get(0);
            }

            restockMessage(timeTillNextRestock, player);
        }
    }
}
