package com.resbah.WirelessChests;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



public class WirelessChestsCommandExecutor implements CommandExecutor {
	private WirelessChests plugin;
	public WirelessChestsCommandExecutor(WirelessChests plugin){
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (cmd.getName().equalsIgnoreCase("wcrem")){
			if (player == null) {
				sender.sendMessage("this command can only be run by a player");
			} else {
				if(player.hasPermission("wirelesschests.remove")){
	    		if(args.length == 2){
	    			String chest = args[0];
	    			String group = args[1];
	    			plugin.getConfig().set("group."+group+"."+chest, null);
	    			player.sendMessage("Chest removed successfully!");
	    			if(plugin.getConfig().getString("defaults."+group) == chest){
	    				plugin.getConfig().set("defaults."+group,null);
	    				player.sendMessage("Chest was a Main chest. Removed from Main Chest List Successfully!");
	    			}
	    			plugin.saveConfig();
	    			plugin.reloadConfig();
	    		}
			}else{
				player.sendMessage("You do not have permission to preform this command!");
			}
			}
			return true;
		}
			
		if (cmd.getName().equalsIgnoreCase("wcremgroup")){
			if (player == null) {
				sender.sendMessage("this command can only be run by a player");
			} else {
				if(player.hasPermission("wirelesschests.removegroup")){
	    		if(args.length == 1){
	    			String group = args[1];
	    			plugin.getConfig().set("group."+group, null);
	    			plugin.getConfig().set("defaults."+group,null);
	    			player.sendMessage("Group removed successfully!");
	    		}
	    			plugin.saveConfig();
	    			plugin.reloadConfig();
	    		}else{
					player.sendMessage("You do not have permission to preform this command!");
				}
			}
			return true;
			}
			
		if (cmd.getName().equalsIgnoreCase("wcabout")){
			if(player.hasPermission("wirelesschests.about")){
				sender.sendMessage("WirelessChests by resba");
				sender.sendMessage("Version 0.0.3-ALPHA");
				sender.sendMessage("https://www.github.com/resba/WirelessChests");
			}else{
				player.sendMessage("You do not have permission to preform this command!");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("wcsetmain")){
			if (player == null) {
				sender.sendMessage("this command can only be run by a player");
			} else {
				if(player.hasPermission("wirelesschests.setmain")){
	    		if(args.length == 2){
	    			String group = args[1];
	    			String chest = args[0];
	    			if(plugin.getConfig().get("group."+group) != null){
	    				if(plugin.getConfig().get("group."+group+"."+chest) != null){
	    					plugin.getConfig().set("defaults."+group, chest);
	    					player.sendMessage("Done!");
	    					plugin.saveConfig();
	    					plugin.reloadConfig();
	    				}else{
	    					player.sendMessage("Chest does not exist!");
	    				}
	    			}else{
	    				player.sendMessage("Group does not exist!");
	    			}
	    			
	    		}
				}else{
					player.sendMessage("You do not have permission to preform this command!");
				}
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("wcsync")){
			if (player == null) {
				sender.sendMessage("this command can only be run by a player");
			} else {
				if(player.hasPermission("wirelesschests.sync")){
			if(args.length == 1){
				String group = args[0];
				if(plugin.getConfig().getString("defaults."+group) != null){
				String dc = plugin.getConfig().getString("defaults."+group);
				int dx = plugin.getConfig().getInt("group."+group+"."+dc+".x");
				int dy = plugin.getConfig().getInt("group."+group+"."+dc+".y");
				int dz = plugin.getConfig().getInt("group."+group+"."+dc+".z");
				World dw = player.getWorld();
				Block db = dw.getBlockAt(dx, dy, dz);
				Chest dchst = (Chest)db.getState();
				Inventory maininv = dchst.getInventory();
				ItemStack[] mainstack = maininv.getContents();
				
				
					Set<String> keys = plugin.getConfig().getConfigurationSection("group."+group).getKeys(false);
					Iterator<String> iter = keys.iterator();
						while (iter.hasNext()) {
							String cnames = iter.next();
							int x = plugin.getConfig().getInt("group."+group+"."+cnames+".x");
							int y = plugin.getConfig().getInt("group."+group+"."+cnames+".y");
							int z = plugin.getConfig().getInt("group."+group+"."+cnames+".z");
							World w = player.getWorld();
							Block b = w.getBlockAt(x, y, z);
							Chest chst = (Chest)b.getState();
							Inventory tchst = chst.getInventory();
							tchst.setContents(mainstack);
							player.sendMessage(cnames+" Synced with Main Chest "+dc+".");
					}
				}
				
				
				}else{
					player.sendMessage("Error! You haven't set a Main chest yet. Do so with /wcsync [chest name] [group name]");
				}
				
			}else{
				player.sendMessage("You do not have permission to preform this command!");
			}
			}
				
			return true;
			}

		if(cmd.getName().equalsIgnoreCase("wcaddchest")){
			if(player == null) {
				sender.sendMessage("This command can only be run by a player.");
				return false;
			}else{
				if(player.hasPermission("wirelesschests.add")){
			if(args.length == 2){
			String chestname = args[0];
			String groupname = args[1];
			Location loc = player.getLocation();
			World w = loc.getWorld();
			loc.setX(loc.getX() - 1);
			Block b = w.getBlockAt(loc);
			int bid = b.getTypeId();
			if(bid!=54){
			b.setType(Material.CHEST);
			}
			player.sendMessage("Created chest "+chestname+" inside group "+groupname+"!");
			plugin.getConfig().set("chest." + chestname, chestname);
			if (plugin.getConfig().get("group." + groupname) == null){
			plugin.getConfig().set("group." + groupname, true);
			}
			plugin.getConfig().set("group." + groupname +"."+chestname+".chestname", chestname);
			plugin.getConfig().set("group." + groupname +"."+chestname+".x", loc.getBlockX());
			plugin.getConfig().set("group." + groupname +"."+chestname+".y", loc.getBlockY());
			plugin.getConfig().set("group." + groupname +"."+chestname+".z", loc.getBlockZ());
			plugin.saveConfig();
			plugin.reloadConfig();
			}else{
				player.sendMessage("Sorry! Your formatting is invalid. Please try again.");
				return false;
			}
		}else{
				player.sendMessage("You do not have permission to preform this command!");
			}
			}
			return true;
		}
		return false;
	}
}
