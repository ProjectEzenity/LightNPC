package com.ezenity.heavynpc.util.nms;

import net.minecraft.server.v1_16_R2.DedicatedPlayerList;
import net.minecraft.server.v1_16_R2.DedicatedServer;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import net.minecraft.server.v1_16_R2.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server hacks for Bukkit
 *
 * @author Kekec852
 *  - Updated by Ezenity
 * @version 0.1.0
 * @since 0.0.1
 */
public class BServer {
    private static BServer ins;
    private MinecraftServer mcServer;
    private CraftServer cServer;
    private Server server;
    private HashMap<String, BWorld> worlds = new HashMap<>();

    private BServer() {
        server = Bukkit.getServer();
        try {
            cServer = (CraftServer) server;
            mcServer = cServer.getServer();
        } catch (Exception ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
    }

    public void disablePlugins() {
        cServer.disablePlugins();
    }

    public void dispatchCommand(CommandSender sender, String msg) {
        cServer.dispatchCommand(sender, msg);
    }

    public DedicatedPlayerList getHandle() {
        return cServer.getHandle();
    }

    public ConsoleReader getReader() {
        return cServer.getReader();
    }

    public void loadPlugins() {
        cServer.loadPlugins();
    }

    public void stop() {
        mcServer.safeShutdown(true);
    }

    public void sendConsoleCommand(String cmd) {
//        if (!mcServer.isStopped && MinecraftServer.isRunning(mcServer)) {
        if (mcServer.isRunning()) {
//            ((DedicatedServer) mcServer).issueCommand(cmd, mcServer);
            ((DedicatedServer) mcServer).issueCommand(cmd, mcServer.getServerCommandListener());
        }
    }

    public Logger getLogger() {
        return cServer.getLogger();
    }

    public List<WorldServer> getWorldServers() {
//        return mcServer.worlds;
        return (List<WorldServer>) mcServer.getWorlds();
    }

    public int getSpawnProtationRadius() {
        return mcServer.server.getSpawnRadius();
    }

// TODO: Still needs updating
////    public PropertyManager getPropertyManager() {
////        return mcServer.getPropertyManager();
////    }
//
//    public PropertyManager getPropertyManager() {
////        return mcServer.getPropertyManager(); // 1_5_R1
////        return (PropertyManager) mcServer.console.getServer().getServicesManager();
//        return mcServer.console.getServer().getServicesManager().;
//
//    }

    public Server getServer() {
        return server;
    }

    public BWorld getWorld(String worldName) {
        if (worlds.containsKey(worldName)) {
            return worlds.get(worldName);
        }
        BWorld w = new BWorld(this, worldName);
        worlds.put(worldName, w);
        return w;
    }

    public static BServer getInstance() {
        if (ins == null) {
            ins = new BServer();
        }
        return ins;
    }

    public MinecraftServer getMCServer() {
        return mcServer;
    }
}