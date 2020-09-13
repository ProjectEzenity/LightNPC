package com.ezenity.lightnpc.util.nms;

import net.minecraft.server.v1_16_R2.WorldProviderNormal;
import net.minecraft.server.v1_16_R2.WorldServer;
import net.minecraft.server.v1_16_R2.World;
import net.minecraft.server.v1_16_R2.Explosion;
import net.minecraft.server.v1_16_R2.PlayerChunkMap;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.AxisAlignedBB;
import net.minecraft.server.v1_16_R2.EntityPlayer;

import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 *  - Modifed by Ezenity
 * @version 0.1.0
 * @since 0.0.1
 */
public class BWorld {
    private BServer server;
    private World world;
    private CraftWorld cWorld;
    private World mcWorld;
    private WorldServer wServer;
//    private WorldProvider wProvider; // 1_5_R1
    private WorldProviderNormal wProvider;

    public BWorld(BServer server, String worldName) {
        this.server = server;
//        world = server.getServer().getWorld(worldName); // 1_5_R1
        world = (net.minecraft.server.v1_16_R2.World) server.getServer().getWorld(worldName);
        try {
//            cWorld = (CraftWorld) world; // 1_5_R1
            cWorld = world.getWorld();
            wServer = cWorld.getHandle();
//            wProvider = wServer.worldProvider; 1_5_R1
//            wProvider = wServer.getMinecraftWorld().getChunkProvider(); // Reverted
            wProvider = (WorldProviderNormal) wServer.worldData; // TODO: Needs testing
        } catch (Exception ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
    }

    public BWorld(World world) {
        this.world = world;
        try {
//            cWorld = (CraftWorld) world; // 1_5_R1
            cWorld = world.getWorld();
            wServer = cWorld.getHandle();
//            wProvider = wServer.worldProvider; // 1_5_R1
            wProvider = (WorldProviderNormal) wServer.worldData; // TODO: Needs testing
        } catch (Exception ex) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
    }

    public PlayerChunkMap getPlayerManager() {
//        return wServer.getPlayerChunkMap(); // 1_5_R1
        return wServer.getChunkProvider().playerChunkMap;
    }

    public CraftWorld getCraftWorld() {
        return cWorld;
    }

    public WorldServer getWorldServer() {
        return wServer;
    }

    public WorldProviderNormal getWorldProvider() {
        return wProvider;
    }

    public boolean createExplosion(double x, double y, double z, float power) {
        return !wServer.explode(null, x, y, z, power, Explosion.Effect.DESTROY).wasCanceled;
    }

    public boolean createExplosion(Location l, float power) {
        return !wServer.explode(null, l.getX(), l.getY(), l.getZ(), power, Explosion.Effect.DESTROY).wasCanceled;
    }

    public void removeEntity(String name, final Player player, JavaPlugin plugin) {
        server.getServer().getScheduler().callSyncMethod(plugin, () -> {
            Location loc = player.getLocation();
            CraftWorld craftWorld = (CraftWorld) player.getWorld();
            CraftPlayer craftPlayer = (CraftPlayer) player;
            double x = loc.getX() + 0.5;
            double y = loc.getY() + 0.5;
            double z = loc.getZ() + 0.5;
            double radius = 10;
//          AxisAlignedBB bb = AxisAlignedBB.a(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius); // 1_5_R1
            AxisAlignedBB bb = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
            List<Entity> entities = craftWorld.getHandle().getEntities(craftPlayer.getHandle(), bb);
            for (Entity o : entities) {
                if (!(o instanceof EntityPlayer)) {
                    o.getBukkitEntity().remove();
                }
            }
            return null;
        });
    }
}