package com.pigslayer.itemenrichments.Apis;

import com.pigslayer.itemenrichments.Main;
import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SignEvent extends Event{

    private final int slot;
    private final boolean close;
    private final Player player;
    private final String itemName;
    public static final HandlerList HANDLERS = new HandlerList();
    public SignEvent(Player p, Integer slot, net.minecraft.world.item.ItemStack item) {
        this.player=p;
        this.slot=slot;
        String str = item.H().getString().substring(1,item.H().getString().length()-1);
        Main.debuglog(String.valueOf(str.substring(0,1).equalsIgnoreCase(" ")));
        if(str.substring(0,1).equalsIgnoreCase(" "))
            str = str.substring(1);
        Main.debuglog("A+"+str);
        this.itemName = str;
        this.close=false;
    }
    public SignEvent(Player p) {
        this.slot=-1;
        this.player=p;
        this.itemName=null;
        this.close=true;
    }
    public boolean isClose(){
        return close;
    }
    public int getslot() {
        return slot;
    }
    public String getItemName(){
        return itemName;
    }
    public Player getPlayer(){
        return player;
    }
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
