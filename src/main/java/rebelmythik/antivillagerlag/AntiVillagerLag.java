package rebelmythik.antivillagerlag;

import org.bukkit.plugin.java.JavaPlugin;
import rebelmythik.antivillagerlag.commands.ReloadCommand;
import rebelmythik.antivillagerlag.events.*;

public final class AntiVillagerLag extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new BlockDisableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockReEnableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new DisableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new ReEnableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new TradeRestocks(this), this);
        getCommand("avlreload").setExecutor(new ReloadCommand(this));
        saveDefaultConfig();

        if (!getConfig().getBoolean("toggleableoptions.userenaming") && !getConfig().getBoolean("toggleableoptions.useblocks")) {
            getLogger().info("You need to use blocks or renaming!");
            return;
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
