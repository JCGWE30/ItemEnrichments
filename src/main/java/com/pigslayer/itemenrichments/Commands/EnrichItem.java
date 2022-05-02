package com.pigslayer.itemenrichments.Commands;

import com.pigslayer.itemenrichments.Handlers;
import com.pigslayer.itemenrichments.Invs.EnrichInv;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class EnrichItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        EnrichInv gui = new EnrichInv();
        ((Player) sender).openInventory(gui.getInventory());
        return true;
    }
}
