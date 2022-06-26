package rebelmythik.antivillagerlag;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import rebelmythik.antivillagerlag.commands.reloadcommand;
import rebelmythik.antivillagerlag.events.DisableVillagerAI;
import rebelmythik.antivillagerlag.events.ReEnableVillagerAI;
import rebelmythik.antivillagerlag.events.TradeRestocks;

public final class AntiVillagerLag extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new DisableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new ReEnableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new TradeRestocks(this), this);
        getCommand("avlreload").setExecutor(new reloadcommand(this));
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
