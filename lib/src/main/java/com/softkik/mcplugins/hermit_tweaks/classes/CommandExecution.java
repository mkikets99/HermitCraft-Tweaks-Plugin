package com.softkik.mcplugins.hermit_tweaks.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandExecution implements CommandExecutor, TabCompleter {

	private enum Sections{
		fldecay
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player send = null;
		if(sender instanceof Player) send = (Player)sender;
		try {
		Sections _section = Sections.valueOf(args[0]);
		switch(_section) {
		case fldecay:
			return FLDecay.CommandExecution(send, args);
		}
		}catch(IllegalArgumentException exc) {
			send.sendMessage(Color.RED+"There is no section called ["+Color.AQUA+args[0]+Color.RED+"]");
		}
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
		command.setUsage("/hctp [<datapack>] [<command>]");
		ArrayList<String> toSend = new ArrayList<String>();
		if(args.length==1) {
			toSend.addAll(Arrays.asList(Sections.values()).stream().map((Sections el)->{return el.toString();}).collect(Collectors.toList()));
		}else {
			try {
			Sections _section = Sections.valueOf(args[0]);
			switch(_section) {
			case fldecay:
				return FLDecay.TabComplete(command, args);
			}
			}catch(IllegalArgumentException exc) {
				return new ArrayList<String>();
			}
		}
		return toSend;
	}
	
}
