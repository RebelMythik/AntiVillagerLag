package rebelmythik.antiVillagerLag.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antiVillagerLag.AntiVillagerLag;
import rebelmythik.antiVillagerLag.utils.ColorCode;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

import java.util.List;

import static rebelmythik.antiVillagerLag.utils.VillagerUtilities.restock;

public class RestockVillager {

    private static void restockMessage(long timeTillNextRestock, Player player, AntiVillagerLag plugin) {
        long totalsec = timeTillNextRestock / 20;
        long sec = totalsec % 60;
        long min = (totalsec - sec) / 60;
        String message = plugin.getConfig().getString("messages.next-restock");
        message = message.replaceAll("%avlrestockmin%", Long.toString(min));
        message = message.replaceAll("%avlrestocksec%", Long.toString(sec));
        player.sendMessage(VillagerUtilities.colorcodes.cm(message));
    }

    private static boolean handleRestock(Villager vil, long currDayTimeTick, AntiVillagerLag plugin) {

        long curTick = vil.getWorld().getFullTime();

        // get the tick time of the current day
        long currentDay = curTick - currDayTimeTick;

        // get last time the villager was restocked
        long vilTick = VillagerUtilities.getLastRestock(vil, plugin);

        // Check if the villager should be restocked and restock
        for (long restockTime : VillagerUtilities.restock_times) {
            long todayRestock = currentDay + restockTime;
            if (curTick >= todayRestock && vilTick < todayRestock) {
                restock(vil);
                VillagerUtilities.setLastRestock(vil, plugin);
                return true;
            }
        }
        return false;
    }

    public static void call(Villager vil, AntiVillagerLag plugin, Player player) {
        long currDayTimeTick = vil.getWorld().getTime();

        // Permission to Bypass restock cooldown
        if (player.hasPermission("avl.restockcooldown.bypass")) {
            restock(vil);
            VillagerUtilities.setLastRestock(vil, plugin);
            return;
        }

        // if successfully restocked, exit
        if (handleRestock(vil, currDayTimeTick, plugin))
            return;

        // check if he gets to see cool-down time
        if (player.hasPermission("avl.message.nextrestock")) {
            long timeTillNextRestock = Long.MAX_VALUE;
            long currentDay = vil.getWorld().getFullTime() - currDayTimeTick;

            for (long restockTime : VillagerUtilities.restock_times) {
                long restockTick = currentDay + restockTime;
                if (vil.getWorld().getFullTime() < restockTick) {
                    timeTillNextRestock = Math.min(timeTillNextRestock, restockTick - vil.getWorld().getFullTime());
                }
            }

            if (timeTillNextRestock == Long.MAX_VALUE) {
                timeTillNextRestock = (24000 + currentDay + VillagerUtilities.restock_times.get(0)) - vil.getWorld().getFullTime();
            }

            restockMessage(timeTillNextRestock, player, plugin);
        }
    }



}
