package com.pigslayer.itemenrichments.Commands;

import com.pigslayer.itemenrichments.Handlers;
import com.pigslayer.itemenrichments.Invs.EnrichmentEdit.EnricmentEditor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class edirenrichments implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        EnricmentEditor gui = new EnricmentEditor();
        ((Player) sender).openInventory(gui.getInventory());
        return true;
    }
}
