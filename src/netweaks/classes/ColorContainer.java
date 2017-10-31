package netweaks.classes;

import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;

//ColorContainer class
//Represents a block, which have a color.
public class ColorContainer {
	Block block;
	DyeColor color;
	ItemStack item;
	Player p;
	
	@SuppressWarnings("deprecation")
	public ColorContainer(Block m, ItemStack item, Player p) {
		this.block = m;
		this.item = item;
		this.p = p;
		if(item.getType() == Material.INK_SACK) {
			color = new Dye(item.getType(), item.getData().getData()).getColor();
		} else {
			color = null;
		}
		
		if(color != null) {
			Wool wool = new Wool(color);
			if(block.getType() == Material.GLASS) block.setType(Material.STAINED_GLASS);
			if(block.getType() == Material.THIN_GLASS) block.setType(Material.STAINED_GLASS_PANE);
			if(wool.getData() != block.getData()) {
				block.setData(wool.getData());
				if(p.getGameMode() != GameMode.CREATIVE) {
					item.setAmount(item.getAmount() - 1);
				}
			}
		}
	}
}