/*
    MakeEmSpawn - This CraftBukkit plugin allows you to spawn entities from eggs
    Copyright (C) 2013  Hybris95

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.hybris.bukkit.makeemspawn;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

import org.bukkit.plugin.Plugin;

public class MakeEmSpawn extends JavaPlugin{
    
	private MakeEmSpawnPlayerListener sMPL;
	private Logger log;
	
	public void onLoad(){}
	
	
	public void onEnable(){
		this.log = this.getServer().getLogger();
		log.info("[MakeEmSpawn] Loading...");
		this.sMPL = new MakeEmSpawnPlayerListener(this);
		this.getServer().getPluginManager().registerEvents(this.sMPL, this);
		log.info("[MakeEmSpawn] Loaded!");
	}
	
	public void onDisable(){
		log.info("[MakeEmSpawn] Disabled!");
		this.log = null;
		HandlerList handler = new HandlerList();
		handler.unregister(this);
		handler.unregister(this.sMPL);
		this.sMPL = null;
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