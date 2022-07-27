package rebelmythik.antivillagerlag.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.CalculateLevel;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

public class VillagerLevelManager {
    private AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    long cooldown;
    public VillagerLevelManager(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = 5;
    }

    public void call(Villager vil, Player player) {

        int vilLevel = vil.getVillagerLevel();
        long newLevel = CalculateLevel.villagerEXP(vil);
        long currentTime = System.currentTimeMillis() / 1000;

        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);
        long totalSeconds = vilLevelCooldown - currentTime;
        long sec = totalSeconds % 60;

        if (vilLevelCooldown > currentTime) {
            String message = plugin.getConfig().getString("messages.cooldown-levelup-message");
            message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
            player.sendMessage(colorCodes.cm(message));
            return;
        }

        if (vilLevel < newLevel) {
            VillagerUtilities.setLevelCooldown(vil, plugin, cooldown);
            vil.setAI(true);
        } else return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            vil.setAI(false);
        }, 100L);
    }
}
