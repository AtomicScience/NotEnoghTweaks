package netweaks.stuff;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import netweaks.general.*;

//Class Tools
//Some usefull stuffs
public class Tools {
	/*/public static void sendAction(Player player, String msg) {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a((String) ("{\"text\": \"" + msg + "\"}"));
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        p.getHandle().playerConnection.sendPacket((net.minecraft.server.v1_11_R1.Packet<?>) ppoc);
    }*/
	
	public static boolean loadDefaults(String source, File dest, Plugin plugin) {
		try {
			OutputStream os = new FileOutputStream(dest);
			InputStream is = plugin.getResource(source);
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = is.read(buffer)) > 0) {
        		os.write(buffer, 0, length);
        	}
        	is.close();
        	os.close();
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static String firstUpperCase(String word) {
		if(word == null || word.isEmpty()) return "";//или return word;
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}
	
	public static void countSend(Player p, Material m){
		String item_name = Tools.firstUpperCase(m.name().replaceAll("_", " ").toLowerCase());
		int count = 0;
		HashMap<Integer,? extends ItemStack> l = p.getInventory().all(m);
		for (Integer key : l.keySet()) {
			count = count + l.get(key).getAmount();
		}
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.BLUE + "| " + ChatColor.GOLD + ChatColor.BOLD + item_name + ChatColor.BLUE + " x " + ChatColor.DARK_GREEN + Integer.toString(count) + ChatColor.BLUE + " |"));
	}
}
