package rebelmythik.antiVillagerLag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import rebelmythik.antiVillagerLag.AntiVillagerLag;
import rebelmythik.antiVillagerLag.utils.ColorCode;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

public class ReloadCommand implements CommandExecutor {

    AntiVillagerLag plugin;

    public ReloadCommand(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("avlreload")) {
            if(!sender.hasPermission("avl.reload")) {
                sender.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }
            sender.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.reload-message")));
            plugin.reloadConfig();

            //update the workblock blocks
            VillagerUtilities.updateNameTags(plugin);
            VillagerUtilities.updateStandingOnBlocks(plugin);
            VillagerUtilities.updateWorkstationBlocks(plugin);
            VillagerUtilities.updateRestockTimes(plugin);
        }
        return true;
    }
}
