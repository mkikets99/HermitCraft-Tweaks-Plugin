package com.softkik.mcplugins.hermit_tweaks.classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.softkik.mcplugins.hermit_tweaks.MainLoader;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandExceptionAdapter.Ignore;

@Command({"hctp fldecay"})
public class FLDecay implements Listener{
	
	private boolean tempSwitch = true;
	
	public FLDecay() {
		tempSwitch = SettingsLoader.getBoolean("hctp-ftdecay-enabled", false);
	}

	@Ignore
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

	@Ignore
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

	@Ignore
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e) {
		if(tempSwitch) {
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
					}.runTaskLater(MainLoader.instance, 20L*SettingsLoader.getInt("hctp-fldecay-delay", 1));
				}
			}
		}
		
	}
	@Ignore
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
					}.runTaskLater(MainLoader.instance,(long)(new Random().nextInt(20*SettingsLoader.getInt("hctp-fldecay-time", 7))));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	@Subcommand("decayTime")
	@Description("Fast Leaf Decay - Decay time config")
	@CommandPermission("hctp.fldecay.decayTime")
	@Usage("hctp fldecay decayTime [<time>]")
	public void DecayTime(Player sender, @Range(min=1,max=200) Integer time) {
		if(SettingsLoader.PersorRequiresOP(sender)) {
			if(time==null) {
				sender.sendMessage("Amount must be set");
			}else {
				SettingsLoader.setData("hctp-fldecay-time", time);
				sender.sendMessage("Settled");
			}
		}
		return;
		
	}
	@Subcommand("delayTime")
	@Description("Fast Leaf Decay - Delay time config")
	@CommandPermission("hctp.fldecay.delayTime")
	@Usage("hctp fldecay delayTime [<time>]")
	public void DelayTime(Player sender, @Range(min=1,max=200) Integer time) {
		if(SettingsLoader.PersorRequiresOP(sender)) {
			if(time==null) {
				sender.sendMessage("Amount must be set");
			}else {
				SettingsLoader.setData("hctp-fldecay-delay", time);
				sender.sendMessage("Settled");
			}
		}
		return;
	}
	@Subcommand("disable")
	@Description("Fast Leaf Decay - Delay time config")
	@CommandPermission("hctp.fldecay.delayTime")
	@Usage("hctp fldecay delayTime [<time>]")
	public void disable(Player sender, @Switch("temporary") boolean temporary) {
		if(SettingsLoader.PersorRequiresOP(sender)) {
			this.tempSwitch = false;
			if(!temporary){
				SettingsLoader.setData("hctp-fldecay-enabled", false);
			}
			sender.sendMessage("Settled");
		}
		return;
	}
	@Subcommand("enable")
	@Description("Fast Leaf Decay - Delay time config")
	@CommandPermission("hctp.fldecay.delayTime")
	@Usage("hctp fldecay delayTime [<time>]")
	public void enable(Player sender, @Switch("temporary") boolean temporary) {
		if(SettingsLoader.PersorRequiresOP(sender)) {
			this.tempSwitch = true;
			if(!temporary){
				SettingsLoader.setData("hctp-fldecay-enabled", true);
			}
			sender.sendMessage("Settled");
		}
		return;
	}
}
