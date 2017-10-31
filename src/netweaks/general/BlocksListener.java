package netweaks.general;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Furnace;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_11_R1.EntityArmorStand;
import net.minecraft.server.v1_11_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_11_R1.WorldServer;

import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import netweaks.classes.BrokenTool;
import netweaks.classes.ColorContainer;
import netweaks.stuff.Tools;

//BlocksListener class
//Handles all needed Blocks-related events
public class BlocksListener implements Listener {
	private final Main plugin;
	
	@SuppressWarnings("unused")
	private BlocksListener() {
		plugin = null;
	}
	
	public BlocksListener(Main plugin) {
		this.plugin = plugin;
	}
	
	//Tool's breaking handling (suddenly)
	@EventHandler
	public void PlayerItemBreakEvent(PlayerItemBreakEvent e) {
		BrokenTool t = new BrokenTool(e);
	}
	
	//Automatic block change
	public void onBlockPlace(BlockPlaceEvent e) {
		boolean right_gm = e.getPlayer().getGameMode().name() == "SURVIVAL";
		if(e.getItemInHand().getAmount() == 1 && right_gm) {
			PlayerInventory inv = e.getPlayer().getInventory();
			if(e.getHand() == EquipmentSlot.HAND) {
				inv.setItemInMainHand(null);
			}
			
			else {
				inv.setItemInOffHand(null);
			}
			
			//Calculating...
			if(e.getPlayer().getInventory().contains(e.getItemInHand().getType())) {
				if(e.getHand() == EquipmentSlot.HAND) {
					inv.setItemInMainHand(null);
				}
				
				else {
					inv.setItemInOffHand(null);
				}
				int close_stack = inv.first(e.getItemInHand().getType());
				ItemStack new_hand = inv.getItem(close_stack);
				inv.clear(close_stack);
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP,(float) 2.00,(float) 1.00);
				if(e.getHand() == EquipmentSlot.HAND) {
					inv.setItemInMainHand(new_hand);
				}
				
				else {
					inv.setItemInOffHand(new_hand);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerInteractEvent(PlayerInteractEvent e) throws Exception {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
		if(plugin.getWg() != null) { if(plugin.getWg().canBuild(e.getPlayer(), e.getClickedBlock())) {
		Material clicked_type = e.getClickedBlock().getType();
		Block block = e.getClickedBlock();
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {						
			//Colorable blocks
			if((clicked_type  == Material.WOOL || clicked_type  == Material.GLASS || clicked_type  == Material.STAINED_GLASS || clicked_type  == Material.STAINED_CLAY || clicked_type  == Material.CARPET || clicked_type  == Material.STAINED_GLASS_PANE|| clicked_type  == Material.THIN_GLASS) & e.getItem() != null) {
				new ColorContainer(block, item, player);
			}
			
			//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			//Crop's harvesting
			if(clicked_type == Material.CROPS) {
				if(block.getData() == (byte) 7) {
					block.setType(Material.CROPS);
					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.SEEDS, 1 + (int) (Math.random() * 2)));
					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.WHEAT, 1));
				}
			}
			if(clicked_type == Material.CARROT) {
				if(block.getData() == (byte) 7) {
					block.setType(Material.CARROT);
					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.CARROT_ITEM, 1 + (int) (Math.random() * 3)));
				}
			}
			if(clicked_type == Material.POTATO) {
				if(block.getData() == (byte) 7) {
					block.setType(Material.POTATO);
					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.POTATO_ITEM, 1 + (int) (Math.random() * 3)));
				}
			}
			if(clicked_type == Material.BEETROOT_BLOCK) {
				if(block.getData() == (byte) 3) {
					block.setType(Material.BEETROOT_BLOCK);
					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.BEETROOT, 1));
					block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.BEETROOT_SEEDS, 1 + (int) (Math.random() * 3)));
					}
			}
			//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
		}
		
		}
		if(e.getAction() == Action.RIGHT_CLICK_AIR) {
			e.getItem().setDurability((short) 1650);
		}
	} else {
		//e.setCancelled(true);
		
		//e.getPlayer().sendMessage(plugin.getLocale().getString("messages.not_allowed_operation", "You are not permitted to do that!"));
	}
		}
	}
}
