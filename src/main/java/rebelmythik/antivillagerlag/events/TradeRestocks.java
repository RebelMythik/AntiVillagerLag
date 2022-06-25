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
    public long restock1;
    public long restock2;


    public TradeRestocks(AntiVillagerLag plugin) {
        this.plugin = plugin;
        restock1 = plugin.getConfig().getLong("RestockTimes.time1");
        restock2 = plugin.getConfig().getLong("RestockTimes.time2");
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
        container.set(key, PersistentDataType.LONG, plugin.getServer().getWorld("world").getTime());
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
        long curTick = world.getTime();
        long vilTick = getTime(vil);

        //Check if he should be restocked
        System.out.println("curTick: " + Long.toString(curTick));
        System.out.println("vilTick: " + Long.toString(vilTick));
        if (curTick >= vilTick) {
            System.out.println("Time is equal");
            if (curTick >= restock2) {
                System.out.println("Curtick is > than Restock2");
                System.out.println(vilTick);
                System.out.println(restock2);
                System.out.println(restock1);
                if (vilTick <= restock2) {
                    System.out.println("vilTick is less than Restock2");
                    restock(vil);
                    System.out.println("Restocked Villager!");
                    setNewTime(vil);
                    System.out.println("Set new time");
                }
            } else if (curTick >= restock1) {
                System.out.println("Current tick is greater than Restock1");
                if (vilTick <= restock1) {
                    System.out.println("viltick is less than restock1");
                    restock(vil);
                    System.out.println("Restocked Villager!");
                    setNewTime(vil);
                    System.out.println("Set new time");
                }
            }
        } else {
            if (curTick >= restock1) {
                System.out.println("Current tick is greater than or equal to restock1");
                restock(vil);
                System.out.println("Restocked Villager!");
                setNewTime(vil);
                System.out.println("Set new time");
            }
        }
    }
}
