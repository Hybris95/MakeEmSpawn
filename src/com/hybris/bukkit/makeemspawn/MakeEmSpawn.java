package com.hybris.bukkit.makeemspawn;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import org.bukkit.plugin.Plugin;

public class MakeEmSpawn extends JavaPlugin{
	
	private MakeEmSpawnPlayerListener sMPL;
	private PluginManager pluginManager;
	private Logger log;
	
	public void onLoad(){}
	
	
	public void onEnable(){
		this.log = this.getServer().getLogger();
		log.info("[MakeEmSpawn] Loading...");
		this.sMPL = new MakeEmSpawnPlayerListener(this);
		this.pluginManager = this.getServer().getPluginManager();
		
		pluginManager.registerEvent(EventType.PLAYER_COMMAND_PREPROCESS, sMPL, EventPriority.NORMAL, this);
		pluginManager.registerEvent(EventType.PLAYER_DROP_ITEM, sMPL, EventPriority.NORMAL, this);
		pluginManager.registerEvent(EventType.PLAYER_EGG_THROW, sMPL, EventPriority.NORMAL, this);
		pluginManager.registerEvent(EventType.PLAYER_RESPAWN, sMPL, EventPriority.NORMAL, this);
		pluginManager.registerEvent(EventType.PLAYER_QUIT, sMPL, EventPriority.NORMAL, this);
		log.info("[MakeEmSpawn] Loaded!");
	}
	
	public void onDisable(){
		log.info("[MakeEmSpawn] Disabled!");
		this.log = null;
		this.sMPL = null;
		this.pluginManager = null;
	}
	
	boolean hasPermissions(Player player, String node){
		boolean toReturn = false;
		String realNode = "makeemspawn." + node;
        toReturn = player.hasPermission(realNode);
        if(!toReturn)
        {
		    toReturn = player.isOp();
        }		
		return toReturn;
	}
	
}