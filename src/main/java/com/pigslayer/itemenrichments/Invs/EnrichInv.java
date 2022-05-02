package com.pigslayer.itemenrichments.Invs;

import com.pigslayer.itemenrichments.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnrichInv implements InventoryHolder {
    private static Inventory inv;
    public Boolean sucsess = false;

    public EnrichInv() {
        inv = Bukkit.createInventory(this,54, "Enrich Your Items");
        init();
    }
    private static void init(){
        for(int i = 0; i < 54; i++){
            switch(i){
                case 13:
                    inv.setItem(i,createItem(ChatColor.YELLOW+"No Item Selected", Material.ORANGE_STAINED_GLASS_PANE, Collections.singletonList(ChatColor.GOLD + "Select an item from your inventory")));
                    break;
                default:
                    String name = " ";
                    if(Main.debug)
                        name= String.valueOf(i);
                    inv.setItem(i,createPane(i));
            }
        }
    }

    public static ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack createPane(Integer spot){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        if(Main.debug)
            meta.setDisplayName(spot.toString());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }
    public void setSucsess(Boolean tru){
        sucsess=tru;
    }

    public static ItemStack createAir() {
        ItemStack item = new ItemStack(Material.AIR, 1);
        return item;
    }

    public ItemStack checkItem(){
        if(getInventory().getItem(13) == null||getInventory().getItem(13).isSimilar(createItem(ChatColor.YELLOW+"No Item Selected", Material.ORANGE_STAINED_GLASS_PANE, Collections.singletonList(ChatColor.GOLD + "Select an item from your inventory"))))
            return null;
        return getInventory().getItem(13);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}

