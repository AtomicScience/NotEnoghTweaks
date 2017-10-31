package netweaks.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import netweaks.stuff.*;

public class Main extends JavaPlugin {
	WorldGuardPlugin wg = null;
	File db_raw = new File(this.getDataFolder(), "players.db");
	File locale_raw = new File(this.getDataFolder(), "locale.yml");
	public BukkitScheduler scheduler = getServer().getScheduler();
	private FileConfiguration db;
	private FileConfiguration locale;
	
	public void onEnable() {
		loadConfiguration();
		
		checkWorldGuard();
		getServer().getPluginManager().registerEvents(new BlocksListener(this), this);
		getServer().getPluginManager().registerEvents(new CountInfo(this), this);
		getServer().getPluginManager().registerEvents(new TipsListener(this), this);
		getLogger().info("Plugin loaded!");
	}
	
	public JavaPlugin getPluginObject() {
		return this;
	}
	
	@EventHandler
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandListener l = new CommandListener(sender, command, label, args, this);
		return l.RunCommand();
	}
	
	//Database:
	public FileConfiguration loadDb() {
		if(!db_raw.exists()) {
			try {
				getLogger().log(Level.WARNING, "No 'players.db' file found, creating a blank one...");
				if(db_raw.createNewFile()) {
					getLogger().log(Level.INFO, "New 'players.db' created succesfully!");
				}
			} catch(Exception e) {
				getLogger().log(Level.SEVERE, "Error occurred while creating a new blank 'players.db' file");
				getLogger().log(Level.SEVERE, e.getMessage());
				this.getPluginLoader().disablePlugin(this);
			}
		}
		FileConfiguration db = new YamlConfiguration();
		try {
			db.load(db_raw);
		} catch(InvalidConfigurationException e) {
			getLogger().log(Level.SEVERE, "Error occurred while attempting to read 'player.db' file! File damaged!");
			getLogger().log(Level.SEVERE, e.getMessage());
			//Creating a database copy
			InputStream is = null;
		    OutputStream os = null;
		    getLogger().log(Level.WARNING, "Creating a blank 'players.db' file instead of damaged one. Old file will be moved to 'failure_players.db' When you'll fix the problem you can install the old file again");
		    try {
		        is = new FileInputStream(db_raw);
		        os = new FileOutputStream(new File(this.getDataFolder(), "failure_players.db"));
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		        is.close();
		        os.close();
		        db_raw.delete();
		        db_raw.createNewFile();
		    } catch(Exception ex) {
		    	getLogger().log(Level.SEVERE, "Error occurred while attempting to create a copy of 'player.db' file! Stopping the plugin to save the data...");
		    	this.getPluginLoader().disablePlugin(this);
		    }
		} catch(Exception e) {
			getLogger().log(Level.SEVERE, "Error occurred while attempting to read 'player.db' file! Unable to access the file! Stopping the plugin to save the data...");
			getLogger().log(Level.SEVERE, e.getMessage());
			this.getPluginLoader().disablePlugin(this);
		}
		
		return db;
	}
	
	public FileConfiguration getDb() {
		return db;
	}
	
	//Locales:
	public FileConfiguration loadLocale() {
		if(!locale_raw.exists()) {
			getLogger().log(Level.WARNING, "No 'locale.yml' file found, loading defaults");
			if(Tools.loadDefaults("locale.yml", locale_raw, this)) {
				getLogger().log(Level.INFO, "Default 'locale.yml' loaded succesfully!");
			} else {
				getLogger().log(Level.SEVERE, "Something went wrong while attempting to load 'locale.yml'! Check if " + this.getFile().getName() + " contains needed file");
				this.getPluginLoader().disablePlugin(this);
			}
		}
		FileConfiguration db = new YamlConfiguration();
		try {
			db.load(locale_raw);
		} catch(InvalidConfigurationException e) {
			getLogger().log(Level.SEVERE, "§cError occurred while attempting to read 'locale.yml' file! Wrong YAML format!");
			getLogger().log(Level.SEVERE, e.getMessage());
			//Creating a translation copy
			InputStream is = null;
		    OutputStream os = null;
		    getLogger().log(Level.WARNING, "Loading a default 'locale.yml' file instead of damaged one. Old file will be moved to 'failure_locale.yml'");
		    try {
		        is = new FileInputStream(locale_raw);
		        os = new FileOutputStream(new File(this.getDataFolder(), "failure_locale.yml"));
		        byte[] buffer = new byte[1024];
		        int length;
		        String comment = "#'failure_locale.yml' file from " + new Date().toString() + "\n" + "#Reason of failure: \n" + e.getMessage() + "\n" + "#End of the reason (don't forget to delete all this shit before starting) \n\n";
				os.write(comment.getBytes(), 0, comment.getBytes().length);	        
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		        is.close();
		        os.close();
		        locale_raw.delete();
		        locale_raw.createNewFile();
		        if(!Tools.loadDefaults("locale.yml", locale_raw, this)) throw new Exception();
		    } catch(Exception ex) {
		    	getLogger().log(Level.SEVERE, "Error occurred while attempting to load default 'locale.yml' file! Stopping the plugin because of no translation file...");
		    	this.getPluginLoader().disablePlugin(this);
		    }
		} catch(Exception e) {
			getLogger().log(Level.SEVERE, "Error occurred while attempting to read 'locale.yml' file! Unable to access the file! Stopping the plugin to save the data...");
			getLogger().log(Level.SEVERE, e.getMessage());
			this.getPluginLoader().disablePlugin(this);
		}		
		return db;
	}
	
	public FileConfiguration getLocale() {
		return locale;
	}
	
	public WorldGuardPlugin getWg() {
		return this.wg;
	}
	
	private void checkWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	    // WorldGuard may not be loaded
	    if (plugin != null || plugin instanceof WorldGuardPlugin) {
	        this.wg = (WorldGuardPlugin) plugin;
	        getLogger().log(Level.INFO, "Succesfully hooked up with WorldGuard!");
	    } else {
	    	getLogger().log(Level.WARNING, "------------------------------------------");
	    	getLogger().log(Level.WARNING, "Hooking up with WorldGuard failed.");
	    	getLogger().log(Level.WARNING, "Probably, it's because you don't use WorldGuard at all, and it means that players can use NotEnoughTweaks to harm your server! (e.g. recolor wool blocks at your spawn)");
	    	getLogger().log(Level.WARNING, "If you use another grief-protection plugin it's higly recomend to you to stop the server and disable this plugin");
	    	getLogger().log(Level.WARNING, "------------------------------------------");
	    }
	}
	
	private void loadConfiguration() {
		 getLogger().log(Level.INFO, "Loading the configuration files of the plugin...");
		 db = loadDb();
		 locale = loadLocale();
	     getConfig().options().copyDefaults(true);
	     
	     // setting a boolean value
	     this.getConfig().set("path.to.boolean", true);

	     // setting a String
	     String stringValue = "Hello World!";
	     this.getConfig().set("path.to.string", stringValue);

	     // setting an int value
	     int integerValue = 8;
	     this.getConfig().set("path.to.integer", integerValue);

	     // Setting a List of Strings
	     // The List of Strings is first defined in this array
	     List<String> listOfStrings = Arrays.asList("Hello World", "Welcome to Bukkit", "Have a Good Day!");
	     this.getConfig().set("path.to.list", listOfStrings);

	     // Setting a vector
	     // event is assumed to be an existing event inside an "onEvent" method.
	     Vector vector = new Vector(0, 100, 0);
	     this.getConfig().set("path.to.vector", vector);

	     // Erasing a value
	     this.getConfig().set("path.to.value", null);
	}
	
	private Map<String, String> list = new HashMap<String, String>();
	
	public String getPlayerString(String playerName) {
		return list.get(playerName);
	}
	
	public void onDisable()
	{
		saveConfig();
		try {
			db.save(db_raw);
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Error occurred while attempting to save 'player.db' file!");
			getLogger().log(Level.SEVERE, e.getMessage());
		}
		getLogger().info("Plugin stopped and unloaded succesfuly!");
	}

}


