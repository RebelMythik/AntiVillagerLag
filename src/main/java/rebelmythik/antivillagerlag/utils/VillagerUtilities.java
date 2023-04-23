package rebelmythik.antivillagerlag.utils;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import rebelmythik.antivillagerlag.AntiVillagerLag;

import java.util.List;

public class VillagerUtilities {

    // Define keys
    private static final String COOLDOWN_KEY = "cooldown";
    private static final String TIME_KEY = "time";
    private static final String LEVEL_COOLDOWN_KEY = "levelCooldown";
    private static final String MARKER_KEY = "Marker";
        // One key for each method of disabling villagers
    private static final String DISABLED_BY_NAMETAG_KEY = "disabledByNametag";
    private static final String DISABLED_BY_BLOCK_KEY = "disabledByBlock";
    private static final String DISABLED_BY_WORKSTATION_KEY = "disabledByWorkstation";
    private static final String DISABLED_BY_COMMAND_KEY = "disabledByCommand";

    // looks good to me

    // Trade Cooldown Tag (Time when next restock will happen)
    public static boolean hasRestockCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, COOLDOWN_KEY);
        return container.has(key, PersistentDataType.LONG);
    }
    public static void setNewRestockCooldown(Villager v, AntiVillagerLag plugin, Long cooldown) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, COOLDOWN_KEY);
        container.set(key, PersistentDataType.LONG, (System.currentTimeMillis() / 1000) + cooldown);
    }
    public static long getRestockCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, COOLDOWN_KEY);
        return container.get(key, PersistentDataType.LONG);
    }

    // Level Cooldown Tag (Time when villager will be off cooldown)
    public static boolean hasLevelCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, LEVEL_COOLDOWN_KEY);
        return container.has(key, PersistentDataType.LONG);
    }
    public static void setLevelCooldown(Villager v, AntiVillagerLag plugin, Long cooldown) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, LEVEL_COOLDOWN_KEY);
        container.set(key, PersistentDataType.LONG, (System.currentTimeMillis() / 1000) + cooldown);
    }
    public static long getLevelCooldown(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, LEVEL_COOLDOWN_KEY);
        return container.get(key, PersistentDataType.LONG);
    }

    // Time tag (Time represents the last time that the villager was restocked)
    public static boolean hasTime(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, TIME_KEY);
        return (container.has(key, PersistentDataType.LONG));
    }
    public static void setNewTime(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, TIME_KEY);
        container.set(key, PersistentDataType.LONG, v.getWorld().getFullTime());
    }
    public static long getTime(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, TIME_KEY);
        return container.get(key, PersistentDataType.LONG);
    }

    // Marker tag
    public static boolean hasMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, MARKER_KEY);
        return container.has(key, PersistentDataType.STRING);
    }
    public static void setMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, MARKER_KEY);
        container.set(key, PersistentDataType.STRING, ("AVL"));
    }
    public static void removeMarker(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, MARKER_KEY);
        container.remove(key);
    }

    // Disabled by Nametag Stuff
    public static boolean hasDisabledByNametag(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_NAMETAG_KEY);
        return container.has(key, PersistentDataType.STRING);
    }
    public static void setDisabledByNametag(Villager v, AntiVillagerLag plugin, Boolean disabledByNametag) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_NAMETAG_KEY);
        container.set(key, PersistentDataType.STRING, disabledByNametag.toString());
    }
    public static boolean getDisabledByNametag(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_NAMETAG_KEY);
        return Boolean.parseBoolean(container.get(key, PersistentDataType.STRING));
    }

    // Disabled by Block Stuff
    public static boolean hasDisabledByBlock(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_BLOCK_KEY);
        return container.has(key, PersistentDataType.STRING);
    }
    public static void setDisabledByBlock(Villager v, AntiVillagerLag plugin, Boolean disabledByBlock) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_BLOCK_KEY);
        container.set(key, PersistentDataType.STRING, disabledByBlock.toString());
    }
    public static boolean getDisabledByBlock(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_BLOCK_KEY);
        return Boolean.parseBoolean(container.get(key, PersistentDataType.STRING));
    }

    // Disabled by Workstation Stuff
    public static boolean hasDisabledByWorkstation(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_WORKSTATION_KEY);
        return container.has(key, PersistentDataType.STRING);
    }
    public static void setDisabledByWorkstation(Villager v, AntiVillagerLag plugin, Boolean disabledByWorkstation) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_WORKSTATION_KEY);
        container.set(key, PersistentDataType.STRING, disabledByWorkstation.toString());
    }
    public static boolean getDisabledByWorkstation(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_WORKSTATION_KEY);
        return Boolean.parseBoolean(container.get(key, PersistentDataType.STRING));
    }

    // Disabled by Command Stuff
    public static boolean hasDisabledByCommand(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_COMMAND_KEY);
        return container.has(key, PersistentDataType.STRING);
    }
    public static void setDisabledByCommand(Villager v, AntiVillagerLag plugin, Boolean disabledByCommand) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_COMMAND_KEY);
        container.set(key, PersistentDataType.STRING, disabledByCommand.toString());
    }
    public static boolean getDisabledByCommand(Villager v, AntiVillagerLag plugin) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, DISABLED_BY_COMMAND_KEY);
        return Boolean.parseBoolean(container.get(key, PersistentDataType.STRING));
    }

    //Misc
    public static void returnItem(Player player, AntiVillagerLag plugin) {
        // check if giving nametag is toggled and if player is not in creative
        if (!plugin.getConfig().getBoolean("toggleableoptions.usenametags") && player.getGameMode() != GameMode.CREATIVE) {
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
        }
    }
    public static String replaceText(String text, String stuff2cut, String replacement) {
        int index = text.indexOf(stuff2cut);
        String text1 = text.substring(0, index);
        String text2 = text.substring(index + stuff2cut.length());
        return text1 + replacement + text2;
    }
    public static boolean isDisabled(Villager vil, AntiVillagerLag plugin) {
        return getDisabledByNametag(vil, plugin) || getDisabledByBlock(vil, plugin) || getDisabledByWorkstation(vil, plugin) || getDisabledByCommand(vil, plugin);
    }
    public static boolean onAiToggleCooldown(Villager vil, Player player, AntiVillagerLag plugin, ColorCode colorCodes) {

        // Permission to Bypass Cooldown.
        if (player.hasPermission("avl.blockcooldown.bypass"))
            return false;

        // create variables
        long vilCooldown = VillagerUtilities.getRestockCooldown(vil, plugin);

        long currentTime = System.currentTimeMillis() / 1000;

        // see if the cooldown is over and send message if it isn't
        if (vilCooldown > currentTime) {

            long totalSeconds = vilCooldown - currentTime;
            long sec = totalSeconds % 60;
            long min = (totalSeconds - sec) / 60;

            String message = plugin.getConfig().getString("messages.cooldown-block-message");
            if (message.contains("%avlminutes%")) {
                message = VillagerUtilities.replaceText(message, "%avlminutes%", Long.toString(min));
            }
            message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
            player.sendMessage(colorCodes.cm(message));
            return true;
        }
        return false;
    }
    public static void restock(Villager v) {
        List<MerchantRecipe> recipes = v.getRecipes();
        for (MerchantRecipe r: recipes) {
            r.setUses(0);
        }
    }
    public static void disableTheVillager(Villager vil, AntiVillagerLag plugin) {

    }
    public static void undisableTheVillager(Villager vil, AntiVillagerLag plugin) {

    }
}
