package com.softkik.mcplugins.hermit_tweaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.softkik.mcplugins.hermit_tweaks.classes.CommandExecution;
//import com.softkik.mcplugins.hermit_tweaks.classes.CustomNetherPortal;
import com.softkik.mcplugins.hermit_tweaks.classes.FLDecay;

public class MainLoader extends JavaPlugin{
	public static MainLoader instance;
	public void onEnable() {
		saveDefaultConfig();
		instance = this;
//		Bukkit.getServer().getPluginManager().registerEvents(new CustomNetherPortal(),this);
		Bukkit.getServer().getPluginManager().registerEvents(new FLDecay(),this);
		CommandExecution ce = new CommandExecution();
		this.getCommand("hctp").setExecutor(ce);
		this.getCommand("hctp").setTabCompleter(ce);
		
	}
}
