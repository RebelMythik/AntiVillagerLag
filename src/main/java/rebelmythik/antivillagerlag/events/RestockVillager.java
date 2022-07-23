package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.MerchantRecipe;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.List;

import static rebelmythik.antivillagerlag.utils.VillagerUtilities.replaceText;

public class RestockVillager implements Listener {
    public AntiVillagerLag plugin;
    public long restock1;
    public long restock2;
    ColorCode colorCodes = new ColorCode();

    public RestockVillager(AntiVillagerLag plugin) {
        this.plugin = plugin;
        restock1 = plugin.getConfig().getLong("RestockTimes.time1");
        restock2 = plugin.getConfig().getLong("RestockTimes.time2");
    }

    public void restock(Villager v) {
        List<MerchantRecipe> recipes = v.getRecipes();
        for (MerchantRecipe r: recipes) {
            r.setUses(0);
        }
    }
    @EventHandler
    public void clickVillager(PlayerInteractEntityEvent e) {
        if (!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;
        Villager vil = (Villager) e.getRightClicked();
        long curTick = vil.getWorld().getFullTime();
        long currDayTimeTick = vil.getWorld().getTime();
        long currentDayTick = curTick - currDayTimeTick;
        long todayRestock1 = currentDayTick + restock1;
        long todayRestock2 = currentDayTick + restock2;
        Player player = e.getPlayer();

        // Check if villager is disabled
        if (!VillagerUtilities.isDisabled(vil, plugin)) return;

        //Permission to Bypass restock cooldown
        if (player.hasPermission("avl.restockcooldown.bypass")) {
            restock(vil);
            VillagerUtilities.setNewTime(plugin, vil);
            return;
        }

        //If he doesn't have a time, restock
        if (!VillagerUtilities.hasTime(plugin, vil)) {
            restock(vil);
            VillagerUtilities.setNewTime(plugin, vil);
            return;
        }

        long vilTick = VillagerUtilities.getTime(plugin, vil);
        //Check if he should be restocked
        if (curTick >= todayRestock1 && vilTick < todayRestock1) {
            restock(vil);
            VillagerUtilities.setNewTime(plugin, vil);
            return;
        } else if (curTick >= todayRestock2 && vilTick < todayRestock2) {
            restock(vil);
            VillagerUtilities.setNewTime(plugin, vil);
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
