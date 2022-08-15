package rebelmythik.antivillagerlag;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import rebelmythik.antivillagerlag.commands.RadiusOptimizeCommand;
import rebelmythik.antivillagerlag.commands.ReloadCommand;
import rebelmythik.antivillagerlag.events.EventListenerHandler;
import rebelmythik.antivillagerlag.utils.ColorCode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class AntiVillagerLag extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new EventListenerHandler(this),this);
        ColorCode colorcodes = new ColorCode();

        getCommand("avlreload").setExecutor(new ReloadCommand(this));
        getCommand("avlreload").setPermissionMessage(colorcodes.cm(this.getConfig().getString("messages.no-permission")));

        getCommand("avloptimize").setExecutor(new RadiusOptimizeCommand(this));
        getCommand("avloptimize").setPermissionMessage(colorcodes.cm(this.getConfig().getString("messages.no-permission")));


        saveDefaultConfig();
        updateConfig();

        int pluginId = 15890;
        Metrics metrics = new Metrics(this, pluginId);

        // Optional: Add custom charts
        metrics.addCustomChart(new MultiLineChart("players_and_servers", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());
            return valueMap;
        }));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

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
