package com.pigslayer.itemenrichments.Invs.EnrichmentEdit;

import com.pigslayer.itemenrichments.Handlers;
import com.pigslayer.itemenrichments.Main;
import de.tr7zw.nbtapi.NBTItem;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnricmentEditor implements InventoryHolder {
    private static Inventory inv;
    public static List<Integer> ints = Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53);
    private static final List<String> types = Arrays.asList("Edit enrichments","Editing %","Edit XP Method","Select Icon","Edit Allowed Items","Edit Perks","Editing Perk");
    private static final List<Integer> sizes = Arrays.asList(54,45,54,54,54,54,54);
    private final Integer type;
    private ConfigurationSection pcfg;
    private Integer page=0;
    public EnricmentEditor() {
        this.type=0;
        inv = Bukkit.createInventory(this,sizes.get(0), types.get(0));
        init(0,null);
    }
    public EnricmentEditor(Integer type, ConfigurationSection cfg) {
        this.pcfg=cfg;
        this.type=type;
        inv = Bukkit.createInventory(this,sizes.get(type), types.get(type).replace("%", Objects.requireNonNull(cfg.getString("DisplayName"))));
        init(type,cfg);
    }
    public EnricmentEditor(Integer type, ConfigurationSection cfg,Integer pg) {
        this.page=pg;
        this.pcfg=cfg;
        this.type=type;
        Main.debuglog(pg+"FORTHELOLS");
        inv = Bukkit.createInventory(this,sizes.get(type), types.get(type).replace("%", Objects.requireNonNull(cfg.getString("DisplayName"))));
        init(type,cfg);
    }
    private void init(Integer type, ConfigurationSection cfga){
        int ci = 0;
        switch(type){
            case 0:
                for (int i = 0; i < 54; i++) {
                    if (ints.contains(i)) {
                        inv.setItem(i, createPane(i));
                    } else {
                        if (Main.data.getConfig().getConfigurationSection("Enrichments").getKeys(false).size() > ci) {
                            ConfigurationSection cfg = Main.data.getConfig().getConfigurationSection("Enrichments." + Main.data.getConfig().getConfigurationSection("Enrichments").getKeys(false).toArray()[ci]);
                            ItemStack item = createItem(ChatColor.GREEN + cfg.getString("DisplayName"), Material.valueOf(cfg.getString("Item")), Arrays.asList(ChatColor.GOLD + "Left-Click to edit this enrichment", ChatColor.RED + "Right-Click to delete this enrichment"));
                            NBTItem ite = new NBTItem(item);
                            ite.setString("E_ID",String.valueOf(Main.data.getConfig().getConfigurationSection("Enrichments").getKeys(false).toArray()[ci]));
                            item=ite.getItem();
                            inv.setItem(i,item);
                            ci++;
                        }
                    }
                    inv.setItem(49,createItem(ChatColor.GREEN+"Create Enrichment",Material.GREEN_CONCRETE,Collections.singletonList(ChatColor.GRAY+"Create a new enrichment!")));
                }
                break;
            case 1:
                for (int i = 0; i < 45; i++) {
                    switch(i){
                        case 11:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Change Icon", Material.valueOf(cfga.getString("Item")),Arrays.asList(ChatColor.GREEN+"Current Icon: "+cfga.getString("Item"),ChatColor.GRAY+"Click an item in your inventory to set the icon",ChatColor.GRAY+"Click this to browse all icons")));
                            break;
                        case 13:
                            String str = cfga.getString("TierSubType");
                            char[] ch = str.toCharArray();
                            str=str.toLowerCase();
                            str=ch[0]+str.split(Character.toString(ch[0]).toLowerCase())[1]+"s";
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Change XP Method",Material.DIAMOND_SWORD,Arrays.asList(ChatColor.GREEN+"Current Method: Kill "+str,ChatColor.GRAY+"Click to edit")));
                            break;
                        case 15:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Change Allowed Items", Material.DIAMOND_HELMET,Collections.singletonList(ChatColor.GRAY+"Click to edit")));
                            break;
                        case 29:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Change Price",Material.GOLD_INGOT,Arrays.asList(ChatColor.GREEN+"Current Cost: "+cfga.getInt("Cost"),ChatColor.GRAY+"Click to edit")));
                            break;
                        case 31:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Change Name",Material.NAME_TAG,Arrays.asList(ChatColor.GREEN+"Current Name: "+cfga.getString("DisplayName"),ChatColor.GRAY+"Click to edit")));
                            break;
                        case 33:
                            try {
                                inv.setItem(i, createItem(ChatColor.YELLOW + "Change Tiers", Material.TOTEM_OF_UNDYING, Arrays.asList(ChatColor.GREEN + "Current Tiers: " + Main.data.getConfig().getConfigurationSection("Enrichments." + cfga.getName() + ".Perks").getKeys(false).size(), ChatColor.GRAY + "Click to edit")));
                            }catch(NullPointerException ignored){
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Change Tiers",Material.TOTEM_OF_UNDYING,Arrays.asList(ChatColor.GREEN+"Current Tiers: 0",ChatColor.GRAY+"Click to edit")));
                            }
                            break;
                        case 40:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Go Back",Material.ARROW,null));
                            break;
                        default:
                            inv.setItem(i,createPane(i));
                    }
                }
                break;
            case 2:
                int cii=page*21;
                int ciii;
                List<String> tys = new ArrayList<>();
                tys.add("ALL");
                for(Material ty:Material.values()){
                    if(ty.toString().contains("_SPAWN_EGG")){
                        tys.add(ty.toString().split("_SPAWN_EGG")[0]);
                    }
                }
                for (int i = 0; i < 54; i++) {
                    switch(i) {
                        case 4:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Kills (Selected)",Material.DIAMOND_SWORD,Arrays.asList(ChatColor.GREEN+"Selected Mob: "+Handlers.FancyString(cfga.getString("TierSubType")),ChatColor.GRAY+"Cannot be changed for now")));
                            break;
                        case 48:
                            inv.setItem(i,createPane(i));
                            if(page>0)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Previous Page",Material.ARROW,null));
                            break;
                        case 49:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Go Back",Material.BARRIER,null));
                            break;
                        case 50:
                            inv.setItem(i,createPane(i));
                            if(tys.size()>cii)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Next Page",Material.ARROW,null));
                            break;
                        default:
                        if (i < 17) {
                            inv.setItem(i, createPane(i));
                        } else if (ints.contains(i)) {
                            inv.setItem(i, createPane(i));
                        } else {
                            if (cii+1 > tys.size()) {
                                inv.setItem(i,createAir());
                            } else {
                                String str = Handlers.FancyString(tys.get(cii));
                                if(tys.get(cii).equals("ALL")){
                                    ItemStack item = createItem(ChatColor.GREEN + "All Mobs", Material.DIAMOND_SWORD, Collections.singletonList(ChatColor.GRAY + "Click to select All Mobs"));
                                    NBTItem ite = new NBTItem(item);
                                    ite.setString("E_ID", tys.get(cii));
                                    item=ite.getItem();
                                    inv.setItem(i, item);
                                }else {
                                    ItemStack item = createItem(ChatColor.GREEN + str, Material.valueOf(tys.get(cii) + "_SPAWN_EGG"), Collections.singletonList(ChatColor.GRAY + "Click to select " + str + "s"));
                                    NBTItem ite = new NBTItem(item);
                                    ite.setString("E_ID", tys.get(cii));
                                    item=ite.getItem();
                                    inv.setItem(i, item);
                                }
                                cii++;
                            }
                        }
                    }
                }
                break;
            case 3:
                cii=page*21;
                ciii=(page+1)*21;
                List<Material> mats = new ArrayList<>();
                for(Material mat:Material.values()){
                    if(!mat.isAir()&&mats.size()<1100)
                        mats.add(mat);
                }
                for (int i = 0; i < 54; i++) {
                    switch (i){
                        case 48:
                            inv.setItem(i,createPane(i));
                            if(page>0)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Previous Page",Material.ARROW,null));
                            break;
                        case 49:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Go Back",Material.BARRIER,null));
                            break;
                        case 50:
                            inv.setItem(i,createPane(i));
                            Main.debuglog(ciii+" "+mats.size()+"SOUP");
                            if(mats.size()>ciii)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Next Page",Material.ARROW,null));
                            break;
                        default:
                            if(ints.contains(i)){
                                inv.setItem(i,createPane(i));
                            }else{
                                if(mats.size()>cii){
                                    Main.debuglog(mats.get(cii).name()+" "+mats.indexOf(mats.get(cii)));
                                    inv.setItem(i,createItem(ChatColor.GREEN+Handlers.FancyString(mats.get(cii).name()),mats.get(cii), Collections.singletonList(ChatColor.GRAY + "Click to select " + Handlers.FancyString(mats.get(cii).name()))));
                                }else{
                                    inv.setItem(i,createAir());
                                }
                                cii++;
                            }
                    }
                }
                break;
            case 4:
                cii=page*28;
                ciii=(page+1)*28;
                List<String> stra = cfga.getStringList("WhitelistedItems");
                List<String> strs = Arrays.stream(("SWORD SHOVEL PICKAXE AXE HOE HELMET CHESTPLATE LEGGINGS BOOTS").split(" ")).toList();
                for (int i = 0; i < 54; i++) {
                    switch (i){
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            if(stra.contains(strs.get(i))){
                                ItemStack item = createItem(ChatColor.GREEN+Handlers.FancyString(strs.get(i)),Material.valueOf("DIAMOND_"+ strs.get(i)),Arrays.asList(ChatColor.GRAY+"Allow all "+Handlers.FancyString(strs.get(i))+"s to have this enrichment applied",ChatColor.GREEN+"ENABLED"));
                                ItemMeta meta = item.getItemMeta();
                                meta.addEnchant(Enchantment.LUCK,1,true);
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                item.setItemMeta(meta);
                                inv.setItem(i,item);
                            }else{
                                inv.setItem(i,createItem(ChatColor.GREEN+Handlers.FancyString(strs.get(i)),Material.valueOf("DIAMOND_"+ strs.get(i)),Arrays.asList(ChatColor.GRAY+"Allow all "+Handlers.FancyString(strs.get(i))+"s to have this enrichment applied",ChatColor.RED+"DISABLED")));
                            }
                            break;
                        case 48:
                            inv.setItem(i,createPane(i));
                            if(page>0)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Previous Page",Material.ARROW,null));
                            break;
                        case 49:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Go Back",Material.BARRIER,null));
                            break;
                        case 50:
                            inv.setItem(i,createPane(i));
                            if(stra.size()>ciii)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Next Page",Material.ARROW,null));
                            break;
                        default:
                            if(ints.contains(i)){
                                inv.setItem(i,createPane(i));
                            }else{
                                if(stra.size()>cii){
                                    if (!strs.contains(stra.get(cii))) {
                                        inv.addItem(createItem(ChatColor.GREEN + Handlers.FancyString(stra.get(cii)), Material.valueOf(stra.get(cii)), Collections.singletonList(ChatColor.GRAY + "Click to remove " + Handlers.FancyString(stra.get(cii)))));
                                    }
                                }else{
                                    inv.setItem(i,createAir());
                                }
                                cii++;
                            }
                    }
                }
                break;
            case 5:
                cii=page*28;
                ciii=(page+1)*28;
                try {
                    stra = cfga.getConfigurationSection("Perks").getKeys(false).stream().toList();
                }catch(NullPointerException ignored){
                    stra = new ArrayList<>();
                }
                for (int i = 0; i < 54; i++) {
                    switch(i){
                        case 48:
                            inv.setItem(i,createPane(i));
                            if(page>0)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Previous Page",Material.ARROW,null));
                            break;
                        case 49:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Go Back",Material.BARRIER,null));
                            break;
                        case 50:
                            inv.setItem(i,createPane(i));
                            if(stra.size()>ciii)
                                inv.setItem(i,createItem(ChatColor.YELLOW+"Next Page",Material.ARROW,null));
                            break;
                        case 52:
                            inv.setItem(i,createItem(ChatColor.GREEN+"Create a new perk",Material.GREEN_CONCRETE,Collections.singletonList(ChatColor.GRAY+"Click to create a new perk!")));
                            break;
                        default:
                            if(ints.contains(i)){
                                inv.setItem(i,createPane(i));
                            }else{
                                if(stra.size()>cii) {
                                    inv.setItem(i, createItem(ChatColor.GOLD+stra.get(cii), Material.WRITABLE_BOOK, Arrays.asList(ChatColor.GREEN + "Left Click to edit this perk",ChatColor.RED+"Right Click to Delete this perk")));
                                }else{
                                    inv.setItem(i,createAir());
                                }
                                cii++;
                            }
                    }
                }
                    break;
            case 6:
                List<String> abilites = Arrays.asList("SPEED","JUMP_HEIGHT","DAMAGE","KNOCKBACK_RESISTANCE","DEFENSE");
                ConfigurationSection config = cfga.getConfigurationSection("Perks."+cfga.getConfigurationSection("Perks").getKeys(false).stream().toList().get(page));
                for (int i = 0; i < 54; i++) {
                    switch(i){
                        case 13:
                            if(page==0){
                                inv.setItem(i,createItem(ChatColor.GREEN+"No XP Needed", Material.EMERALD_BLOCK, Arrays.asList(ChatColor.GRAY+"This is the base tier, and as such", ChatColor.GRAY+"No XP is needed to reach it")));
                            }else {
                                inv.setItem(i, createItem(ChatColor.GREEN + "Change Tier XP Amount", Material.EXPERIENCE_BOTTLE, Arrays.asList(ChatColor.GOLD + "Current XP Needed: " + config.getInt("TierCount"), ChatColor.GRAY + "Click to change")));
                            }
                            break;
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                            if(config.getStringList("Types").contains(abilites.get(i-29))){
                                ItemStack item = createItem(ChatColor.GREEN+Handlers.FancyString(abilites.get(i-29)),Material.DIAMOND_BLOCK,Collections.singletonList(ChatColor.GRAY+"This buff is enabled. Click to disable"));
                                NBTItem ite = new NBTItem(item);
                                ite.setString("E_ID",abilites.get(i-29));
                                item=ite.getItem();
                                inv.setItem(i, item);
                            }else{
                                ItemStack item = createItem(ChatColor.RED+Handlers.FancyString(abilites.get(i-29)),Material.REDSTONE_BLOCK,Collections.singletonList(ChatColor.GRAY+"This buff is disabled. Click to enable"));
                                NBTItem ite = new NBTItem(item);
                                ite.setString("E_ID",abilites.get(i-29));
                                item=ite.getItem();
                                inv.setItem(i, item);
                            }
                            break;
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                            if(config.getStringList("Types").contains(abilites.get(i-38))){
                                ItemStack item = createItem(ChatColor.GREEN+"Current Buff: "+config.getIntegerList("Effects").get(config.getStringList("Types").indexOf(abilites.get(i-38))).toString(),Material.GOLD_BLOCK,Arrays.asList(ChatColor.GRAY+"Right Click to remove 1. Left Click to add 1",ChatColor.GRAY+"Shift+Right Click to remove 10. Shift+Left Click to add 10"));
                                NBTItem ite = new NBTItem(item);
                                ite.setString("E_ID",abilites.get(i-38));
                                item=ite.getItem();
                                inv.setItem(i, item);
                            }else{
                                inv.setItem(i, createItem(" ",Material.BEDROCK,null));
                            }
                            break;
                        case 49:
                            inv.setItem(i,createItem(ChatColor.YELLOW+"Go Back",Material.BARRIER,null));
                            break;
                        default:
                            inv.setItem(i,createPane(i));
                    }
                }
                break;
        }
    }
    public Integer getType(){
        return type;
    }
    public Integer getPage(){
        return page;
    }
    public ConfigurationSection getPcfg(){
        return pcfg;
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
    public static ItemStack createAir() {
        return new ItemStack(Material.AIR, 1);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
