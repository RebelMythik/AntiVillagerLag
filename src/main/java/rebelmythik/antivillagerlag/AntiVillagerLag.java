package rebelmythik.antivillagerlag;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import rebelmythik.antivillagerlag.autoevents.VillagerScanner;
import rebelmythik.antivillagerlag.commands.RadiusOptimizeCommand;
import rebelmythik.antivillagerlag.commands.ReloadCommand;
import rebelmythik.antivillagerlag.events.EventListenerHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class AntiVillagerLag extends JavaPlugin {

    private VillagerScanner villagerScanner;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new EventListenerHandler(this),this);
        villagerScanner = new VillagerScanner(this);
        villagerScanner.startTaskTimer();

        getCommand("avlreload").setExecutor(new ReloadCommand(this));
        getCommand("avloptimize").setExecutor(new RadiusOptimizeCommand(this));
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
        villagerScanner.cancelTaskTimer();
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
                        Object value = cfg.get(str);
                        if (value instanceof String) {
                            String stringValue = (String) value;
                            if (!stringValue.startsWith("'") || !stringValue.endsWith("'")) {
                                // Use a placeholder to avoid triple single quotes
                                tmp.set(str, "__PLACEHOLDER__" + stringValue + "__PLACEHOLDER__");
                                changesMade = true;
                            }
                        } else {
                            tmp.set(str, value);
                            changesMade = true;
                        }
                    }
                }
                tmp.save(getDataFolder() + "/config.yml");
                if(changesMade)
                    tmp.save(getDataFolder() + "/config.yml");

                // Read and re-write config to format the values correctly
                String content = new String(Files.readAllBytes(Paths.get(getDataFolder() + "/config.yml")));
                content = content.replaceAll("__PLACEHOLDER__", "'");
                Files.write(Paths.get(getDataFolder() + "/config.yml"), content.getBytes());
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
