package rebelmythik.antivillagerlag.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VilUtil;

public class BlockReEnableVillagerAI implements Listener {

    public AntiVillagerLag plugin;
    long cooldown;
    ColorCode colorcodes = new ColorCode();
    long currenttime = System.currentTimeMillis() / 1000;

    public BlockReEnableVillagerAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        cooldown = plugin.getConfig().getLong("cooldown");
    }

    @EventHandler
    public void RightClick(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        if (!(entity.getType().equals(EntityType.VILLAGER))) return;
        Villager vil = (Villager) entity;
        currenttime = System.currentTimeMillis() / 1000;
        Location loc = vil.getLocation();
        double blocky =  loc.getY() - 1;
        //If he doesn't have a cooldown, add it?
        if (!VilUtil.hasCooldown(vil, plugin)) {
            VilUtil.setNewCooldown(vil, plugin, cooldown);
            return;
        }

        long vilCooldown = VilUtil.getCooldown(vil, plugin);

        if (!plugin.getConfig().getBoolean("toggleableoptions.useblocks")) return;

        //replace with block logic
        if (vil.getName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"))) return;
        if (vil.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ()).getType().equals(Material.getMaterial(plugin.getConfig().getString("BlockThatDisables")))) return;

        if (!player.hasPermission("avl.renamecooldown.bypass")) {
            if (vilCooldown >= currenttime) {
                Long totalseconds = vilCooldown - currenttime;
                Long sec = totalseconds % 60;
                Long min = (totalseconds - sec) / 60;

                String message = plugin.getConfig().getString("messages.cooldown-block-message");
                if (message.contains("%avlminutes%")) {
                    message = VilUtil.replaceText(message, "%avlminutes%", Long.toString(min));
                }
                message = VilUtil.replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorcodes.cm(message));
                e.setCancelled(true);
                return;
            }
        }

        vil.setAI(true);
        VilUtil.setNewCooldown(vil, plugin, cooldown);
    }
}
