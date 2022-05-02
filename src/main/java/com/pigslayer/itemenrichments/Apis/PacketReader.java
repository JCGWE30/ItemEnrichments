package com.pigslayer.itemenrichments.Apis;

import com.pigslayer.itemenrichments.Main;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReader {
    Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<>();

    public void inject(Player player){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        channel = craftPlayer.getHandle().b.a.m;
        channels.put(player.getUniqueId(), channel);

        if(channel.pipeline().get("PacketInjector")!=null)
            return;

        channel.pipeline().addAfter("decoder","PacketInjector", new MessageToMessageDecoder<PacketPlayInWindowClick>(){

            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInWindowClick packet, List<Object> arg) throws Exception {
                arg.add(packet);
                readPacket(player, packet);
            }
        });
        if(channel.pipeline().get("PacketSInjector")!=null)
            return;

        channel.pipeline().addAfter("decoder","PacketSInjector", new MessageToMessageDecoder<PacketPlayInCloseWindow>(){

            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInCloseWindow packet, List<Object> arg) throws Exception {
                arg.add(packet);
                readSPacket(player, packet);
            }
        });
    }

    public void uninject(Player player){
        channel = channels.get(player.getUniqueId());
        if(channel.pipeline().get("PacketInjector")!=null)
            channel.pipeline().remove("PacketInjector");
        if(channel.pipeline().get("PacketSInjector")!=null)
            channel.pipeline().remove("PacketSInjector");
    }

    public void readPacket(Player player, Packet<?> packet){
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInWindowClick")){
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    PacketPlayInWindowClick packett = ((PacketPlayInWindowClick)packet);
                    Bukkit.getPluginManager().callEvent(new SignEvent(player,packett.c(),packett.e()));
                }
            }, 0);
        }
    }
    public void readSPacket(Player player, Packet<?> packet){
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInCloseWindow")){
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new SignEvent(player));
                }
            }, 0);
        }
    }
}