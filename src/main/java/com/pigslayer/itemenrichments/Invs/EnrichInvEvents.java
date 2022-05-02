package com.pigslayer.itemenrichments.Invs;

import com.pigslayer.itemenrichments.Main;
import de.tr7zw.nbtapi.NBTItem;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnrichInvEvents implements Listener {
    private static List<Integer> slots = Arrays.asList(19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43);
    public static List<String> calls = Arrays.asList("SPEED","JUMP_HEIGHT","KILL","DAMAGE","KNOCKBACK_RESISTANCE","DEFENSE");
    public static List<String> returns = Arrays.asList("+ &% Movment Speed","+ & Jump height","& Slain","+ &% Attack Damage","+ &% Knockback resistance","+ &% Armor Toughness");
    @EventHandler
    public static void clickdeny(InventoryClickEvent e){
        if(e.getCurrentItem() == null)
            return;
        if(e.getInventory().getHolder() instanceof EnrichInv){
            EnrichInv tinv = ((EnrichInv)e.getInventory().getHolder());
            e.setCancelled(true);
            if(e.getClickedInventory().getHolder() instanceof EnrichInv){
                if(e.getSlot()==31){
                    if(((EnrichInv)e.getInventory().getHolder()).sucsess){
                        e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
                        tinv.setSucsess(false);
                        EnrichInv inv = new EnrichInv();
                        e.getWhoClicked().openInventory(inv.getInventory());
                        return;
                    }
                    NBTItem itee = new NBTItem(e.getCurrentItem());
                    if(e.getCurrentItem().getType()==Material.BARRIER&&!itee.hasKey("E_ID")){
                        ItemStack item = e.getInventory().getItem(13);
                        ItemMeta meta = item.getItemMeta();
                        List<String> lore = meta.getLore();
                        NBTItem ite = new NBTItem(item);
                        for(int i = 0; i < ite.getInteger("E_LL"); i++){
                            lore.remove(lore.size()-1);
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        ite = new NBTItem(item);
                        ite.removeKey("E_ID");
                        ite.removeKey("E_LV");
                        ite.removeKey("E_XP");
                        ite.removeKey("E_LL");
                        item=ite.getItem();
                        e.getInventory().setItem(13,item);
                        Success(e.getClickedInventory());
                        e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1f);
                        e.getWhoClicked().sendMessage(Main.prefix+" "+ChatColor.GREEN+"Enrichment Removed!");
                    }
                }
                if(e.getSlot()==13){
                    if(tinv.checkItem()!=null){
                        EnrichInv gui = new EnrichInv();
                        e.getWhoClicked().openInventory(gui.getInventory());
                        e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
                        e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ITEM_BUNDLE_DROP_CONTENTS,0.5f,1f);
                    }
                }else{
                    NBTItem ite = new NBTItem(e.getCurrentItem());
                    if(Main.data.getConfig().get("Enrichments."+ite.getString("E_ID")+".Cost")!=null){
                        if(Main.eco.getBalance((Player)e.getWhoClicked())>=Main.data.getConfig().getInt("Enrichments."+ite.getString("E_ID")+".Cost")){
                            EconomyResponse r = Main.eco.withdrawPlayer((Player)e.getWhoClicked(),Main.data.getConfig().getInt("Enrichments."+ite.getString("E_ID")+".Cost"));
                            if(r.transactionSuccess()){
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1f);
                                e.getWhoClicked().sendMessage(Main.prefix+" "+ChatColor.GREEN+Main.data.getConfig().getString("Enrichments."+ite.getString("E_ID")+".DisplayName")+" Applied to item!");
                                ItemStack item = e.getInventory().getItem(13);

                                List<String> lore = new ArrayList<>();
                                ItemMeta meta = item.getItemMeta();
                                if(meta.hasLore()){
                                    lore=item.getItemMeta().getLore();
                                }
                                lore.add(" ");
                                lore.add(ChatColor.BLUE+Main.data.getConfig().getString("Enrichments."+ite.getString("E_ID")+".DisplayName")+" "+ChatColor.AQUA+Main.data.getConfig().getConfigurationSection("Enrichments."+ite.getString("E_ID")+".Perks").getKeys(false).toArray()[0]);
                                String str = Main.data.getConfig().getString("Enrichments."+ite.getString("E_ID")+".TierSubType");
                                char[] ch = str.toCharArray();
                                str=str.toLowerCase();
                                str=ch[0]+str.split(Character.toString(ch[0]).toLowerCase())[1]+"s";
                                int LL = 3;
                                lore.add(ChatColor.GRAY+returns.get(calls.indexOf(Main.data.getConfig().getString("Enrichments."+ite.getString("E_ID")+".TierType"))).replace("&",str)+" 0/"+Main.data.getConfig().getInt("Enrichments."+ite.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+ite.getString("E_ID")+".Perks").getKeys(false).toArray()[1]+".TierCount"));
                                for(String stre:Main.data.getConfig().getStringList("Enrichments."+ite.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+ite.getString("E_ID")+".Perks").getKeys(false).toArray()[1]+".Types")){
                                    lore.add(ChatColor.GREEN+returns.get(calls.indexOf(stre)).replace("&",String.valueOf(Main.data.getConfig().getIntegerList("Enrichments."+ite.getString("E_ID")+".Perks."+ Main.data.getConfig().getConfigurationSection("Enrichments."+ite.getString("E_ID")+".Perks").getKeys(false).toArray()[0]+".Effects").get(0))));
                                    LL++;
                                }
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                NBTItem itee = new NBTItem(item);
                                itee.setString("E_ID",ite.getString("E_ID"));
                                itee.setInteger("E_LV",0);
                                itee.setInteger("E_XP",0);
                                itee.setInteger("E_LL", LL);
                                item=itee.getItem();
                                e.getInventory().setItem(13, item);
                                Success(e.getClickedInventory());
                            }else{
                                e.getWhoClicked().sendMessage(Main.prefix+" "+ChatColor.RED+"Error occured: "+r.errorMessage);
                                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_VILLAGER_NO,1f,1f);
                            }
                        }else{
                            e.getWhoClicked().sendMessage(Main.prefix+" "+ChatColor.RED+"You cant afford that!");
                            e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_VILLAGER_NO,1f,1f);
                        }
                    }
                }
            }else{

                if(tinv.checkItem()!=null){
                    e.getWhoClicked().sendMessage(Main.prefix+" "+ChatColor.RED+"Take the current item out first");
                    e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_ENDERMAN_TELEPORT,0.5f,0.5f);
                    return;
                }
                int cur = 0;
                boolean hit=false;
                NBTItem check = new NBTItem(e.getCurrentItem());
                if(check.hasKey("E_ID")){
                    e.getInventory().setItem(31,EnrichInv.createItem(ChatColor.GOLD+"Remove enrichment",Material.BARRIER,Arrays.asList(ChatColor.RED+"Click here to remove the enrichment",ChatColor.DARK_RED+"WARNING: This is permanent, and can not be undone")));
                    ItemStack itom = e.getCurrentItem().clone();
                    itom.setAmount(1);
                    e.getInventory().setItem(13, itom);
                    itom.setAmount(e.getCurrentItem().getAmount()-1);
                    e.getClickedInventory().setItem(e.getSlot(), itom);
                    e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ITEM_BUNDLE_DROP_CONTENTS,0.5f,1f);
                    return;
                }
                for(String str: Main.data.getConfig().getConfigurationSection("Enrichments").getKeys(false)){
                    String mat = e.getCurrentItem().getType().toString();
                    List<String> strs = Arrays.stream(("SWORD SHOVEL PICKAXE AXE HOE HELMET CHESTPLATE LEGGINGS BOOTS").split(" ")).toList();
                    for(String ste:strs){
                        if(mat.contains(ste)){
                            mat=ste;
                        }
                    }
                    if(Main.data.getConfig().getStringList("Enrichments."+str+".WhitelistedItems").contains(mat)){
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD+""+Main.data.getConfig().getConfigurationSection("Enrichments."+str+".Perks").getKeys(false).size()+" Tiers");
                        lore.add(" ");
                        lore.add(ChatColor.GREEN+"Tier 1 Perks");
                        for(String stre:Main.data.getConfig().getStringList("Enrichments."+str+".Perks."+ Main.data.getConfig().getConfigurationSection("Enrichments."+str+".Perks").getKeys(false).toArray()[0]+".Types")){
                            lore.add(ChatColor.GREEN+returns.get(calls.indexOf(stre)).replace("&",String.valueOf(Main.data.getConfig().getIntegerList("Enrichments."+str+".Perks."+ Main.data.getConfig().getConfigurationSection("Enrichments."+str+".Perks").getKeys(false).toArray()[0]+".Effects").get(0))));
                        }
                        lore.add(" ");
                        lore.add(ChatColor.AQUA+"Cost: "+ChatColor.YELLOW+Main.data.getConfig().getInt("Enrichments."+str+".Cost"));
                        lore.add(ChatColor.LIGHT_PURPLE+"Click to apply enrichment!");
                        ItemStack item = EnrichInv.createItem(ChatColor.YELLOW+Main.data.getConfig().getString("Enrichments."+str+".DisplayName"),Material.valueOf(Main.data.getConfig().getString("Enrichments."+str+".Item")),lore);
                        NBTItem nbt = new NBTItem(item);
                        nbt.setString("E_ID",str);
                        item =nbt.getItem();
                        e.getInventory().setItem(slots.get(cur),item);
                        hit=true;
                        cur++;
                    }
                }
                if(hit){
                    ItemStack itom = e.getCurrentItem().clone();
                    itom.setAmount(1);
                    e.getInventory().setItem(13, itom);
                    itom.setAmount(e.getCurrentItem().getAmount()-1);
                    e.getClickedInventory().setItem(e.getSlot(), itom);
                    e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ITEM_BUNDLE_DROP_CONTENTS,0.5f,1f);
                    return;
                }
                e.getWhoClicked().sendMessage(Main.prefix+" "+Main.prefix+" "+ChatColor.RED+"That item does not have any enrichments :(");
                e.getWhoClicked().getWorld().playSound(e.getWhoClicked(), Sound.ENTITY_ENDERMAN_TELEPORT,0.5f,0.5f);
            }
        }
    }
    public static void Success(Inventory inv){
        ItemStack item = inv.getItem(13);
        for(int i = 0; i < 54; i++){
            ItemStack pane = EnrichInv.createPane(i);
            inv.setItem(i,pane);
        }
        inv.setItem(31,item);
        ((EnrichInv)inv.getHolder()).sucsess=true;
    }
    public static List<String> SetArray(Set<String> set){
        List<String> obj = new ArrayList<>();
        for(String objs:set){
            obj.add(objs);
        }
        return obj;
    }
    @EventHandler
    public static void pick(PlayerPickupItemEvent e){
        try {
            if (e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof EnrichInv) {
                e.setCancelled(true);
            }
        }catch(NullPointerException ignored){

        }
    }
    @EventHandler
    public static void close(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof EnrichInv){
            EnrichInv tinv = ((EnrichInv)e.getInventory().getHolder());
            if(tinv.sucsess) {
                e.getPlayer().getInventory().addItem(e.getInventory().getItem(31));
                return;
            }
            if(tinv.checkItem()!=null){
                e.getPlayer().getInventory().addItem(tinv.checkItem());
            }
        }
    }
}
