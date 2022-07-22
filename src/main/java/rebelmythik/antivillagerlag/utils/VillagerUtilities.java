package rebelmythik.antivillagerlag.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import rebelmythik.antivillagerlag.AntiVillagerLag;

import java.util.List;

public class VillagerUtilities {

    public static void setNewCooldown(Villager v, AntiVillagerLag plugin, Long cooldown) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cooldown");
        container.set(key, PersistentDataType.LONG, (System.currentTimeMillis() / 1000) + cooldown);
    }

    public static boolean hasCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cooldown");
        return container.has(key, PersistentDataType.LONG);
    }

    public static long getCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cooldown");
        long time = container.get(key, PersistentDataType.LONG);
        return time;
    }

    public static String replaceText(String text, String stuff2cut, String replacement) {
        int index = text.indexOf(stuff2cut);
        String text1 = text.substring(0, index);
        String text2 = text.substring(index + stuff2cut.length());
        String finalText = text1 + replacement + text2;
        return finalText;
    }


    public static void restock(Villager v) {
        List<MerchantRecipe> recipes = v.getRecipes();
        for (MerchantRecipe r: recipes) {
            r.setUses(0);
        }
    }

    public static void setNewTime(AntiVillagerLag plugin, Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        container.set(key, PersistentDataType.LONG, v.getWorld().getTime());
    }

    public static boolean hasTime(AntiVillagerLag plugin, Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        return (container.has(key, PersistentDataType.LONG));
    }

    public static long getTime(AntiVillagerLag plugin, Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        long time = container.get(key, PersistentDataType.LONG);
        return time;
    }
}
