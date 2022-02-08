package com.softkik.mcplugins.hermit_tweaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.softkik.mcplugins.hermit_tweaks.classes.CustomNetherPortal;

public class MainLoader extends JavaPlugin{
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new CustomNetherPortal(),this);
	}
}
