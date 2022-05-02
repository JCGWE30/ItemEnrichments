package com.pigslayer.itemenrichments;

import com.pigslayer.itemenrichments.Apis.PacketReader;
import com.pigslayer.itemenrichments.Commands.DebugXP;
import com.pigslayer.itemenrichments.Commands.EnrichItem;
import com.pigslayer.itemenrichments.Commands.cfgreload;
import com.pigslayer.itemenrichments.Commands.edirenrichments;
import com.pigslayer.itemenrichments.Files.Config;
import com.pigslayer.itemenrichments.Invs.EnrichInvEvents;
import com.pigslayer.itemenrichments.Invs.EnrichmentEdit.EditorEvents;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static boolean debug = true;
    public static Economy eco;
    public static Config data;
    public static String prefix;
    @Override
    public void onEnable(){
        for(Player p: Bukkit.getOnlinePlayers()){
            PacketReader reader = new PacketReader();
            reader.inject(p);
        }
        log("Setting up ItemEnrichments");
        prefix=ChatColor.translateAlternateColorCodes('&',"&6&l[&3&lItemEnrichments&6&l]&r");
        data=new Config(this);
        if(!setupEconomy()){
            this.getLogger().severe("No vault dependency found. Disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }
        int pluginId = 14905;
        Metrics metrics = new Metrics(this,pluginId);
        getServer().getPluginManager().registerEvents(new EditorEvents(), this);
        getCommand("enrichitem").setExecutor(new EnrichItem());
        getCommand("debugxp").setExecutor(new DebugXP());
        getCommand("editenrich").setExecutor(new edirenrichments());
        getCommand("enrichmentsreload").setExecutor(new cfgreload());
        getServer().getPluginManager().registerEvents(new EnrichInvEvents(), this);
        getServer().getPluginManager().registerEvents(new MainEvents(),this);
        log("ItemEnrichments ready to go");
    }
    @Override
    public void onDisable(){
        for(Player p: Bukkit.getOnlinePlayers()){
            PacketReader reader = new PacketReader();
            reader.uninject(p);
        }
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economy!=null)
            eco = economy.getProvider();
        return (eco!=null);
    }
    public static void debuglog(String msg){
        if(debug)
            Main.getPlugin(Main.class).getLogger().info("DEBUG: "+msg);
    }
    public static void log(String msg){
        Main.getPlugin(Main.class).getLogger().info(msg);
    }
}
