package rebelmythik.antivillagerlag.events;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;

public class TradeRestocks implements Listener {

    public AntiVillagerLag plugin;

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
        if (!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;
        Villager vil = (Villager) e.getRightClicked();
        if (!vil.getName().equals(plugin.getConfig().getString("NameThatDisables"))) return;

        //Code if the villager is braindead to restock it
        World world = plugin.getServer().getWorld("world");
        long time = world.getGameTime();
        long lastRestock = 0;
        long curDay = time / numOfTicksInDay;
        long curTick = time % numOfTicksInDay;
        //Code if the day is over
        /*
        if gameTime > day of last restock {
            if gameTime > firsttime
                restock villager code here
        }

        else

        Check if the tick time is past on the same day
        then restock
        */
        //Code if the day is same and check if its past the next tick time





    }



}
