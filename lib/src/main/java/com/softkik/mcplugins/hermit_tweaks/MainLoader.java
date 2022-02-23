package com.softkik.mcplugins.hermit_tweaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.softkik.mcplugins.hermit_tweaks.classes.FLDecay;
import com.softkik.mcplugins.hermit_tweaks.classes.SettingsLoader;

import revxrsal.commands.bukkit.BukkitCommandHandler;

public class MainLoader extends JavaPlugin{
	public static MainLoader instance;
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		SettingsLoader.Init();
		FLDecay fld = new FLDecay();
		Bukkit.getServer().getPluginManager().registerEvents(fld,this);
		BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
		commandHandler.register(fld);
		commandHandler.registerBrigadier();
		
	}
	
	public void onDisable() {
		System.out.println("Poweroff");
		SettingsLoader.save();
	}
}
