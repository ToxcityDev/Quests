package me.quickselling.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener {
	private Inventory inv;
	private Inventory inv2;
	ArrayList <Player> killaplayer = new ArrayList<>();
	ArrayList <Player> killaplayercompleted = new ArrayList<>();
    ArrayList<String> obsidian1 = new ArrayList();
    private Map<Player, Integer> obsidianBlocksBroken = new HashMap<Player, Integer>();
    
	private static String prefix = ChatColor.YELLOW + "" + ChatColor.BOLD + "(!) ";
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
	}
	public void onDisable() {
		
	}

	public boolean onCommand(CommandSender sender,Command cmd,String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("quests")) {
	
	
			Player p = (Player) sender;
			
			GUI(p);
		}
		return true;
	}

	private void GUI(Player player) {
		inv = Bukkit.getServer().createInventory(null, 27, ChatColor.YELLOW + "Quests");
		
		// Daily
		ItemStack daily = new ItemStack(Material.GOLD_INGOT);
		ItemMeta dailymeta = daily.getItemMeta();
		dailymeta.setDisplayName(ChatColor.AQUA + "Daily");
		daily.setItemMeta(dailymeta);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "»  " +ChatColor.AQUA + "Can be completed daily");
		dailymeta.setLore(lore);
		// Weekly
		ItemStack weekly = new ItemStack(Material.IRON_INGOT);
		ItemMeta weeklymeta = weekly.getItemMeta();
		weeklymeta.setDisplayName(ChatColor.AQUA + "Weekly");
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add(ChatColor.GRAY + "»  " +ChatColor.AQUA + "Can be completed weekly");
		weekly.setItemMeta(weeklymeta);
		weeklymeta.setLore(lore2);
		//Monthly
		ItemStack monthly = new ItemStack(Material.DIAMOND);
		ItemMeta monthlymeta = monthly.getItemMeta();
		monthlymeta.setDisplayName(ChatColor.AQUA + "Monthly");
		ArrayList<String> lore3 = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "»  " +ChatColor.AQUA + "Can be completed monthly");
		monthlymeta.setLore(lore3);
		monthly.setItemMeta(monthlymeta);
		
		inv.setItem(10, daily);
		inv.setItem(13, weekly);
		inv.setItem(16, monthly);
		
		player.openInventory(inv);
	}
	public void Daily(Player player) {
		inv = Bukkit.getServer().createInventory(null, 27, ChatColor.AQUA + "Daily");
		ItemStack killaplayer = new ItemStack(Material.IRON_SWORD);
		ItemMeta killmeta = killaplayer.getItemMeta();
		killmeta.setDisplayName(ChatColor.AQUA +  "Kill a Zombie");
		killaplayer.setItemMeta(killmeta);
		inv.setItem(0, killaplayer);
		player.openInventory(inv);
		
		
		
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!e.getInventory().getName().equalsIgnoreCase(inv.getName()))
			return;
		if (e.getCurrentItem().getItemMeta() == null)
			return;
		if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Daily")) {
			e.setCancelled(true); 
			p.closeInventory();
			Daily(p);
		}
		
		if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Weekly")) {
			e.setCancelled(true);
		
			p.closeInventory();
			 
		}
		if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Monthly")) {
			e.setCancelled(true);
			p.closeInventory();
		}

		if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Kill a Zombie")) {
			e.setCancelled(true);
		
			p.sendMessage(ChatColor.YELLOW + ""  + ChatColor.BOLD + "(!) " + ChatColor.YELLOW + "You quest is to do the following.");
			p.sendMessage("");
			p.sendMessage(ChatColor.GRAY + "» Kill 1 Zombie");
			p.sendMessage("");
			p.sendMessage(ChatColor.GRAY + "» You will get 1 Diamond if you complete this quest");
			p.closeInventory();
		}
	}
	
	@EventHandler
			public void EntityDeath(final EntityDeathEvent event) {
				if(event.getEntity() instanceof Zombie) {
					  Zombie zombie = (Zombie) event.getEntity();
					  
				       Player mcPlayer = zombie.getKiller();
				       if (mcPlayer == null)
				    	   return;
				
				    	   mcPlayer.sendMessage(ChatColor.YELLOW + "You have finished the quest! Heres your reward");
				    	   ItemStack rewarddiamond = new ItemStack(Material.DIAMOND);
				    	   mcPlayer.getInventory().addItem(rewarddiamond);
				}
		}
				@EventHandler 
					public void PlayerChat(AsyncPlayerChatEvent event) {
					if (event.getMessage().equalsIgnoreCase("This server is awesome")) {
					Player p = event.getPlayer();
					p.sendMessage(prefix + ChatColor.YELLOW + "You have completed a quest! here is your reward. ");
					ItemStack rewarddiamond = new ItemStack(Material.DIAMOND);
					p.getInventory().addItem(rewarddiamond);
				    	   
				       }
				}
				@EventHandler
				public void PlayerBreakBlock(BlockBreakEvent event) {
				    Player player = event.getPlayer();
				    Block block = event.getBlock();
				    int blocks = 0;
				    
				    
				    // Check if the block is obsidian.
				    if (block.getType() == Material.OBSIDIAN) {
				        // Check to see if player has already broken a block before (map contains player).
				        if (this.obsidianBlocksBroken.containsKey(player)) {
				            // At this point we know that the player has broken an obsidian block and has done so before. 
				            // So increment the integer.
				            blocks = this.obsidianBlocksBroken.get(player);
				           this.obsidianBlocksBroken.put(player, blocks + 1);
				        } else {
				            // Else we're dealing with a new player.
				            blocks = 1;
				           this.obsidianBlocksBroken.put(player, blocks);
				        }

				        // Replace the broken block with it's original type (obsidian).
				       block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).setType(block.getType());

				        // If the player breaks 10 obsidian blocks, reward him/her.
				        if (blocks == 64) { // 9 because we just reached 10 (-1).
				            player.sendMessage("It works!");
				            // Reset to 0.
				            this.obsidianBlocksBroken.put(player, 0);
				        }
						    if (block.getType() == Material.STONE) {
						        // Check to see if player has already broken a block before (map contains player).
						        if (this.obsidianBlocksBroken.containsKey(player)) {
						            // At this point we know that the player has broken an obsidian block and has done so before. 
						            // So increment the integer.
						            blocks = this.obsidianBlocksBroken.get(player);
						           this.obsidianBlocksBroken.put(player, blocks + 1);
						        } else {
						            // Else we're dealing with a new player.
						            blocks = 1;
						           this.obsidianBlocksBroken.put(player, blocks);
						        }

						        // Replace the broken block with it's original type (obsidian).
						       block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ()).setType(block.getType());

						        // If the player breaks 10 obsidian blocks, reward him/her.
						        if (blocks == 1) { // 9 because we just reached 10 (-1).
						            player.sendMessage("It works! STONE");
						            // Reset to 0.
						            this.obsidianBlocksBroken.put(player, 0);
	 			          
				        }
				    }
				}

				
			


			        
			  
					}



}
		
	

				    		   
				
				
					
				
	
			
			
			

	
		

		
		
	

	
