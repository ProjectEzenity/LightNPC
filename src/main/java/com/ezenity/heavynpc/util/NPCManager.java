package com.ezenity.heavynpc.util;

import com.ezenity.heavynpc.configuration.Config;
import com.ezenity.heavynpc.configuration.Lang;
import com.ezenity.heavynpc.util.nms.NPCNetworkManager;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.v1_16_R2.Entity;
import net.minecraft.server.v1_16_R2.NetworkManager;
import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PlayerInteractManager;
import net.minecraft.server.v1_16_R2.World;
import net.minecraft.server.v1_16_R2.WorldServer;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;

import com.ezenity.heavynpc.Main;
import com.ezenity.heavynpc.util.nms.BServer;
import com.ezenity.heavynpc.util.nms.BWorld;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * NPC Manager.
 * <p>
 * Handles all task for a light/heavy npc.
 *
 * @author Ezenity
 * @version 0.1.0
 */
public class NPCManager {
    /**
     * Initialize a plugin instance. This is used for instantiating the npc to the plugin.
     */
    private final Main plugin;
    /**
     * Map of the npcs. Creates an hashmap for a npc as an integer.
     */
    private final HashMap<Integer, NPC> npcs = new HashMap<>();
    public ConcurrentHashMap<String, Mob> mobDB = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Integer, Player> mobIDs = new ConcurrentHashMap<>();
    private final HashMap<Integer, Boolean> isMob = new HashMap<>();
    private final Map<World, BWorld> bworlds = new HashMap<>();
    private int taskId;
    private final BServer server;
    private NPCNetworkManager npcNetworkManager;

