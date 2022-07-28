package rebelmythik.antivillagerlag.events;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;


public class BlockAI {
    private final AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    long cooldown;

    public BlockAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }
    public static void handleAiState(Villager vil, AntiVillagerLag plugin, boolean willBeDisabled){
        if(vil.hasAI()) {
            // Check that the villager is disabled
            if (!willBeDisabled)
                return;
            VillagerUtilities.setMarker(vil, plugin);
            VillagerUtilities.setDisabledByBlock(vil, plugin, true);
            vil.setAI(false);
        } else {
            // Re-Enabling AI
            // Check that the villager is disabled and disabled by Block
            if (willBeDisabled || !VillagerUtilities.getDisabledByBlock(vil, plugin))
                return;
            if (!VillagerUtilities.hasMarker(vil, plugin)) return;

            vil.setAI(true);

            VillagerUtilities.setDisabledByBlock(vil, plugin, false);
        }
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
        Location loc = vil.getLocation();
        Material belowvil = vil.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY()-1), loc.getBlockZ()).getType();
        // else check if Villager is disabled with Block

        boolean willBeDisabled = plugin.getConfig().getStringList("BlocksThatDisables").contains(belowvil.toString());

        // Handle the correct AI state
        handleAiState(vil, this.plugin, willBeDisabled);

        VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
    }
}
