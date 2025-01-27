package rebelmythik.antiVillagerLag;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import rebelmythik.antiVillagerLag.commands.OptimizeCommand;
import rebelmythik.antiVillagerLag.commands.ReloadCommand;
import rebelmythik.antiVillagerLag.commands.RemoveChangesCommand;
import rebelmythik.antiVillagerLag.commands.UnoptimizeCommand;
import rebelmythik.antiVillagerLag.events.EventListener;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class AntiVillagerLag extends JavaPlugin {

    @Override
    public void onEnable() {

        //  Command Registration
        getCommand("avlreload").setExecutor(new ReloadCommand(this));
        getCommand("avloptimize").setExecutor(new OptimizeCommand(this));
        getCommand("avlunoptimize").setExecutor(new UnoptimizeCommand(this));
        getCommand("avlremove").setExecutor(new RemoveChangesCommand(this));

        //  Event Registration
        getServer().getPluginManager().registerEvents(new EventListener(this), this);


        //  Config Stuff
        saveDefaultConfig();
        updateConfig();

        VillagerUtilities.updateNameTags(this);
        VillagerUtilities.updateStandingOnBlocks(this);
        VillagerUtilities.updateWorkstationBlocks(this);

        //  Bstats Code
        int pluginId = 15890;
        Metrics metrics = new Metrics(this, pluginId);
        //  Optional: Add custom charts
        metrics.addCustomChart(new MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));
    }

    @Override
    public void onDisable() {
        //  Plugin shutdown logic
    }

    //  Configuration File Updater
    public Configuration cfg = this.getConfig().getDefaults();
    public void updateConfig() {
        try {
            if(new File(getDataFolder() + "/config.yml").exists()) {
                boolean changesMade = false;
                YamlConfiguration tmp = new YamlConfiguration();
                tmp.load(getDataFolder() + "/config.yml");
                for(String str : cfg.getKeys(true)) {
                    if(!tmp.getKeys(true).contains(str)) {
                        tmp.set(str, cfg.get(str));
                        changesMade = true;
                    }
                }
                if(changesMade)
                    tmp.save(getDataFolder() + "/config.yml");
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
