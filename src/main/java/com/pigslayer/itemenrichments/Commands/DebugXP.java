package com.pigslayer.itemenrichments.Commands;

import com.pigslayer.itemenrichments.Handlers;
import com.pigslayer.itemenrichments.Invs.EnrichInv;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DebugXP implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Handlers.forceHandleENxp(Integer.parseInt(args[0]), ((Player) sender));
        return true;
    }
}
