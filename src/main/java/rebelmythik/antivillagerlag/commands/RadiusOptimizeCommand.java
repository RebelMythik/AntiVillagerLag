package rebelmythik.antivillagerlag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.List;

public class RadiusOptimizeCommand implements CommandExecutor {

    AntiVillagerLag plugin;
    ColorCode colorcodes = new ColorCode();

    private final long cooldown;

    public RadiusOptimizeCommand(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // make sure command is executed by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command needs to be run as a player");
            return false;
        }
        Player player = (Player) sender;

        // check the command
        if (cmd.getName().equalsIgnoreCase("avloptimize")) {

            // check for permission
            if(!player.hasPermission("avl.optimize")) {
                player.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }

            // handle usage
            if (args.length == 0) {
                player.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.correct-usage")));
                return false;
            }
            if (args.length == 1) {
                Bukkit.getPlayer(args[0]);
                try {
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException numberFormatException) {
                    player.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.correct-usage")));
                    return false;
                }
                double radius = Double.parseDouble(args[0]);

                if (radius > plugin.getConfig().getDouble("RadiusLimit")) {
                    player.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.radius-limit")));
                    return false;
                }

                for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                    if (entity instanceof Villager) {
                        Villager vil = (Villager) entity;
                        // check whether this villager has tags
                        VillagerUtilities.verifyTags(vil, plugin);
                        if(!VillagerUtilities.isDisabled(vil, plugin)) {

                            // Set Villager Name to Optimize Name and disable the AI
                            List<String> namesThatDisable = plugin.getConfig().getStringList("NamesThatDisable");
                            vil.setCustomName(namesThatDisable.get(0));
                            VillagerUtilities.disableTheVillager(vil, plugin, 10L);
                            VillagerUtilities.setDisabledByNametag(vil, plugin, true);

                        }
                    }
                }
            }
        }
        return false;
    }
}
