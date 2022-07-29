package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;



public class EventListenerHandler implements Listener {

    public AntiVillagerLag plugin;
    public BlockAI blockAi;
    public NameTagAI nameTagAI;
    public RestockVillager restockVillager;
    public VillagerLevelManager villagerLevelManager;
    ColorCode colorCodes = new ColorCode();
    public EventListenerHandler(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.blockAi = new BlockAI(plugin);
        this.nameTagAI = new NameTagAI(plugin);
        this.restockVillager = new RestockVillager(plugin);
        this.villagerLevelManager = new VillagerLevelManager(plugin);
    }



    @EventHandler
    public void rightClick(PlayerInteractEntityEvent e){

        // do checks and setup
        if (!e.getRightClicked().getType().equals(EntityType.VILLAGER)) return;

        Villager vil = (Villager) e.getRightClicked();

        // check whether this villager has cooldown tags
        if (!VillagerUtilities.hasCooldown(vil, plugin)) VillagerUtilities.setNewCooldown(vil, plugin, (long)0);
        if (!VillagerUtilities.hasLevelCooldown(vil, plugin)) VillagerUtilities.setLevelCooldown(vil, plugin, (long)0);

        long currentTime = System.currentTimeMillis() / 1000;

        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);

        long totalSeconds = vilLevelCooldown - currentTime;
        long sec = totalSeconds % 60;

        Player player = e.getPlayer();

        // Check if the villager is disabled for leveling and send a message
        if (vilLevelCooldown > currentTime) {
            String message = plugin.getConfig().getString("messages.cooldown-levelup-message");
            message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
            player.sendMessage(colorCodes.cm(message));
            // why not ;)
            vil.shakeHead();
            e.setCancelled(true);
            return;
        }

        if(!VillagerUtilities.hasDisabledByBlock(vil, plugin)){
            VillagerUtilities.setDisabledByBlock(vil, plugin, false);
        }

        // handle Nametag Ai
        if (plugin.getConfig().getBoolean("toggleableoptions.userenaming") && !VillagerUtilities.getDisabledByBlock(vil, plugin))
            nameTagAI.call(vil, player, e);

        // handle Block Ai, check if nametag cancelled event (avoid duplicate error?)
        if (plugin.getConfig().getBoolean("toggleableoptions.useblocks") && !e.isCancelled())
            blockAi.call(vil, player);

        // handle Restock, check if Villager is disabled before
        if (VillagerUtilities.isDisabled(vil, plugin))
            restockVillager.call(vil, player);
    }

    @EventHandler
    public void afterTrade(InventoryCloseEvent event) {
        // check if inventory belongs to a Villager Trade Screen
        if(event.getInventory().getType() != InventoryType.MERCHANT) return;

        if (event.getInventory().getHolder() == null) return;

        Villager vil = (Villager) event.getInventory().getHolder();


        if (!VillagerUtilities.isDisabled(vil, plugin)) return;
        if (!VillagerUtilities.hasLevelCooldown(vil, plugin)) VillagerUtilities.setLevelCooldown(vil, plugin, (long)0);


        Player player = (Player) event.getPlayer();

        villagerLevelManager.call(vil, player);
    }
}


