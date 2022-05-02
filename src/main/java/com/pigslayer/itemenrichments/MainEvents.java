package com.pigslayer.itemenrichments;

import com.pigslayer.itemenrichments.Apis.PacketReader;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class MainEvents implements Listener {
    public static ArrayList<Player> jumpers = new ArrayList<>();
    @EventHandler
    public static void join(PlayerJoinEvent e){
        PacketReader reader = new PacketReader();
        reader.inject(e.getPlayer());
    }
    @EventHandler
    public static void hit(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            double psg = Handlers.getStat(p, "DAMAGE").doubleValue() / 100;
            e.setDamage(e.getDamage() + (e.getDamage() *psg));
            }
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            double psg = Handlers.getStat(p, "KNOCKBACK_RESISTANCE").doubleValue() / 100;
            double dsg = Handlers.getStat(p, "DEFENSE").doubleValue() / 100;
            e.setDamage(e.getDamage()*(1-dsg));
            if(psg>0) {
                p.setVelocity(p.getLocation().getDirection().multiply(-1).multiply(1 - psg));
            }
        }
    }
    @EventHandler
    public static void jump(PlayerMoveEvent e){
        boolean isJumping = e.getPlayer().getVelocity().getY() > 0;
        if(isJumping){
            if(e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().subtract(0,1,0)).getType()==Material.AIR) {
                if (!jumpers.contains(e.getPlayer())) {
                    jumpers.add(e.getPlayer());
                    double psg = Handlers.getStat(e.getPlayer(), "JUMP_HEIGHT").doubleValue();
                    e.getPlayer().setVelocity(e.getPlayer().getVelocity().add(new Vector(0, psg / 10, 0)));
                }
            }
        }
        if(e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().subtract(0,1,0)).getType()!=Material.AIR){
            if(jumpers.contains(e.getPlayer())){
                jumpers.remove(e.getPlayer());
            }
        }
    }
    @EventHandler
    public static void leave(PlayerQuitEvent e){
        PacketReader reader = new PacketReader();
        reader.uninject(e.getPlayer());
    }
    @EventHandler
    public static void kill(EntityDeathEvent e){
        if(e.getEntity().getKiller()!=null){
            Handlers.HandleENxp(1,e.getEntityType(),e.getEntity().getKiller());
        }
    }
    @EventHandler
    public static void armorbuff(InventoryClickEvent e){
        Main.getPlugin(Main.class).getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                Handlers.calculateStats(((Player) e.getWhoClicked()));
            }
        }, (int)0.01);
    }
    @EventHandler
    public static void armorvuff(InventoryCloseEvent e){
        Handlers.calculateStats(((Player) e.getPlayer()));
    }
    @EventHandler
    public static void armorbuff(PlayerInteractEvent e){
        Main.getPlugin(Main.class).getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                Handlers.calculateStats(e.getPlayer());
            }
        }, (int)0.01);
    }
    @EventHandler
    public static void armorbuff(PlayerItemHeldEvent e){
        Main.getPlugin(Main.class).getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                Handlers.calculateStats(e.getPlayer());
            }
        }, (int)0.01);
    }
}
