package com.softkik.mcplugins.hermit_tweaks.classes;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.MetadataValue;

public class CustomNetherPortal implements Listener{

	private boolean checkIfClosed (World w, Block b, Block beginning) {
		if(beginning==null) {
			return checkIfClosed(w, b, b);
		}
		
		return false;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Block block = e.getBlock();
		Player player = e.getPlayer();
		if(block.getBlockData().getMaterial().equals(Material.FIRE)) {
			if(block.getWorld().getBlockAt(block.getX(), block.getY()-1, block.getZ()).getBlockData().getMaterial().equals(Material.OBSIDIAN)) {
				block.setType(Material.NETHER_PORTAL);
				BlockData bd = block.getBlockData();
				if(bd instanceof Orientable) {
					Orientable orientable = (Orientable) bd;
				    orientable.setAxis(Axis.Z);
				}
				block.setBlockData(bd);
			}
		}
		player.sendMessage("This is "+block.getBlockData().getAsString());
	}
}
