package com.softkik.mcplugins.hermit_tweaks.classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.softkik.mcplugins.hermit_tweaks.MainLoader;

public class FLDecay implements Listener{

	private Set<Block> getNearbyBlocks(Block start, List<Material> allowedMaterials, HashSet<Block> blocks) {
	    for (int x = -1; x < 2; x++) {
	        for (int y = -1; y < 2; y++) {
	            for (int z = -1; z < 2; z++) {
	                Block block = start.getLocation().clone().add(x, y, z).getBlock();
	                if (block != null && !blocks.contains(block) && allowedMaterials.contains(block.getType())) {
	                    blocks.add(block);
	                    blocks.addAll(getNearbyBlocks(block, allowedMaterials, blocks));
	                }
	            }
	        }
	    }
	    return blocks;
	}
	
	private boolean hasNearbyLeaf(Block b,Material m)
	{
		Location bl = b.getLocation();
		Material leafMat = null;
		switch(m) {
		case ACACIA_LOG:
			leafMat = Material.ACACIA_LEAVES;
			break;
		case JUNGLE_LOG:
			leafMat = Material.JUNGLE_LEAVES;
			break;
		case SPRUCE_LOG:
			leafMat = Material.SPRUCE_LEAVES;
			break;
		case DARK_OAK_LOG:
			leafMat = Material.DARK_OAK_LEAVES;
			break;
		case BIRCH_LOG:
			leafMat = Material.BIRCH_LEAVES;
			break;
		default:
			leafMat = Material.OAK_LEAVES;
			break;
		}
		Block north = b.getWorld().getBlockAt(bl.getBlockX(), bl.getBlockY(), bl.getBlockZ()-1);
		Block west = b.getWorld().getBlockAt(bl.getBlockX()-1, bl.getBlockY(), bl.getBlockZ());
		Block east = b.getWorld().getBlockAt(bl.getBlockX()+1, bl.getBlockY(), bl.getBlockZ());
		Block south = b.getWorld().getBlockAt(bl.getBlockX(), bl.getBlockY(), bl.getBlockZ()+1);
		Block up = b.getWorld().getBlockAt(bl.getBlockX(), bl.getBlockY()+1, bl.getBlockZ());
		if(north.getType().compareTo(leafMat)==0||
			west.getType().compareTo(leafMat)==0||
			east.getType().compareTo(leafMat)==0||
			south.getType().compareTo(leafMat)==0||
			up.getType().compareTo(leafMat)==0) {
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e) {
		List<Material> woods = new ArrayList<Material>() {
			private static final long serialVersionUID = 1L;

		{
			add(Material.OAK_LOG);
			add(Material.BIRCH_LOG);
			add(Material.ACACIA_LOG);
			add(Material.JUNGLE_LOG);
			add(Material.SPRUCE_LOG);
			add(Material.DARK_OAK_LOG);
		}};
		final Material bbc = e.getBlock().getType();
		if(woods.contains(e.getBlock().getType())) {
			if(this.hasNearbyLeaf(e.getBlock(), e.getBlock().getType())) {
				new BukkitRunnable() {
					public void run() {
						FLDecay.this.tryToBreak(e.getBlock(),bbc);
					}
				}.runTaskLater(MainLoader.instance, 20L*SettingsLoader.getInt("hctp-tree-decay-delay", 1));
			}
		}
		
	}

	private void tryToBreak(Block block, Material oldM) {
		Material leafMat = null;
		switch(oldM) {
		case ACACIA_LOG:
			leafMat = Material.ACACIA_LEAVES;
			break;
		case JUNGLE_LOG:
			leafMat = Material.JUNGLE_LEAVES;
			break;
		case SPRUCE_LOG:
			leafMat = Material.SPRUCE_LEAVES;
			break;
		case DARK_OAK_LOG:
			leafMat = Material.DARK_OAK_LEAVES;
			break;
		case BIRCH_LOG:
			leafMat = Material.BIRCH_LEAVES;
			break;
		default:
			leafMat = Material.OAK_LEAVES;
			break;
		}
		final Material lm = leafMat;
		Set<Block> allNSet = getNearbyBlocks(block, new ArrayList<Material>() {
			private static final long serialVersionUID = 1L;
		{add(lm);}}, new HashSet<Block>());
		for(Block bb : allNSet) {
			if(!((Leaves)bb.getState().getBlockData()).isPersistent() &&
				((Leaves)bb.getState().getBlockData()).getDistance()==7) {
				final Block fbb = bb; 
				try {
					new BukkitRunnable() {
						public void run() {
							fbb.breakNaturally();
						}
					}.runTaskLater(MainLoader.instance,(long)(new Random().nextInt(20*SettingsLoader.getInt("hctp-tree-decay-time", 7))));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	static boolean CommandExecution(Player player, String... command) {
		if(!player.isOp()) {
			player.sendMessage(Color.AQUA+"["+Color.ORANGE+"Fast Leaf Decay"+Color.AQUA+"]"+Color.RED+": You must be OP to execute this configuration");
			return true;
		}
		// Switch between posibble items
		return true;
		
	}
	
	static List<String> TabComplete(Command comm, String... args){
		if(args.length==2) {
			comm.setUsage("/hctp fldacay [<command>]");
			return new ArrayList<String>() {
				private static final long serialVersionUID = 1L;

			{add("decayTime");add("delayTime");add("enable");add("disable");}};
		}
		if(args.length==3&&new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

		{add("decayTime");add("delayTime");}}.contains(args[1])) {
			comm.setUsage("/hctp fldacay "+args[1]+" [<time>]");
			return new ArrayList<String>();
		}
		return new ArrayList<String>();
	}
}
