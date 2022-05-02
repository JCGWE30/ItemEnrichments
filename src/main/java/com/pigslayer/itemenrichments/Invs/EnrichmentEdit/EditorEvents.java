package com.pigslayer.itemenrichments.Invs.EnrichmentEdit;

import com.pigslayer.itemenrichments.Apis.SignEvent;
import com.pigslayer.itemenrichments.Files.Config;
import com.pigslayer.itemenrichments.Handlers;
import com.pigslayer.itemenrichments.Invs.EnrichInv;
import com.pigslayer.itemenrichments.Main;
import de.tr7zw.nbtapi.NBTItem;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.inventory.Containers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EditorEvents implements Listener {
    public static HashMap<Player,String> menu = new HashMap<>();
    public static HashMap<Player, ConfigurationSection> menuloc = new HashMap<>();
    @EventHandler
    public static void sign(SignEvent e){
        if(e.isClose()){
            try {
                Main.debuglog(getConfigurationSection(e).getName());
                menu.remove(e.getPlayer());
                EnricmentEditor gui = new EnricmentEditor(1, getConfigurationSection(e));
                e.getPlayer().openInventory(gui.getInventory());
                menuloc.remove(e.getPlayer());
            }catch(NullPointerException ignored){

            }
        }else {
            if (menu.containsKey(e.getPlayer())) {
                if(e.getslot()==0) {
                    e.getPlayer().getOpenInventory().setCursor(new ItemStack(Material.AIR));
                    ((CraftPlayer) e.getPlayer()).getHandle().b.a(new PacketPlayOutSetSlot(0, 0, 0, CraftItemStack.asNMSCopy(EnricmentEditor.createItem(" ", Material.PAPER, null))));
                }else if(e.getslot()==2){
                    String str = e.getItemName();
                    Main.debuglog(str);
                    if(menu.get(e.getPlayer())=="COST"){
                        try {
                            getConfigurationSection(e).set("Cost", Integer.parseInt(str));
                            EnricmentEditor gui = new EnricmentEditor(1, getConfigurationSection(e));
                            e.getPlayer().openInventory(gui.getInventory());
                            Main.data.saveConfig();
                            menu.remove(e.getPlayer());
                            menuloc.remove(e.getPlayer());
                            e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                            e.getPlayer().sendMessage(Main.prefix+ChatColor.GREEN+" Price updated!");
                        }catch(NumberFormatException ignored){
                            e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.ENTITY_VILLAGER_NO,1f,1f);
                            e.getPlayer().sendMessage(Main.prefix+ChatColor.RED+" Please enter a valid number");
                            e.getPlayer().getOpenInventory().setCursor(new ItemStack(Material.AIR));
                            ((CraftPlayer) e.getPlayer()).getHandle().b.a(new PacketPlayOutSetSlot(0, 0, 0, CraftItemStack.asNMSCopy(EnricmentEditor.createItem(" ", Material.PAPER, null))));
                        }
                    }if(menu.get(e.getPlayer())=="NAME"){
                            getConfigurationSection(e).set("DisplayName", str);
                            EnricmentEditor gui = new EnricmentEditor(1, getConfigurationSection(e));
                            e.getPlayer().openInventory(gui.getInventory());
                            Main.data.saveConfig();
                            menu.remove(e.getPlayer());
                            menuloc.remove(e.getPlayer());
                            e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                            e.getPlayer().sendMessage(Main.prefix+ChatColor.GREEN+" Price updated!");
                    }if(menu.get(e.getPlayer())=="PERK_AMOUNT"){
                        try {
                            getConfigurationSection(e).set("TierCount", Integer.parseInt(str));
                            EnricmentEditor gui = new EnricmentEditor(6, getConfigurationSection(e).getParent().getParent(), getConfigurationSection(e).getParent().getKeys(false).stream().toList().indexOf(getConfigurationSection(e).getName()));
                            e.getPlayer().openInventory(gui.getInventory());
                            Main.data.saveConfig();
                            menu.remove(e.getPlayer());
                            menuloc.remove(e.getPlayer());
                            e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                            e.getPlayer().sendMessage(Main.prefix+ChatColor.GREEN+" XP Updated!");
                        }catch(NumberFormatException ignored){
                            e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.ENTITY_VILLAGER_NO,1f,1f);
                            e.getPlayer().sendMessage(Main.prefix+ChatColor.RED+" Please enter a valid number");
                            e.getPlayer().getOpenInventory().setCursor(new ItemStack(Material.AIR));
                            ((CraftPlayer) e.getPlayer()).getHandle().b.a(new PacketPlayOutSetSlot(0, 0, 0, CraftItemStack.asNMSCopy(EnricmentEditor.createItem(" ", Material.PAPER, null))));
                        }
                    }if(menu.get(e.getPlayer())=="CREATE"){
                        Main.data.getConfig().set("Enrichments."+str.replace(" ","")+".DisplayName", str);
                        Main.data.getConfig().set("Enrichments."+str.replace(" ","")+".Item", Material.DIAMOND.toString());
                        Main.data.getConfig().set("Enrichments."+str.replace(" ","")+".TierType", "KILL");
                        Main.data.getConfig().set("Enrichments."+str.replace(" ","")+".Perks.1.TierCount", 10);
                        Main.data.getConfig().set("Enrichments."+str.replace(" ","")+".Cost", 1);
                        Main.data.getConfig().set("Enrichments."+str.replace(" ","")+".TierSubType", "ZOMBIE");
                        Main.data.saveConfig();
                        EnricmentEditor gui = new EnricmentEditor();
                        e.getPlayer().openInventory(gui.getInventory());
                        menu.remove(e.getPlayer());
                        e.getPlayer().getWorld().playSound(e.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                        e.getPlayer().sendMessage(Main.prefix+ChatColor.GREEN+" Enrichment Created!");
                    }
                }
            }
        }
    }

    private static ConfigurationSection getConfigurationSection(SignEvent e) {
        return menuloc.get(e.getPlayer());
    }

    @EventHandler
    public static void click(InventoryClickEvent e){
        Main.debuglog(e.getEventName());
        if(e.getInventory().getHolder() instanceof EnricmentEditor)
            e.setCancelled(true);
        if(e.getClickedInventory()==null || e.getInventory()==null || e.getCurrentItem()==null)
            return;
        if(e.getClickedInventory().getHolder() instanceof EnricmentEditor){
            EnricmentEditor mgui = ((EnricmentEditor) e.getClickedInventory().getHolder());
            switch((mgui.getType())){
                case 0:
                    NBTItem item = new NBTItem(e.getCurrentItem());
                    if(item.hasKey("E_ID")){
                        if(e.getClick()==ClickType.LEFT) {
                            EnricmentEditor gui = new EnricmentEditor(1, Main.data.getConfig().getConfigurationSection("Enrichments." + item.getString("E_ID")));
                            e.getWhoClicked().openInventory(gui.getInventory());
                        }else if(e.getClick()==ClickType.RIGHT){
                            Main.data.getConfig().set("Enrichments."+item.getString("E_ID"),null);
                            Main.data.saveConfig();
                            EnricmentEditor gui = new EnricmentEditor();
                            e.getWhoClicked().openInventory(gui.getInventory());
                            e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" Enrichment Removed!");
                            e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                        }
                    }else if(e.getSlot()==49){
                        int id = (int) Math.random();
                        ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutOpenWindow(id,Containers.h, CraftChatMessage.fromStringOrNull("Input enrichment name")));
                        ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutSetSlot(id,0,0,CraftItemStack.asNMSCopy(EnricmentEditor.createItem(" ",Material.PAPER,null))));
                        menu.put(((Player) e.getWhoClicked()),"CREATE");
                    }
                    break;
                case 1:
                    switch(e.getSlot()){
                        case 11:EnricmentEditor gui = new EnricmentEditor(3,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 13:
                            gui = new EnricmentEditor(2,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 15:
                            gui = new EnricmentEditor(4,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 29:
                            int id = (int) Math.random();
                            ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutOpenWindow(id,Containers.h, CraftChatMessage.fromStringOrNull("Input new enrichment cost")));
                            ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutSetSlot(id,0,0,CraftItemStack.asNMSCopy(EnricmentEditor.createItem(" ",Material.PAPER,null))));
                            menu.put(((Player) e.getWhoClicked()),"COST");
                            menuloc.put(((Player)e.getWhoClicked()), ((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            break;
                        case 31:
                            id = (int) Math.random();
                            ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutOpenWindow(id,Containers.h, CraftChatMessage.fromStringOrNull("Input new enrichment name")));
                            ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutSetSlot(id,0,0,CraftItemStack.asNMSCopy(EnricmentEditor.createItem(" ",Material.PAPER,null))));
                            menu.put(((Player) e.getWhoClicked()),"NAME");
                            menuloc.put(((Player)e.getWhoClicked()), ((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            break;
                        case 33:
                            gui = new EnricmentEditor(5,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 40:
                            gui = new EnricmentEditor();
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                    }
                    break;
                case 2:
                    Main.debuglog(String.valueOf(e.getSlot()));
                    EnricmentEditor gui;
                    switch(e.getSlot()){
                        case 50:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(2,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()+1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        case 49:
                            gui = new EnricmentEditor(1,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                                e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 48:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(2,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()-1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        default:
                            NBTItem ite = new NBTItem(e.getCurrentItem());
                            if(ite.hasKey("E_ID")){
                                ConfigurationSection cfg = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg();
                                cfg.set("TierSubType",ite.getString("E_ID"));
                                Main.data.saveConfig();
                                gui = new EnricmentEditor(1,Main.data.getConfig().getConfigurationSection("Enrichments."+cfg.getName()));
                                e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" XP Mob changed to "+ Handlers.FancyString(ite.getString("E_ID")));
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                    }
                    break;
                case 3:
                    switch(e.getSlot()){
                        case 50:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(3,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()+1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        case 49:
                            gui = new EnricmentEditor(1,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 48:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(3,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()-1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        default:
                            if(e.getCurrentItem().getItemMeta().getDisplayName()!=" "&&!EnricmentEditor.ints.contains(e.getSlot())){
                                ConfigurationSection cfg = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg();
                                cfg.set("Item",e.getCurrentItem().getType().name());
                                Main.data.saveConfig();
                                gui = new EnricmentEditor(1,Main.data.getConfig().getConfigurationSection("Enrichments."+cfg.getName()));
                                e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" Icon changed to "+ Handlers.FancyString(e.getCurrentItem().getType().name()));
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                    }
                    break;
                case 4:
                    switch(e.getSlot()){
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            if (!e.getCurrentItem().getItemMeta().hasEnchants()) {
                                List<String> wlist = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg().getStringList("WhitelistedItems");
                                wlist.add(e.getCurrentItem().getType().name().toUpperCase().split("_")[1]);
                                ((EnricmentEditor) e.getInventory().getHolder()).getPcfg().set("WhitelistedItems",wlist);
                                Main.data.saveConfig();
                                e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" All "+e.getCurrentItem().getItemMeta().getDisplayName()+" added!");
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                                gui = new EnricmentEditor(4,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),((EnricmentEditor)e.getClickedInventory().getHolder()).getPage());
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }else if (e.getCurrentItem().getItemMeta().hasEnchants()) {
                                List<String> wlist = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg().getStringList("WhitelistedItems");
                                wlist.remove(e.getCurrentItem().getType().name().toUpperCase().split("_")[1]);
                                ((EnricmentEditor) e.getInventory().getHolder()).getPcfg().set("WhitelistedItems",wlist);
                                Main.data.saveConfig();
                                e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" All "+e.getCurrentItem().getItemMeta().getDisplayName()+" removed!");
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                                gui = new EnricmentEditor(4,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),((EnricmentEditor)e.getInventory().getHolder()).getPage());
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        case 50:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(4,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()+1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        case 49:
                            gui = new EnricmentEditor(1,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 48:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(4,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()-1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        default:
                            if(!EnricmentEditor.ints.contains(e.getSlot())){
                                ConfigurationSection cfg = ((EnricmentEditor)e.getInventory().getHolder()).getPcfg();
                                List<String> wlist = cfg.getStringList("WhitelistedItems");
                                wlist.remove(e.getCurrentItem().getType().name().toUpperCase().replace(" ","_"));
                                cfg.set("WhitelistedItems",wlist);
                                Main.data.saveConfig();
                                e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" "+e.getCurrentItem().getItemMeta().getDisplayName()+" removed!");
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                                gui = new EnricmentEditor(4,cfg,((EnricmentEditor)e.getInventory().getHolder()).getPage());
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                    }
                    break;
                case 5:
                    switch(e.getSlot()){
                        case 50:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(5,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()+1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        case 48:
                            if(e.getCurrentItem().getType()==Material.ARROW){
                                gui = new EnricmentEditor(5,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage()-1);
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }
                            break;
                        case 49:
                            gui = new EnricmentEditor(1,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                        case 52:
                            ConfigurationSection cofg = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg().getConfigurationSection("Perks");
                            cofg.set((cofg.getKeys(false).size()+1)+".TierCount",10);
                            Main.data.saveConfig();
                            gui = new EnricmentEditor(6,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),cofg.getKeys(false).size()-1);
                            e.getWhoClicked().openInventory(gui.getInventory());
                            e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" New perk created!");
                            e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                            break;
                        default:
                        if(!EnricmentEditor.ints.contains(e.getSlot())){
                            if(e.getClick()== ClickType.LEFT) {
                                ConfigurationSection cfg = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg();
                                List<String> stra = cfg.getConfigurationSection("Perks").getKeys(false).stream().toList();
                                gui = new EnricmentEditor(6, ((EnricmentEditor) e.getInventory().getHolder()).getPcfg(), stra.indexOf(e.getCurrentItem().getItemMeta().getDisplayName().split("§6")[1]));
                                e.getWhoClicked().openInventory(gui.getInventory());
                            }else if(e.getClick()==ClickType.RIGHT){
                                ConfigurationSection cfg = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg();
                                List<String> stra = cfg.getConfigurationSection("Perks").getKeys(false).stream().toList();
                                Main.debuglog("RealShit"+(String) cfg.getConfigurationSection("Perks").getKeys(false).toArray()[stra.indexOf(e.getCurrentItem().getItemMeta().getDisplayName().split("§6")[1])]);
                                cfg.getConfigurationSection("Perks").set((String) cfg.getConfigurationSection("Perks").getKeys(false).toArray()[stra.indexOf(e.getCurrentItem().getItemMeta().getDisplayName().split("§6")[1])],null);
                                Main.data.saveConfig();
                                gui = new EnricmentEditor(5,((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),mgui.getPage());
                                e.getWhoClicked().sendMessage(Main.prefix+ChatColor.GREEN+" Perk deleted!");
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,2f,1f);
                                e.getWhoClicked().openInventory(gui.getInventory());
                                break;
                            }
                        }
                    }
                    break;
                case 6:
                    switch(e.getSlot()){
                        case 13:
                            int id = (int) Math.random();
                            ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutOpenWindow(id,Containers.h, CraftChatMessage.fromStringOrNull("Input new perk name")));
                            ((CraftPlayer)e.getWhoClicked()).getHandle().b.a(new PacketPlayOutSetSlot(id,0,0,CraftItemStack.asNMSCopy(EnricmentEditor.createItem(" ",Material.PAPER,null))));
                            menu.put(((Player) e.getWhoClicked()),"PERK_AMOUNT");
                            ConfigurationSection cfa = ((EnricmentEditor)e.getClickedInventory().getHolder()).getPcfg().getConfigurationSection("Perks");
                            ConfigurationSection cfaa = cfa.getConfigurationSection(cfa.getKeys(false).toArray()[mgui.getPage()].toString());
                            menuloc.put(((Player)e.getWhoClicked()), cfaa);
                            break;
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                            if(e.getCurrentItem().getType()==Material.REDSTONE_BLOCK){
                                NBTItem ite = new NBTItem(e.getCurrentItem());
                                ConfigurationSection cfg = ((EnricmentEditor)e.getClickedInventory().getHolder()).getPcfg().getConfigurationSection("Perks");
                                ConfigurationSection cfgg = cfg.getConfigurationSection(cfg.getKeys(false).toArray()[mgui.getPage()].toString());
                                List<String> st = cfgg.getStringList("Types");
                                List<Integer> it = cfgg.getIntegerList("Effects");
                                st.add(ite.getString("E_ID"));
                                it.add(1);
                                cfgg.set("Types",st);
                                cfgg.set("Effects",it);
                                Main.data.saveConfig();
                                EnricmentEditor guia = new EnricmentEditor(6,((EnricmentEditor)e.getInventory().getHolder()).getPcfg(),mgui.getPage());
                                e.getWhoClicked().openInventory(guia.getInventory());
                            }else if(e.getCurrentItem().getType()==Material.DIAMOND_BLOCK){
                                NBTItem ite = new NBTItem(e.getCurrentItem());
                                ConfigurationSection cfg = ((EnricmentEditor)e.getClickedInventory().getHolder()).getPcfg().getConfigurationSection("Perks");
                                Main.debuglog(cfg.getKeys(false).toArray()[mgui.getPage()]+" ");
                                ConfigurationSection cfgg = cfg.getConfigurationSection(cfg.getKeys(false).toArray()[mgui.getPage()].toString());
                                List<String> st = cfgg.getStringList("Types");
                                List<Integer> it = cfgg.getIntegerList("Effects");
                                it.remove(st.indexOf(ite.getString("E_ID")));
                                st.remove(ite.getString("E_ID"));
                                cfgg.set("Types",st);
                                cfgg.set("Effects",it);
                                Main.data.saveConfig();
                                EnricmentEditor guia = new EnricmentEditor(6,((EnricmentEditor)e.getInventory().getHolder()).getPcfg(),mgui.getPage());
                                e.getWhoClicked().openInventory(guia.getInventory());
                            }
                            break;
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                            if(e.getCurrentItem().getType()==Material.GOLD_BLOCK){
                                int amo = 1;
                                switch (e.getClick()){
                                    case RIGHT:
                                        amo=-1;
                                        break;
                                    case SHIFT_LEFT:
                                        amo=10;
                                        break;
                                    case SHIFT_RIGHT:
                                        amo=-10;
                                        break;
                                }
                                NBTItem ite = new NBTItem(e.getCurrentItem());
                                ConfigurationSection cfg = ((EnricmentEditor)e.getClickedInventory().getHolder()).getPcfg().getConfigurationSection("Perks");
                                Main.debuglog(cfg.getKeys(false).toArray()[mgui.getPage()]+" ");
                                ConfigurationSection cfgg = cfg.getConfigurationSection(cfg.getKeys(false).toArray()[mgui.getPage()].toString());
                                List<String> st = cfgg.getStringList("Types");
                                List<Integer> it = cfgg.getIntegerList("Effects");
                                int fina = it.get(st.indexOf(ite.getString("E_ID")))+amo;
                                if(0>=fina)
                                    fina=1;
                                it.set(st.indexOf(ite.getString("E_ID")),fina);
                                cfgg.set("Types",st);
                                cfgg.set("Effects",it);
                                Main.data.saveConfig();
                                EnricmentEditor guia = new EnricmentEditor(6,((EnricmentEditor)e.getInventory().getHolder()).getPcfg(),mgui.getPage());
                                e.getWhoClicked().openInventory(guia.getInventory());
                            }
                            break;
                        case 49:
                            gui = new EnricmentEditor(5,((EnricmentEditor) e.getInventory().getHolder()).getPcfg());
                            e.getWhoClicked().openInventory(gui.getInventory());
                            break;
                    }
            }
        }else if(e.getInventory().getHolder() instanceof EnricmentEditor){
            EnricmentEditor mgui = ((EnricmentEditor) e.getInventory().getHolder());
            switch((mgui.getType())) {
                case 0:
                    ConfigurationSection cfg = ((EnricmentEditor) e.getInventory().getHolder()).getPcfg();
                    cfg.set("Item", e.getCurrentItem().getType().toString());
                    Main.data.saveConfig();
                    EnricmentEditor gui = new EnricmentEditor(1, Main.data.getConfig().getConfigurationSection("Enrichments." + cfg.getName()));
                    e.getWhoClicked().sendMessage(Main.prefix + ChatColor.GREEN + " Icon changed to " + Handlers.FancyString(e.getCurrentItem().getType().name()));
                    e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1f);
                    e.getWhoClicked().openInventory(gui.getInventory());
                    break;
                case 4:
                    cfg = ((EnricmentEditor)e.getInventory().getHolder()).getPcfg();
                    List<String> wlist = cfg.getStringList("WhitelistedItems");
                    if(wlist.contains(e.getCurrentItem().getType().name().toUpperCase().replace(" ","_"))) {
                        wlist.remove(e.getCurrentItem().getType().name().toUpperCase().replace(" ", "_"));
                        cfg.set("WhitelistedItems", wlist);
                        Main.data.saveConfig();
                        e.getWhoClicked().sendMessage(Main.prefix + ChatColor.GREEN + " " + Handlers.FancyString(e.getCurrentItem().getType().name()) + " removed!");
                        e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1f);
                        gui = new EnricmentEditor(4, ((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),((EnricmentEditor) e.getInventory().getHolder()).getPage());
                        e.getWhoClicked().openInventory(gui.getInventory());
                    }else{
                        wlist.add(e.getCurrentItem().getType().name().toUpperCase().replace(" ", "_"));
                        cfg.set("WhitelistedItems", wlist);
                        Main.data.saveConfig();
                        e.getWhoClicked().sendMessage(Main.prefix + ChatColor.GREEN + " " + Handlers.FancyString(e.getCurrentItem().getType().name())+ " added!");
                        e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2f, 1f);
                        gui = new EnricmentEditor(4, ((EnricmentEditor) e.getInventory().getHolder()).getPcfg(),((EnricmentEditor) e.getInventory().getHolder()).getPage());
                        e.getWhoClicked().openInventory(gui.getInventory());
                    }
                    break;
            }
        }
    }
}
