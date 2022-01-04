package me.rebelmythik.antivillagerlag;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import me.rebelmythik.antivillagerlag.utils.timer;

public final class AntiVillagerLag extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
//        timer t = new timer(this);
//        t.createScheduler();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
