package netweaks.general;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import netweaks.stuff.Tools;

import netweaks.classes.BrokenTool;

//TipsListener class
//Listens some Events which can trigger a tip
public class TipsListener implements Listener {
	private Main plugin;
	private FileConfiguration db;
	
	public TipsListener(Main main) {
		plugin = main;
		db = main.getDb();
	}
	
	@EventHandler
	public void PlayerItemBreakEvent(PlayerItemBreakEvent e) {
		if(!plugin.getDb().getBoolean("players." + e.getPlayer().getName() + ".tips.toolchange", false)) {
			showTip(e.getPlayer(), "tool_autochange");
			plugin.getDb().set("players." + e.getPlayer().getName() + ".tips.toolchange", true);
		}
	}
	
	@EventHandler
	public void onItemCraft(CraftItemEvent e) {
		if(!plugin.getDb().getBoolean("players." + e.getWhoClicked().getName() + ".tips.color", false)) {
		if(e.getCurrentItem().getType().toString().contains("GLASS") || e.getCurrentItem().getType().toString().contains("WOOL") || e.getCurrentItem().getType().toString().contains("CARPET")) {
			showTip((Player) e.getWhoClicked(), "color_crafting");
			plugin.getDb().set("players." + e.getWhoClicked().getName() + ".tips.color", true);
		}
	}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(!plugin.getDb().getBoolean("players." + e.getPlayer().getName() + ".tips.crops", false)) {
			if((e.getBlock().getType().equals(Material.CROPS) || e.getBlock().getType().equals(Material.CARROT) || e.getBlock().getType().equals(Material.POTATO) || e.getBlock().getType().equals(Material.BEETROOT_BLOCK)) && e.getBlock().getData() == 7) {
				showTip(e.getPlayer(), "crops_harvesting");
				plugin.getDb().set("players." + e.getPlayer().getName() + ".tips.crops", true);
			}
		}
	}
	
	//showTip method
	public void showTip(Player p, String name) {
		if(!plugin.getLocale().getString("tips." + name).equals("none")) {
			//if(!plugin.getLocale().getString("tips.static.action_bar_text").equals("none")) Tools.sendAction((Player) p, ChatColor.BLUE + plugin.getLocale().getString("tips.static.action_bar_text", "§bNew hint unlocked!"));
			if(plugin.getLocale().getBoolean("tips.static.sound", false)) p.getWorld().playSound(p.getEyeLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 1.0f, 1.0f);
			if(!plugin.getLocale().getString("tips.static.upper_separator").equals("none")) p.sendMessage(plugin.getLocale().getString("tips.static.upper_separator", "---------------------------------------------------------"));
			p.sendMessage(plugin.getLocale().getString("tips." + name, "No text found for this hint, please contact server's administrator and send name of the tweak: " + name.toUpperCase()));
			if(!plugin.getLocale().getString("tips.static.lower_separator").equals("none")) p.sendMessage(plugin.getLocale().getString("tips.static.lower_separator", "---------------------------------------------------------"));
		}
	}
}
