package rebelmythik.antivillagerlag.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.api.colorcode;

public class DisableVillagerAI implements Listener {
    public AntiVillagerLag plugin;
    long cooldown;
    colorcode colorcodes = new colorcode();

    public DisableVillagerAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        cooldown = plugin.getConfig().getLong("cooldown");
    }

    long currenttime = System.currentTimeMillis() / 1000;

    public void setNewCooldown(Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cooldown");
        currenttime = System.currentTimeMillis() / 1000;
        container.set(key, PersistentDataType.LONG, currenttime + cooldown);
    }
    public boolean hasCooldown(Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cooldown");
        if (container.has(key, PersistentDataType.LONG)) {
            return true;
        }
        return false;
    }
    public long getCooldown(Villager v) {
        PersistentDataContainer container = v.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cooldown");
        long time = container.get(key, PersistentDataType.LONG);
        return time;
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
        if (!hasCooldown(vil)) {
            setNewCooldown(vil);
            return;
        }

        long vilCooldown = getCooldown(vil);
        //

        if (e.getHand().equals(EquipmentSlot.HAND)) {
            if (!inv.getItemInMainHand().getType().equals(Material.NAME_TAG)) return;
            item = inv.getItemInMainHand();
        } else {
            if (!inv.getItemInOffHand().getType().equals(Material.NAME_TAG)) return;
            item = inv.getItemInOffHand();
            right = false;
        }
        if (!item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"))) return;

        if (!player.hasPermission("avl.renamecooldown.bypass")) {
            if (vilCooldown >= currenttime) {
                Long totalseconds = vilCooldown - currenttime;
                Long sec = totalseconds % 60;
                Long min = (totalseconds - sec) / 60;

                String message = plugin.getConfig().getString("messages.cooldown-message");
                if (message.contains("%avlminutes%")) {
                    message = replaceText(message, "%avlminutes%", Long.toString(min));
                }
                message = replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorcodes.cm(message));
                e.setCancelled(true);
                return;
            }
        }
        if (right) {
            inv.getItemInMainHand().setAmount(inv.getItemInMainHand().getAmount() + 1);
        } else {
            inv.getItemInOffHand().setAmount(inv.getItemInOffHand().getAmount() + 1);
        }
        vil.setAI(false);
        setNewCooldown(vil);
    }
}