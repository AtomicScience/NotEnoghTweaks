package netweaks.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

//CommandListener class
//Handles the commands
public class CommandListener {
	private final Main plugin;
	
	CommandSender sender;
	Command command;
	String label;
	String[] args;
	
	
	public CommandListener(CommandSender sender, Command command, String label, String[] args, Main plugin) {
		this.sender = sender;
		this.command = command;
		this.label = label;
		this.args = args;
		this.plugin = plugin;
	}
	
	public boolean RunCommand() {
		if(command.getName().equals("tweak")) {
			sender.sendMessage("ÛSSS");
			return true;
		}
		
		if(command.getName().equals("g")) {
			try {
				int i = Integer.parseInt(args[0]);
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (sender.getName().equals(p.getName())) { }
					return true;
				}
			}
			catch (Exception e) {
				sender.sendMessage("Wrong argument! I can get only numbers!");
				return true;
			}
		}
	return false;
}
}
