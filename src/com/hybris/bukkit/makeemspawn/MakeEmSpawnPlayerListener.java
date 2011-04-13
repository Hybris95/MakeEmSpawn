package com.hybris.bukkit.makeemspawn;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerEvent;
//import org.bukkit.event.player.PlayerMoveEvent;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.World;
import java.util.List;

import org.bukkit.entity.CreatureType;

import java.util.HashMap;

class MakeEmSpawnPlayerListener extends PlayerListener{
	
	private MakeEmSpawn plugin;
	private HashMap<String,String> makeemspawners;
	private HashMap<String,Byte> numbers;
	
	MakeEmSpawnPlayerListener(MakeEmSpawn plugin){
		this.plugin = plugin;
		this.makeemspawners = new HashMap<String,String>();
		this.numbers = new HashMap<String,Byte>();
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		
		if(event.isCancelled()){return;}
		
		String command = event.getMessage();
		Player player = event.getPlayer();
		boolean isMakeEmSpawner = makeemspawners.containsKey(player.getName());
		
		if(command.toLowerCase().startsWith("/makeemspawn") || command.toLowerCase().startsWith("/mes")){
			
			String[] split = command.split(" ", 4);
			
			if(split.length < 2){
				printUsage(player);
				return;
			}
			
			String option = split[1];
			int optionInt = giveOptionInt(option);
			
			switch(optionInt){
				case 0:				
				default:
					printUsage(player);
					break;
				
				case 1:// makeemspawn
					
					if(split.length < 3){
						printUsage(player);
						return;
					}
					
					String possibleNumberOfMobs = "1";
					byte numberOfMobs = (byte)(-1);
					if(split.length >= 4){
						possibleNumberOfMobs = split[3];
						try{
							numberOfMobs = Byte.parseByte(possibleNumberOfMobs);
							if(numberOfMobs < (byte)1){
								printUsage(player);
								return;
							}
						}
						catch(NumberFormatException e){
							printUsage(player);
							return;
						}
					}
					
					String mobName = split[2];
					String creatureType = creatureType(mobName, player);
					if(creatureType.equals("")){
						printUsage(player);
						return;
					}
					
					makeemspawners.put(player.getName(),creatureType);
					if(isMakeEmSpawner){
						player.sendMessage("Next egg launched will spawn this mob: " + mobName);
					}
					else{
						if(giveEgg(player)){
							player.sendMessage("Next egg launched will spawn this mob: " + mobName);
						}
						else{
							player.sendMessage("Get some place for an egg!");
							makeemspawners.remove(player.getName());
						}
					}
					
					if(numberOfMobs != (byte)(-1)){
						player.sendMessage("Next eggs launched will spawn " + numberOfMobs + " at once");
						numbers.put(player.getName(),numberOfMobs);
					}
					break;
				
				case 2:// unmakeemspawn
					if(!this.plugin.hasPermissions(player, "spawn")){
						player.sendMessage("You cannot use this command");
						return;
					}
					
					if(isMakeEmSpawner){
						removeEgg(player);
						makeemspawners.remove(player.getName());
					}
					
					List<World> worlds = this.plugin.getServer().getWorlds();
					for(World world : worlds){
						List<LivingEntity> entities = world.getLivingEntities();
						for(LivingEntity entity : entities){
							if(!HumanEntity.class.isAssignableFrom(entity.getClass())){
								entity.setHealth(0);
							}
						}
					}
					player.sendMessage("You are not makeemspawned (anymore) and the worlds got cleaned from creatures");
					break;
			}
			event.setCancelled(true);
		}
	}
	
	private String creatureType(String mobName, Player player){
		String creatureType = mobName.toUpperCase();
		mobName = mobName.toLowerCase();
		if(plugin.hasPermissions(player, mobName)){
			if(mobName.equals("chicken")){}
			else if(mobName.equals("cow")){}
			else if(mobName.equals("creeper")){}
			else if(mobName.equals("ghast")){}
			else if(mobName.equals("giant")){}
			else if(mobName.equals("pig")){}
			else if(mobName.equals("pigzombie")){
				creatureType = "pig_zombie";
			}
			else if(mobName.equals("sheep")){}
			else if(mobName.equals("skeleton")){}
			else if(mobName.equals("slime")){}
			else if(mobName.equals("spider")){}
			else if(mobName.equals("squid")){}
			else if(mobName.equals("wolf")){}
			else if(mobName.equals("zombie")){}
			else if(mobName.equals("monster")){} // TOTEST
			else{
				player.sendMessage("Unsupported Creature");
				creatureType = "";
			}
		}
		else{
			player.sendMessage("Unsupported Creature or Unsufficent permissions");
			creatureType = "";
		}
		return creatureType;
	}
	
	private boolean giveEgg(Player player){
		PlayerInventory inv = player.getInventory();
		HashMap<Integer,ItemStack> couldntFit = inv.addItem(new CraftItemStack(344, 1)); // 344 = Egg Id
		if(couldntFit.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean removeEgg(Player player){
		PlayerInventory inv = player.getInventory();
		HashMap<Integer,ItemStack> couldntBeRemoved = inv.removeItem(new CraftItemStack(344, 1));
		if(couldntBeRemoved.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}
	
	private int giveOptionInt(String option){
		if(option.startsWith("s") || (option.startsWith("spawn"))){
			return 1;
		}
		else if(option.startsWith("us") || (option.startsWith("unspawn"))){
			return 2;
		}
		return 0;
	}
	
	private void printUsage(Player player){
		player.sendMessage("/<makeemspawn|mes> <<s|spawn>|<us|unspawn>> [<mobname> [numberOfMobs]]");
		player.sendMessage("mobnames:");
		player.sendMessage("chicken|cow|pig|sheep|creeper|skeleton|spider|zombie|squid");
	}
	
	public void onPlayerEggThrow(PlayerEggThrowEvent event){		
		Player player = event.getPlayer();
		if(makeemspawners.containsKey(player.getName())){
			event.setHatchType(CreatureType.valueOf(makeemspawners.get(player.getName()).toUpperCase()));
			event.setHatching(true);
			
			Byte number = numbers.get(player.getName());
			if(number != null){
				event.setNumHatches(number);
			}
			else{
				event.setNumHatches((byte)1);
			}
			makeemspawners.remove(player.getName());
		}
	}
	
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if(event.isCancelled()){return;}
		
		Player player = event.getPlayer();
		if(makeemspawners.containsKey(player.getName())){
			ItemStack items = event.getItemDrop().getItemStack();
			if(items.getTypeId() == 344){ // 344 = Egg Id
				makeemspawners.remove(player.getName());
				player.sendMessage("You are not a makeemspawner anymore");
				event.setCancelled(true);
				removeEgg(player); // TODO Bugs yet (maybe because since its the PlayerDropItemEvent the Item isn't in the inventory just while this event)
			}
		}
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event){	
		Player player = event.getPlayer();
		if(makeemspawners.containsKey(player.getName())){
			giveEgg(player);
			// TODO Complexify with multiworld support
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		
		if(makeemspawners.containsKey(player.getName())){
			makeemspawners.remove(event.getPlayer().getName());
			removeEgg(player);
		}
	}
	
	/*public void onPlayerTeleport(PlayerMoveEvent event){
		// TODO Complexify with multiworld support
	}*/
	
}