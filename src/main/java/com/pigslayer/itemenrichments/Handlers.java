package com.pigslayer.itemenrichments;

import com.pigslayer.itemenrichments.Invs.EnrichInvEvents;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Handlers {
    public static HashMap<Player, AttributeModifier> spat = new HashMap<>();
    public static void HandleENxp(Integer xp, EntityType ent, Player p){
        List<String> ents = Arrays.asList("ALL",ent.toString());
        List<ItemStack> items = GenerateENList(p);
        for(ItemStack item:items){
            NBTItem nbti = new NBTItem(item);
            if(Main.data.getConfig().getString("Enrichments." + nbti.getString("E_ID") + ".TierType").equals("KILL")){
                if(!ents.contains(Main.data.getConfig().getString("Enrichments." + nbti.getString("E_ID") + ".TierSubType")))
                    return;
                returnItem(p,PvtHandle(xp,item),item);
            }
        }
    }
    public static void forceHandleENxp(Integer xp,Player p){
        List<ItemStack> items = GenerateENList(p);
        for(ItemStack item:items){
                returnItem(p,PvtHandle(xp,item),item);
        }
    }
    private static ItemStack PvtHandle(Integer xp,ItemStack item){
            NBTItem nbti = new NBTItem(item);
                ConfigurationSection cfg = Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks").getKeys(false).toArray()[nbti.getInteger("E_LV")]);
                if(nbti.getInteger("E_XP")+xp>=Main.data.getConfig().getInt("Enrichments."+nbti.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks").getKeys(false).toArray()[nbti.getInteger("E_LV")+1]+".TierCount")){
                    nbti.setInteger("E_LV",nbti.getInteger("E_LV")+1);
                    nbti.setInteger("E_XP",0);
                }else{
                    nbti.setInteger("E_XP",nbti.getInteger("E_XP")+xp);
                }
                item = nbti.getItem();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                String str = Main.data.getConfig().getString("Enrichments."+nbti.getString("E_ID")+".TierSubType");
                char[] ch = str.toCharArray();
                str=str.toLowerCase();
                str=ch[0]+str.split(Character.toString(ch[0]).toLowerCase())[1]+"s";
                lore.set(lore.size()-nbti.getInteger("E_LL")+1, ChatColor.BLUE+Main.data.getConfig().getString("Enrichments."+nbti.getString("E_ID")+".DisplayName")+" "+ChatColor.AQUA+Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks").getKeys(false).toArray()[nbti.getInteger("E_LV")]);
                lore.set(lore.size()-nbti.getInteger("E_LL")+2, ChatColor.GRAY+ EnrichInvEvents.returns.get(EnrichInvEvents.calls.indexOf(Main.data.getConfig().getString("Enrichments."+nbti.getString("E_ID")+".TierType"))).replace("&",str)+" "+nbti.getInteger("E_XP")+"/"+Main.data.getConfig().getInt("Enrichments."+nbti.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks").getKeys(false).toArray()[nbti.getInteger("E_LV")+1]+".TierCount"));
                Main.debuglog("Starting");
                for(String ignored :cfg.getStringList("Types")){
                    lore.remove(lore.size()-1);
                    Main.debuglog("Yeeted");
                }
                Main.debuglog(" "+nbti.getInteger("E_LV"));
                cfg = Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks").getKeys(false).toArray()[nbti.getInteger("E_LV")]);
                for(String stre :cfg.getStringList("Types")){
                    lore.add(ChatColor.GREEN+EnrichInvEvents.returns.get(EnrichInvEvents.calls.indexOf(stre)).replace("&",String.valueOf(Main.data.getConfig().getIntegerList("Enrichments."+nbti.getString("E_ID")+".Perks."+ Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks").getKeys(false).toArray()[nbti.getInteger("E_LV")]+".Effects").get(0))));
                    Main.debuglog(ChatColor.GREEN+EnrichInvEvents.returns.get(EnrichInvEvents.calls.indexOf(stre)).replace("&",String.valueOf(Main.data.getConfig().getIntegerList("Enrichments."+nbti.getString("E_ID")+".Perks."+ Main.data.getConfig().getConfigurationSection("Enrichments."+nbti.getString("E_ID")+".Perks").getKeys(false).toArray()[nbti.getInteger("E_LV")]+".Effects").get(0))));
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
    }
    public static List<ItemStack> GenerateENList(Player p){
        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> trueitems = new ArrayList<>();
        if(p.getInventory().getItemInMainHand().getType()!= Material.AIR)
            items.add(p.getInventory().getItemInMainHand());
        if(p.getInventory().getHelmet()!=null)
            items.add(p.getInventory().getHelmet());
        if(p.getInventory().getChestplate()!=null)
            items.add(p.getInventory().getChestplate());
        if(p.getInventory().getLeggings()!=null)
            items.add(p.getInventory().getLeggings());
        if(p.getInventory().getBoots()!=null)
            items.add(p.getInventory().getBoots());
        for(ItemStack item:items){
            NBTItem nbt = new NBTItem(item);
            if(nbt.hasKey("E_ID"))
                trueitems.add(item);
        }
        return trueitems;
    }
    private static Integer getItemSlot(Player p, ItemStack item){
        if(p.getInventory().getItemInMainHand().isSimilar(item))
            return -1;
        if(Arrays.stream(p.getInventory().getArmorContents()).toList().contains(item)){
            return Arrays.stream(p.getInventory().getArmorContents()).toList().indexOf(item);
        }
        return null;
    }
    private static void returnItem(Player p, ItemStack item,ItemStack OGItem){
        switch(getItemSlot(p,OGItem)){
            case -1:
                p.getInventory().setItemInMainHand(item);
                break;
            case 1:
                p.getInventory().setLeggings(item);
                break;
            case 2:
                p.getInventory().setChestplate(item);
                break;
            case 3:
                p.getInventory().setHelmet(item);
                break;
            case 0:
                p.getInventory().setBoots(item);
                break;
        }
        calculateStats(p);
    }
    public static String FancyString(String stre){
        String str = stre;
        char[] ch = str.toCharArray();
        str = String.valueOf(ch[0]).toUpperCase();
        List<Character> chh = new ArrayList<>();
        for (Character c : ch) {
            chh.add(c);
        }
        chh.remove(0);
        for (Character c : chh) {
            str = str + c.toString().toLowerCase();
        }
        str = str.replace("_", " ");
        return str;
    }
    public static void calculateStats(Player p){
        List<ItemStack> items = GenerateENList(p);
        int speed = 0;
        for(ItemStack item:items){
            NBTItem nbt = new NBTItem(item);
            ConfigurationSection cfg = Main.data.getConfig().getConfigurationSection("Enrichments."+nbt.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+nbt.getString("E_ID")+".Perks").getKeys(false).toArray()[nbt.getInteger("E_LV")]);
            for(String str:cfg.getStringList("Types")){
                if ("SPEED".equals(str)) {
                    speed = speed + cfg.getIntegerList("Effects").get(cfg.getStringList("Types").indexOf(str));
                }
            }
        }
        for(AttributeModifier at:p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers()){
            if(at.getName().contains("IEN")){
                if ("SPEED".equals(at.getName().split("_")[1])) {
                    p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(at);
                }
            }
        }
        if(speed>0)
            p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).addModifier(new AttributeModifier("IEN_SPEED",0.001*speed, AttributeModifier.Operation.ADD_NUMBER));
    }
    public static Integer getStat(Player p,String stat){
        List<ItemStack> items = GenerateENList(p);
        int stati = 0;
        for(ItemStack item:items){
            NBTItem nbt = new NBTItem(item);
            ConfigurationSection cfg = Main.data.getConfig().getConfigurationSection("Enrichments."+nbt.getString("E_ID")+".Perks."+Main.data.getConfig().getConfigurationSection("Enrichments."+nbt.getString("E_ID")+".Perks").getKeys(false).toArray()[nbt.getInteger("E_LV")]);
            for(String str:cfg.getStringList("Types")){
                if(str.equals(stat))
                    stati = stati + cfg.getIntegerList("Effects").get(cfg.getStringList("Types").indexOf(str));
                }
        }
        return stati;
    }
}
