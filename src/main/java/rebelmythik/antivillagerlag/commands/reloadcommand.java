package rebelmythik.antivillagerlag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.api.colorcode;

public class reloadcommand implements CommandExecutor {
    AntiVillagerLag plugin;

    public reloadcommand(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    colorcode colorcodes = new colorcode();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("avlreload")) {
            if(!sender.hasPermission("avl.reload")) {
                sender.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }
            sender.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.reload-message")));
            plugin.reloadConfig();
        }
        return false;
    }
}
