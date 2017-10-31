package netweaks.general;

import netweaks.general.Main;
import netweaks.stuff.Tools;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CountInfo implements Listener {
	private final Main plugin;
	
	@SuppressWarnings("unused")
	private CountInfo() {
		plugin = null;
	}
	
	public CountInfo(Main plugin) {
		this.plugin = plugin;
	}

	
	@EventHandler
	public void onBlockPlace(PlayerInteractEvent e){
		if(e.getAction() != Action.PHYSICAL) {
			if(e.getItem() != null) {
				Tools.countSend(e.getPlayer(), e.getMaterial());
				}		
			}
		}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHeld(PlayerItemHeldEvent e){
		try {
			Tools.countSend(e.getPlayer(), e.getPlayer().getInventory().getItem(e.getNewSlot()).getType());
		} catch(Exception ex) {
			
		}
	}
}
