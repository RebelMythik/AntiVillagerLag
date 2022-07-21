package rebelmythik.antivillagerlag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;

public class ReloadCommand implements CommandExecutor {

    AntiVillagerLag plugin;
    ColorCode colorcodes = new ColorCode();

    public ReloadCommand(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }
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
