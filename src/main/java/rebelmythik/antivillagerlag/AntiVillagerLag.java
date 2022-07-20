package rebelmythik.antivillagerlag;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import rebelmythik.antivillagerlag.commands.ReloadCommand;
import rebelmythik.antivillagerlag.events.*;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class AntiVillagerLag extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new BlockDisableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockReEnableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new DisableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new ReEnableVillagerAI(this), this);
        this.getServer().getPluginManager().registerEvents(new TradeRestocks(this), this);
        this.getServer().getPluginManager().registerEvents(new VilLevelGetter(this), this);
        getCommand("avlreload").setExecutor(new ReloadCommand(this));
        saveDefaultConfig();
        updateConfig();

        if (!getConfig().getBoolean("toggleableoptions.userenaming") && !getConfig().getBoolean("toggleableoptions.useblocks")) {
            this.getLogger().log(Level.FINE, "You don't have Nametags or Blocks enabled for toggling villager AI. This plugin will do nothing. Disabling");
            Bukkit.getPluginManager().disablePlugin(this);
        }
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
