package com.softkik.mcplugins.hermit_tweaks.classes;

import org.bukkit.configuration.file.FileConfiguration;

import com.softkik.mcplugins.hermit_tweaks.MainLoader;

public class SettingsLoader {
	
	static FileConfiguration fc;
	
	public static void Init() {
		fc = MainLoader.instance.getConfig();
	}

	public static Object getInfo(String name) {
		return fc.get(name);
	}
	public static Integer getInt(String name) {
		return fc.getInt(name);
	}
	public static String getString(String name) {
		return fc.getString(name);
	}
	public static Double getDouble(String name) {
		return fc.getDouble(name);
	}

	public static Object getInfo(String name, Object def) {
		return fc.get(name,def);
	}
	public static Integer getInt(String name, Integer def) {
		return fc.getInt(name,def);
	}
	public static String getString(String name, String def) {
		return fc.getString(name,def);
	}
	public static Double getDouble(String name, Double def) {
		return fc.getDouble(name,def);
	}
	public static void setData(String name,Object sn) {
		fc.set(name, sn);
	}
	public static void save() {
		MainLoader.instance.saveConfig();
	}
}
