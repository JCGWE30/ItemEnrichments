package com.pigslayer.itemenrichments.Commands;

import com.pigslayer.itemenrichments.Handlers;
import com.pigslayer.itemenrichments.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cfgreload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Main.data.reloadConfig();
        sender.sendMessage(Main.prefix+ChatColor.GREEN+" Configuration Reloaded!");
        return true;
    }
}
