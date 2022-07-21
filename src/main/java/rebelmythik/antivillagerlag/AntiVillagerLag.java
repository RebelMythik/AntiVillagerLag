package rebelmythik.antivillagerlag;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import rebelmythik.antivillagerlag.events.NameTagAI;

import java.io.File;
import java.io.IOException;

public final class AntiVillagerLag extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new NameTagAI(this), this);


        saveDefaultConfig();
        updateConfig();
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
