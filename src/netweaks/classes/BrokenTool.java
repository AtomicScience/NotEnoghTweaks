package netweaks.classes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

//BrokenTool Class
//Handles the PlayerItemBreakEvent data and performs actions of 	
//tool's autoreplacement
//ƒ¿ƒ¿ƒ¿, ﬂ Õ≈ Àﬁ¡Àﬁ Ã≈“Œƒ€
public class BrokenTool {
	private PlayerItemBreakEvent e;

	@SuppressWarnings("deprecation")
	public BrokenTool(PlayerItemBreakEvent e) {
		this.e = e;
		int slot = this.checkTool();
		if(slot != -1) {
			e.getPlayer().setItemInHand(e.getPlayer().getInventory().getItem(slot));
			e.getPlayer().sendMessage(e.getPlayer().getInventory().getItem(slot).toString());
			e.getPlayer().getInventory().setItem(slot, null);
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP,(float) 2.00,(float) 1.00);
		}
	}
	
	//Let's check if the player has a suitable tool in his inventory
	//@returns the number of slot with suitable tool
	public int checkTool() {		
		//Tool's checking
		//-=-=-=-=-=
		int slot = -1;
		if(e.getBrokenItem().getType().name().contains("_PICKAXE")) {
			slot = e.getPlayer().getInventory().first(Material.DIAMOND_PICKAXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.IRON_PICKAXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.GOLD_PICKAXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.STONE_PICKAXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.WOOD_PICKAXE);
		}
		if(e.getBrokenItem().getType().name().contains("_AXE")) {
			//e.getPlayer().sendMessage("AXE");
			slot = e.getPlayer().getInventory().first(Material.DIAMOND_AXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.IRON_AXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.GOLD_AXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.STONE_AXE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.WOOD_AXE);
		}
		if(e.getBrokenItem().getType().name().contains("_SWORD")) {
			//e.getPlayer().sendMessage("SWORD");
			slot = e.getPlayer().getInventory().first(Material.DIAMOND_SWORD);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.IRON_SWORD);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.GOLD_SWORD);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.STONE_SWORD);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.WOOD_SWORD);
		}
		if(e.getBrokenItem().getType().name().contains("_SPADE")) {
			//e.getPlayer().sendMessage("SHOWEL");
			slot = e.getPlayer().getInventory().first(Material.DIAMOND_SPADE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.IRON_SPADE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.GOLD_SPADE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.STONE_SPADE);
			if(slot == -1) slot = e.getPlayer().getInventory().first(Material.WOOD_SPADE);
		}
		
		//It don't work and I don't know why :(
		//if(e.getBrokenItem().getType() == Material.FISHING_ROD) slot = e.getPlayer().getInventory().first(Material.FISHING_ROD);
		//if(e.getBrokenItem().getType() == Material.FLINT_AND_STEEL) slot = e.getPlayer().getInventory().first(Material.FLINT_AND_STEEL);
		//if(e.getBrokenItem().getType() == Material.SHEARS) slot = e.getPlayer().getInventory().first(Material.SHEARS);
		
		
		//But what if found item is the broken item?
		if(slot != -2 && slot != -1) {
			if(e.getPlayer().getInventory().getItem(slot).equals(e.getBrokenItem())) {
				e.getPlayer().getInventory().setItem(slot, null);
				new BrokenTool(e);
				return -1;
			}
		}
		if(slot == -2) return -1; else return slot;
	}

}
