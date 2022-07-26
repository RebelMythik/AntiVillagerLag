package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;


import static rebelmythik.antivillagerlag.utils.VillagerUtilities.replaceText;
import static rebelmythik.antivillagerlag.utils.VillagerUtilities.restock;

public class RestockVillager {
    public AntiVillagerLag plugin;
    public long restock1;
    public long restock2;
    ColorCode colorCodes = new ColorCode();

    public RestockVillager(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.restock1 = plugin.getConfig().getLong("RestockTimes.time1");
        this.restock2 = plugin.getConfig().getLong("RestockTimes.time2");
    }

    public void call(Villager vil, Player player) {

        long curTick = vil.getWorld().getFullTime();
        long currDayTimeTick = vil.getWorld().getTime();
        long currentDayTick = curTick - currDayTimeTick;
        long todayRestock1 = currentDayTick + restock1;
        long todayRestock2 = currentDayTick + restock2;


        //Permission to Bypass restock cooldown
        if (player.hasPermission("avl.restockcooldown.bypass")) {
            restock(vil);
            VillagerUtilities.setNewTime(vil, plugin);
            return;
        }

        //If he doesn't have a time, restock
        if (!VillagerUtilities.hasTime(vil, plugin)) {
            restock(vil);
            VillagerUtilities.setNewTime(vil, plugin);
            return;
        }

        long vilTick = VillagerUtilities.getTime(vil, plugin);
        //Check if he should be restocked
        if (curTick >= todayRestock1 && vilTick < todayRestock1) {
            restock(vil);
            VillagerUtilities.setNewTime(vil, plugin);
            return;
        } else if (curTick >= todayRestock2 && vilTick < todayRestock2) {
            restock(vil);
            VillagerUtilities.setNewTime(vil, plugin);
            return;
        }

        //check if he gets to see cool-down time
        if (player.hasPermission("avl.message.nextrestock")) {
            long timeTillNextRestock;

            if (currDayTimeTick >= restock2) {
                timeTillNextRestock = (24000 - currDayTimeTick) + restock1;
            } else if (currDayTimeTick >= restock1) {
                timeTillNextRestock = restock2 - currDayTimeTick;
            } else {
                timeTillNextRestock = restock1 - currDayTimeTick;
            }
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
    }
}
