package rebelmythik.antiVillagerLag.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import rebelmythik.antiVillagerLag.AntiVillagerLag;

import java.util.ArrayList;
import java.util.List;

public class VillagerUtilities {

    ///     Keys

    private static final String MARKER_KEY = "Marker";
    private static final String AI_COOLDOWN_KEY = "cooldown";
    private static final String LEVEL_COOLDOWN_KEY = "levelCooldown";
    private static final String LAST_RESTOCK_KEY = "time";
    public static final ColorCode colorcodes = new ColorCode();

    ///     Config Variables

    public static List<String> disabling_names = new ArrayList<>();
    public static List<Material> standingon_blocks = new ArrayList<>();
    public static List<Material> workstation_blocks = new ArrayList<>();
    public static List<Long> restock_times = new ArrayList<>();

    ///     Marker

    public static void setMarker(Villager v, AntiVillagerLag plugin, boolean val) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, MARKER_KEY);
        container.set(key, PersistentDataType.BOOLEAN, val);
    }
    public static boolean hasMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, MARKER_KEY);
        return container.has(key, PersistentDataType.BOOLEAN);
    }
    // If false, then villager is disabled
    public static boolean getMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, MARKER_KEY);
        return container.get(key, PersistentDataType.BOOLEAN);
    }
    public static void removeMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, MARKER_KEY);
        container.remove(key);
    }

    ///     Data

    public static void createData(Villager v, AntiVillagerLag plugin, String key_name, long data) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, key_name);
        container.set(key, PersistentDataType.LONG, data);
    }
    public static long getData(Villager v, AntiVillagerLag plugin, String key_name) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, key_name);
        return container.get(key, PersistentDataType.LONG);
    }
    public static void removeData(Villager v, AntiVillagerLag plugin, String key_name) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, key_name);
        container.remove(key);
    }

    public static void setAiCooldown(Villager v, AntiVillagerLag plugin, long cooldown) {
        createData(v, plugin, AI_COOLDOWN_KEY, (System.currentTimeMillis() / 1000) + cooldown);
    }
    public static void setLevelCooldown(Villager v, AntiVillagerLag plugin, long cooldown) {
        createData(v, plugin, LEVEL_COOLDOWN_KEY, (System.currentTimeMillis() / 1000) + cooldown);
    }
    public static void setLastRestock(Villager v, AntiVillagerLag plugin) {
        createData(v, plugin, LAST_RESTOCK_KEY, v.getWorld().getFullTime());
    }

    public static long getAiCooldown(Villager v, AntiVillagerLag plugin) {
        return getData(v, plugin,  AI_COOLDOWN_KEY);
    }
    public static long getLevelCooldown(Villager v, AntiVillagerLag plugin) {
        return getData(v, plugin, LEVEL_COOLDOWN_KEY);
    }
    public static long getLastRestock(Villager v, AntiVillagerLag plugin) {
        return getData(v, plugin, LAST_RESTOCK_KEY);
    }

    ///     Config fetching

    public static void updateNameTags(AntiVillagerLag plugin) {
        if (!plugin.getConfig().getBoolean("toggleableoptions.userenaming")) return;
        disabling_names.clear();
        for (String name : plugin.getConfig().getStringList("NamesThatDisable")) {
            disabling_names.add(name.toLowerCase());
        }

    }

    public static void updateStandingOnBlocks(AntiVillagerLag plugin) {
        if (!plugin.getConfig().getBoolean("toggleableoptions.useblocks")) return;
        standingon_blocks.clear();
        for (String blockName : plugin.getConfig().getStringList("BlocksThatDisable")) {
            Material block = Material.getMaterial(blockName.toUpperCase());
            if (block != null) {
                standingon_blocks.add(block);
            }
        }
    }

    public static void updateWorkstationBlocks(AntiVillagerLag plugin) {
        if (!plugin.getConfig().getBoolean("toggleableoptions.useworkstations")) return;
        workstation_blocks.clear();
        for (String blockName : plugin.getConfig().getStringList("WorkstationsThatDisable")) {
            Material block = Material.getMaterial(blockName.toUpperCase());
            if (block != null) {
                workstation_blocks.add(block);
            }
        }
    }

    public static void updateRestockTimes(AntiVillagerLag plugin) {
        restock_times.clear();
        for (long restockTime : plugin.getConfig().getLongList("RestockTimes.times")) {
            restock_times.add(restockTime);
        }
    }

    ///     Clean Up

    public static void CleanseTheVillagers(Villager v, AntiVillagerLag plugin) {
        if (!hasMarker(v, plugin)) return;
        v.setAware(true);
        removeMarker(v, plugin);
        removeData(v, plugin, AI_COOLDOWN_KEY);
        removeData(v, plugin, LEVEL_COOLDOWN_KEY);
        removeData(v, plugin, LAST_RESTOCK_KEY);
    }

    // Villager Restocking
    public static void restock(Villager v) {
        List<MerchantRecipe> recipes = v.getRecipes();
        for (MerchantRecipe r: recipes) {
            r.setUses(0);
        }
    }

}
