package rebelmythik.antivillagerlag.events;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import rebelmythik.antivillagerlag.AntiVillagerLag;

import java.util.List;

public class TradeRestocks implements Listener {

    public AntiVillagerLag plugin;
    public long restock1 = 1000;
    public long restock2 = 13000;


    public TradeRestocks(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    public void restock(Villager v) {
        List<MerchantRecipe> recipes = v.getRecipes();
        for (MerchantRecipe r : recipes) {
            r.setUses(0);
        }
    }

    public void setNewTime(Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        container.set(key, PersistentDataType.LONG, plugin.getServer().getWorld("world").getGameTime());
    }

    public boolean hasTime(Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        if (container.has(key, PersistentDataType.LONG)) {
            return true;
        }
        return false;
    }

    public long getTime(Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        long time = container.get(key, PersistentDataType.LONG);
        return time;
    }

    @EventHandler
    public void clickVillager(PlayerInteractEntityEvent e) {
        //is it the villager we want?
        if (!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;
        Villager vil = (Villager) e.getRightClicked();
        if (!vil.getCustomName().equals(plugin.getConfig().getString("NameThatDisables"))) return;
        //If he doesn't have a time, restock
        System.out.println(hasTime(vil));
        if (!hasTime(vil)) {
            setNewTime(vil);
            System.out.println("Should have time now");
        }
        //if he does have a time, get it; also create time variables
        World world = plugin.getServer().getWorld("world");
        long time = world.getGameTime();
        long numOfTicksInDay = 24000;
        long curTick = time % numOfTicksInDay;
        long curDay = (time - curTick) / numOfTicksInDay;
        long lastTime = getTime(vil);
        long vilTick = lastTime % numOfTicksInDay;
        long vilDay = (lastTime - vilTick) / numOfTicksInDay;

        //Check if he should be restocked
        System.out.println("lastTime: " + Long.toString(lastTime));
        System.out.println("curDay: " + Long.toString(curDay));
        System.out.println("curTick: " + Long.toString(curTick));
        System.out.println("vilDay: " + Long.toString(vilDay));
        System.out.println("vilTick: " + Long.toString(vilTick));
        if (curDay == vilDay) {
            if (curTick >= restock2) {
                if (vilTick <= restock2) {
                    restock(vil);
                    setNewTime(vil);
                }
            } else if (curTick >= restock1) {
                if (vilTick <= restock1) {
                    restock(vil);
                    setNewTime(vil);
                }
            }
        } else {
            if (curTick >= restock1) {
                restock(vil);
                setNewTime(vil);
            }
        }
    }
}
