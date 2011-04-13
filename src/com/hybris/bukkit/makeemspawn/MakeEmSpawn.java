package com.hybris.bukkit.makeemspawn;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;

import org.bukkit.plugin.Plugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class MakeEmSpawn extends JavaPlugin{
	
	private MakeEmSpawnPlayerListener sMPL;
	private PluginManager pluginManager;
	private Logger log;
	
	private PermissionHandler permissions = null;
	
	public void onLoad(){}
	
	
	public void onEnable(){
		this.log = this.getServer().getLogger();
		log.info("[MakeEmSpawn] Loading...");
		this.sMPL = new MakeEmSpawnPlayerListener(this);
		this.pluginManager = this.getServer().getPluginManager();
		loadPermissions();
		
		pluginManager.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, sMPL, Priority.Normal, this);
		pluginManager.registerEvent(Event.Type.PLAYER_DROP_ITEM, sMPL, Priority.Normal, this);
		pluginManager.registerEvent(Event.Type.PLAYER_EGG_THROW, sMPL, Priority.Normal, this);
		pluginManager.registerEvent(Event.Type.PLAYER_RESPAWN, sMPL, Priority.Normal, this);
		pluginManager.registerEvent(Event.Type.PLAYER_QUIT, sMPL, Priority.Normal, this);
		log.info("[MakeEmSpawn] Loaded!");
	}
	
	public void onDisable(){
		log.info("[MakeEmSpawn] Disabled!");
		this.log = null;
		this.sMPL = null;
		this.pluginManager = null;
		this.permissions = null;
	}
    
	private void loadPermissions() {
		Plugin test = pluginManager.getPlugin("Permissions");
			
		if (this.permissions == null) {
			if (test != null) {
				pluginManager.enablePlugin(test);
				permissions = ((Permissions) test).getHandler();
				log.info("[MakeEmSpawn] successfully loaded Permissions.");
			}
			else {
				log.info("[MakeEmSpawn] not using Permissions. Permissions not detected");
			}
		}
	}
	
	boolean usesPermissions(){
		if(this.permissions == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	boolean hasPermissions(Player player, String node){
		boolean toReturn = false;
		String realNode = "makeemspawn." + node;
		if(usesPermissions()){
			toReturn = this.permissions.has(player, realNode);
		}
		else{
			toReturn = player.isOp();
		}
		
		return toReturn;
	}
	
}