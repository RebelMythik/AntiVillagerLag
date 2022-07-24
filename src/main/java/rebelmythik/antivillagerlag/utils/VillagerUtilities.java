package rebelmythik.antivillagerlag.utils;

import org.bukkit.GameMode;
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
    public static void setNewTime(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        container.set(key, PersistentDataType.LONG, v.getWorld().getFullTime());
    }

    public static boolean hasTime(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        return (container.has(key, PersistentDataType.LONG));
    }

    public static long getTime(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "time");
        long time = container.get(key, PersistentDataType.LONG);
        return time;
    }
    public static void returnItem(Player player, AntiVillagerLag plugin) {
        // check if giving nametag is toggled and if player is not in creative
        if (!plugin.getConfig().getBoolean("toggleableoptions.usenametags") && player.getGameMode() != GameMode.CREATIVE) {
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
        }
    }
    public static boolean isDisabled(Villager vil, AntiVillagerLag plugin) {
        Location loc = vil.getLocation();
        // check if Villager is disabled with Nametag
        if((vil.getCustomName() != null) && (vil.getCustomName().equals(plugin.getConfig().getString("NameThatDisables")))){
            return true;
        }
        // else check if Villager is disabled with Block
        return vil.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ()).getType().equals(Material.getMaterial(plugin.getConfig().getString("BlockThatDisables")));
    }
    public static void setLevelCooldown(Villager v, AntiVillagerLag plugin, Long cooldown) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "levelCooldown");
        container.set(key, PersistentDataType.LONG, (System.currentTimeMillis() / 1000) + cooldown);
    }
    public static boolean hasLevelCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "levelCooldown");
        return container.has(key, PersistentDataType.LONG);
    }
    public static long getLevelCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "levelCooldown");
        long time = container.get(key, PersistentDataType.LONG);
        return time;
    }
    public static void setMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "Marker");
        container.set(key, PersistentDataType.STRING, ("AVL"));
    }
    public static boolean hasMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "Marker");
        return container.has(key, PersistentDataType.STRING);
    }
}
