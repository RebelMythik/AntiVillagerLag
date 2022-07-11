package rebelmythik.antivillagerlag.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import rebelmythik.antivillagerlag.AntiVillagerLag;

public class VilUtil {

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
        String text2 = text.substring(index + stuff2cut.length(), text.length());
        String finalText = text1 + replacement + text2;
        return finalText;
    }
}