    public NPCManager(Main plugin) {
        this.plugin = plugin;
        server = BServer.getInstance();
        try { npcNetworkManager = new NPCNetworkManager(); }
        catch (IOException e) { e.printStackTrace(); }
        /* TODO: Coming soon
        taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                HashSet<Integer> toRemove = new HashSet<Integer>();
                for (Integer i : npcs.keySet()) {
                    Entity j = npcs.get(i).getEntity();
                    j.z();
                    if (j.dead) {
                        toRemove.add(i);
                    }
                }
                for (Integer n : toRemove) {
                    npcs.remove(n);
                }
            }
        }, 1L, 1L);
        */
        Bukkit.getServer().getPluginManager().registerEvents(new SL(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new WL(), plugin);
    }

    public BServer getServer() {
        return server;
    }

    public NetworkManager getNPCNetworkManager() {
        return npcNetworkManager;
    }

    public BWorld getBWorld(World world) {
        BWorld bworld = bworlds.get(world);
        if (bworld != null) return bworld;
        bworld = new BWorld(world);
        bworlds.put(world, bworld);
        return bworld;
    }

    private class SL implements Listener {
        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin() == plugin) {
                if(Config.DEBUG_MODE) Logger.log(ChatColor.GREEN + "Removing all loaded NPCs.");
                despawnAll();
                Bukkit.getServer().getScheduler().cancelTask(taskId);
            }
        }
    }

    private class WL implements Listener {
        @EventHandler
        public void onChunkLoad(ChunkLoadEvent event) {
            for (NPC npc : npcs.values()) {
                if (npc != null && event.getChunk() == npc.getChunk()) {
                    BWorld world = getBWorld((World) event.getWorld());
                    world.getWorldServer().addEntity((Entity) npc.getTarget());
                }
            }
        }
    }

    public NPC spawnHumanNPC(String name, Location l) {
        Integer id = 0;
        while (npcs.containsKey(id)) id++;
        return spawnHumanNPC(name, l, id);
    }

    public NPC spawnHumanNPC(String name, Location l, Integer id) {
        if (npcs.containsKey(id)) {
            Logger.log(ChatColor.RED + "NPC with that id already exists, existing NPC returned (" + id + ")");
            return npcs.get(id);
        } else {
            if (name.length() > 16) {
                String tmp = name.substring(0, 16);
                Logger.log(ChatColor.RED + "NPCs can't have names longer than 16 characters,");
                Logger.log(ChatColor.RED + name + " has been shortened to " + tmp);
                name = tmp;
            }
            BWorld world = getBWorld(l.getWorld());
            NPCEntity npcEntity = new NPCEntity(this, world, name, new PlayerInteractManager(world.getWorldServer())); // TODO: Make NPCEntity Class
            npcEntity.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
            world.getWorldServer().addEntity(npcEntity);
            NPC npc = new HumanNPC(npcEntity); // TODO: Make HumanNPC Class
            npcs.put(id, npc);
            return npc;
        }
    }

    public void despawnById(Integer id) {
        NPC npc = npcs.get(id);
        if (npc != null) {
            npcs.remove(id);
            npc.removeFromWorld();
        }
    }

    public void despawnHuman(Integer id) {
        HashSet<Integer> toRemove = new HashSet<Integer>();
        for (Integer n : npcs.keySet()) {
            NPC npc = npcs.get(n);
            if (npc instanceof HumanNPC) {
                if ((npc != null) && (getID(npc) == id)) {
                    toRemove.add(n);
                    npc.removeFromWorld();
                }
            }
        }
        for (Integer n : toRemove) npcs.remove(n);
    }

    public void despawnAll() {
        for (NPC npc : npcs.values()) {
            if (npc != null) {
                npc.removeFromWorld();
            }
        }
        npcs.clear();
    }

    public boolean isNPC(org.bukkit.entity.Entity entity) {
        return ((CraftEntity) entity).getHandle() instanceof NPCEntity;
    }

    public List<NPC> getNPCs() {
        return new ArrayList<NPC>(npcs.values());
    }

    public NPC getNPCByID(Integer id) {
        return npcs.get(id);
    }

    public Integer getNPCIdFromEntity(org.bukkit.entity.Entity e) {
        if (e instanceof HumanEntity) {
            for (Integer i : npcs.keySet()) {
                if (npcs.get(i).getBukkitEntity().getEntityId() == ((HumanEntity) e).getEntityId()) {
                    return i;
                }
            }
        }
        return null;
    }

    public NPC getNPCByName(String name) {
        if (name.length() > 16) name = name.substring(0, 16);
        for (Integer n : npcs.keySet()) {
            NPC npc = npcs.get(n);
            if (npc instanceof HumanNPC) {
                if ((npc != null) && ((HumanNPC) npc).getName().equals(name)) {
                    return npc;
                }
            }
        }
        return null;
    }

    public NPC getNPCFromEntity(org.bukkit.entity.Entity e) {
        return getNPCByID(getNPCIdFromEntity(e));
    }

    public Integer getID(NPC npc) {
        for (Integer i : npcs.keySet()) {
            if (npcs.get(i) == npc) {
                return i;
            }
        }
        return null;
    }

    public Integer getNPCCountByName(String name) {
        Integer count = 0;
        if (name.length() > 16) name = name.substring(0, 16);
        for (Integer n : npcs.keySet()) {
            NPC npc = npcs.get(n);
            if (npc instanceof HumanNPC) {
                if ((npc != null) && ((HumanNPC) npc).getName().equals(name)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void rename(Integer id, String name) {
        if (name.length() > 16) {
            String tmp = name.substring(0, 16);
            Logger.log(ChatColor.RED + "NPCs can't have names longer than 16 characters,");
            Logger.log(ChatColor.RED + name + " has been shortened to " + tmp);
            name = tmp;
        }
        HumanNPC npc = (HumanNPC) getNPCByID(id);
        npc.setName(name);
        BWorld b = getBWorld(npc.getBukkitEntity().getLocation().getWorld());
        WorldServer s = b.getWorldServer();
        try {
            Method m = s.getClass().getDeclaredMethod("d", Entity.class);
            m.setAccessible(true);
            m.invoke(s, npc.getEntity());
            m = s.getClass().getDeclaredMethod("c", Entity.class);
            m.setAccessible(true);
            m.invoke(s, npc.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        s.everyoneSleeping(); // Not sure what this does... -Billy
    }

    @SuppressWarnings("unused")
    public void loadNPCs() {
        if (plugin.npcs == null) return;
        final Map<String, Object> opts = plugin.npcs.getValues(false);
        if (!opts.keySet().isEmpty()) {
            for (String idStr : opts.keySet()) {
                NPC npc;
                Integer id;
                try {
                    id = Integer.valueOf(idStr);
                } catch(NumberFormatException npe) {
                    plugin.log(ChatColor.RED + "[ERROR] ID in config is NOT a valid number! Skipping! (" + idStr + ")");
                    continue;
                }
                World world = plugin.getServer().getWorld((String) plugin.npcs.get(idStr + ".world"));
                if (world == null) {
                    plugin.log(ChatColor.RED + "[ERROR] No such world to spawn in! Skipping! (" + idStr + ")");
                    continue;
                }
                Location loc = new Location(
                        world,
                        (Double) plugin.npcs.get(idStr + ".x"),
                        (Double) plugin.npcs.get(idStr + ".y"),
                        (Double) plugin.npcs.get(idStr + ".z")
                );
                if (loc == null) {
                    plugin.log(ChatColor.RED + "[ERROR] Something went wrong creating Location! Skipping! (" + idStr + ")");
                    continue;
                }
                String name = (String) plugin.npcs.get(idStr + ".name");
                npc = plugin.npcManager.spawnHumanNPC(name, loc, id);
                if (plugin.npcs.get(idStr + ".message.radius") != null)
                    ((HumanNPC) npc).setMessageRadius((Double) plugin.npcs.get(idStr + ".message.radius"));
                if (plugin.npcs.get(idStr + ".message.say") != null)
                    ((HumanNPC) npc).setMessage((String) plugin.npcs.get(idStr + ".message.say"));
                MobType mobType = null;
                if (plugin.npcs.get(idStr + ".mobtype") != null) {
                    mobType = MobType.fromString((String) plugin.npcs.get(idStr + ".mobtype"));
                }
                if (mobType != null) {
                    isMob.put(id, true);
                    npcToMob(((HumanNPC) npc).getPlayer(), new Mob(id, mobType));
                } else {
                    isMob.put(id, false);
                    if (plugin.npcs.get(idStr + ".armor.boots") != null)
                        ((HumanNPC) npc).setBoots((Integer) plugin.npcs.get(idStr + ".armor.boots"));
                    if (plugin.npcs.get(idStr + ".armor.leggings") != null)
                        ((HumanNPC) npc).setLeggings((Integer) plugin.npcs.get(idStr + ".armor.leggings"));
                    if (plugin.npcs.get(idStr + ".armor.chestplate") != null)
                        ((HumanNPC) npc).setChestplate((Integer) plugin.npcs.get(idStr + ".armor.chestplate"));
                    if (plugin.npcs.get(idStr + ".armor.helmet") != null)
                        ((HumanNPC) npc).setHelmet((Integer) plugin.npcs.get(idStr + ".armor.helmet"));
                    if (plugin.npcs.get(idStr + ".in-hand") != null)
                        ((HumanNPC) npc).setHand((Integer) plugin.npcs.get(idStr + ".in-hand"));
                }
                plugin.log(ChatColor.GOLD + "NPC Loaded: " + ChatColor.YELLOW + name);
            }
        }
    }

    public void npcToMob(Player player, Mob mob) {
        mobDB.put(player.getName(), mob);
        mobIDs.put(mob.entityID, player);
        sendMob(player, null);
    }

    public void sendMob(Player npc, Player observer) {
        if (mobDB.containsKey(npc.getName())) {
            Mob mob = mobDB.get(npc.getName());
            Packet packet = mob.getMobSpawnPacket(npc.getLocation());
            if (observer == null) {
                showToWorld(npc.getWorld(), npc, packet);
            } else {
                if (observer instanceof HumanNPC) return;
                observer.hidePlayer(npc);
                ((CraftPlayer) observer).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void showToWorld(World world, Player player, Packet... packet) {
        for (Player observer : world.getPlayers()) {
            if (observer instanceof HumanNPC) continue;
            if (observer != player) {
                observer.hidePlayer(player);
                for (Packet p : packet) {
                    ((CraftPlayer) observer).getHandle().playerConnection.sendPacket(p);
                }
            }
        }
    }

    public void sendPacketToWorld(World world, Packet... packet) {
        for (Player observer : world.getPlayers()) {
            for (Packet p : packet) {
                ((CraftPlayer) observer).getHandle().playerConnection.sendPacket(p);
            }
        }
    }

    public Boolean isMob(Integer id) {
        return isMob.get(id);
    }
}
}
