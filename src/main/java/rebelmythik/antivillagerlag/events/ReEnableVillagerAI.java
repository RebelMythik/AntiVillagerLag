package rebelmythik.antivillagerlag.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VilUtil;

public class ReEnableVillagerAI implements Listener {

    public AntiVillagerLag plugin;
    long cooldown;
    ColorCode colorcodes = new ColorCode();
    long currenttime = System.currentTimeMillis() / 1000;

    public ReEnableVillagerAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        cooldown = plugin.getConfig().getLong("cooldown");
    }

    @EventHandler
    public void RightClick(PlayerInteractEntityEvent e) {
        boolean right = true;
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        PlayerInventory inv = player.getInventory();
        if (!(entity.getType().equals(EntityType.VILLAGER))) return;
        Villager vil = (Villager) entity;
        ItemStack item;
        currenttime = System.currentTimeMillis() / 1000;
        //If he doesn't have a cooldown, add it?
        if (!VilUtil.hasCooldown(vil, plugin)) {
            VilUtil.setNewCooldown(vil, plugin, cooldown);
            return;
        }

        long vilCooldown = VilUtil.getCooldown(vil, plugin);

        if (!plugin.getConfig().getBoolean("toggleableoptions.userenaming")) return;

        if (e.getHand().equals(EquipmentSlot.HAND)) {
            if (!inv.getItemInMainHand().getType().equals(Material.NAME_TAG)) return;
            item = inv.getItemInMainHand();
        } else {
            if (!inv.getItemInOffHand().getType().equals(Material.NAME_TAG)) return;
            item = inv.getItemInOffHand();
            right = false;
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"))) return;

        if (!player.hasPermission("avl.renamecooldown.bypass")) {
            if (vilCooldown >= currenttime) {
                Long totalseconds = vilCooldown - currenttime;
                Long sec = totalseconds % 60;
                Long min = (totalseconds - sec) / 60;

                String message = plugin.getConfig().getString("messages.cooldown-message");
                if (message.contains("%avlminutes%")) {
                    message = VilUtil.replaceText(message, "%avlminutes%", Long.toString(min));
                }
                message = VilUtil.replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorcodes.cm(message));
                e.setCancelled(true);
                return;
            }
        }

        if (!plugin.getConfig().getBoolean("toggleableoptions.usenametags")) {
            if (right) {
                inv.getItemInMainHand().setAmount(inv.getItemInMainHand().getAmount() + 1);
            } else {
                inv.getItemInOffHand().setAmount(inv.getItemInOffHand().getAmount() + 1);
            }
        }

        vil.setAI(true);
        VilUtil.setNewCooldown(vil, plugin, cooldown);
    }
}