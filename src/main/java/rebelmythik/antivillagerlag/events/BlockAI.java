package rebelmythik.antivillagerlag.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.Locale;

public class BlockAI {
    private final AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    long cooldown;

    public BlockAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }

    public void call(Villager vil, Player player) {

        // create variables
        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);

        long currentTime = System.currentTimeMillis() / 1000;
        long totalSeconds = vilCooldown - currentTime;
        long sec = totalSeconds % 60;
        long min = (totalSeconds - sec) / 60;

        // Permissions to Bypass Cooldown.
        // If they don't have permission run to see if the cooldown is over and send message if it isn't
        if (!player.hasPermission("avl.blockcooldown.bypass")) {
            if (vilCooldown > currentTime) {
                String message = plugin.getConfig().getString("messages.cooldown-block-message");
                if (message.contains("%avlminutes%")) {
                    message = VillagerUtilities.replaceText(message, "%avlminutes%", Long.toString(min));
                }
                message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorCodes.cm(message));
                return;
            }
        }

        // Handle the correct AI state
        VillagerUtilities.handleAiState(vil, this.plugin);

        VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
    }
}
