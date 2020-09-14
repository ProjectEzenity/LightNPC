package com.ezenity.lightnpc.util.npcFx;

import com.ezenity.lightnpc.Main;
import com.ezenity.lightnpc.configuration.Lang;
import com.ezenity.lightnpc.util.Logger;
import com.ezenity.lightnpc.util.SlotType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * NPC Manager.
 * <p>
 * Adds basic NPCs to your server. The NPCs do have a limited functionality such as talking and the ability to wear armor and hold items.
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class NPCManager {
    private static HashMap<String,NPC> selectedNPC = new HashMap<
            >();
    private static List<NPC> npcList = new ArrayList<>();
    private static int nextId = Short.MAX_VALUE;

    public static Integer next() {
        for(int i = 0; i >= 0; i++)
            if(getNPCbyID(i) == null)
                return i;
        return 0;
    }

    public static NPC spawnNPC(String name, Player player, Location loc) {
        Integer id = next();
        if (name == null)
            name = "NPC" + id;
        if (loc == null)
            loc = player.getLocation();
        NPC npc = new NPC(id, nextId--, loc, name, player);
        npc.getWorld().spawn(loc, );// .spawn(loc.getWorld());
        npcList.add(npc);
        return npc;
    }

    public static void despawnNPC(NPC npc) {
        if (npc == null)
            return;
        npcList.remove(npc);
        npc.despawn();
    }

    public static void despawnAll() {
        List<NPC> toDespawn = new ArrayList<NPC>();
        for (NPC npc : getNPCList())
            toDespawn.add(npc);
        for (NPC npc : toDespawn)
            despawnNPC(npc);
    }

    public static NPC getNPCbyID(Integer id) {
        for(NPC npc: npcList)
            if (npc.getId() == id)
                return npc;
        return null;
    }

    public static NPC getNPCbyName(String name) {
        for(NPC npc: npcList)
            if (npc.getName().equals(name))
                return npc;
        return null;
    }

    public static List<NPC> getNPCList() {
        return npcList;
    }

    public static void setSelected(String player, NPC npc) {
        if (npc == null)
            if (selectedNPC.containsKey(player))
                selectedNPC.remove(player);
        if (npcList.contains(npc))
            selectedNPC.put(player, npc);
    }

    public static NPC getSelected(String player) {
        return selectedNPC.get(player);
    }

    public static Boolean selectedAllowed(Player player, Boolean isAdmin) {
        if (!selectedNPC.containsKey(player.getName())) {
            player.sendMessage(Lang.colorize("&4You have not selected an NPC!"));
            return false;
        }
        NPC npc = selectedNPC.get(player.getName());
        if (npc == null) {
            player.sendMessage(Lang.colorize("&4Could not find selected NPC!"));
            return false;
        }
        if (isAdmin && player.hasPermission("pl3xnpc.admin"))
            return true;
        if (!npc.getOwner().equals(player.getName())) {
            Bukkit.getLogger().log(Level.INFO, "Player: " + player.getName() + " Owner: " + npc.getOwner());
            player.sendMessage(Lang.colorize("&4You do not own this NPC!"));
            return false;
        }
        return true;
    }

    public static void loadAll(Main plugin) {
        despawnAll();
        ConfigurationSection conf = plugin.getConfig().getConfigurationSection("npcs");
        if (conf == null)
            return;
        Map<String, Object> opts = conf.getValues(false);
        if (!opts.keySet().isEmpty()) {
            for (String id : opts.keySet()) {
                Integer npcID;
                try {
                    npcID = Integer.valueOf(id);
                } catch(NumberFormatException npe) {
                    if (plugin.getConfig().getBoolean("debug-mode"))
                        Logger.log("&4[ERROR] ID in config is NOT a valid number! Skipping! (" + id + ")");
                    continue;
                }
                loadNPCbyID(plugin, npcID);
            }
        }
    }

    public static void loadNPCbyID(Main plugin, Integer id) {
        ConfigurationSection conf = plugin.getConfig().getConfigurationSection("npcs");
        World world = plugin.getServer().getWorld(conf.getString(id + ".world"));
        if (world == null) {
            if (plugin.getConfig().getBoolean("debug-mode"))
                Logger.log("&4[ERROR] No such world to spawn in! Skipping! (" + id + ")");
            return;
        }
        Location loc = new Location(world, conf.getDouble(id + ".x"), conf.getDouble(id + ".y"), conf.getDouble(id + ".z"));
        String name = "NPC" + id;
        if (conf.get(id + ".name") != null)
            name = conf.getString(id + ".name");
        NPC npc = new NPC(id, nextId--, loc, name, null);
        npc.setOwner(conf.getString(id + ".owner"));
        if (conf.get(id + ".mobtype") != null) {
            if (conf.get(id + ".showmobname") != null) {
                Boolean showName = conf.getBoolean(id + ".showmobname");
                npc.showMobName(showName);
            }
            String mobType = conf.getString(id + ".mobtype");
            MobType mob = MobType.fromString(mobType);
            npc.setMob(mob);
        }
        if (conf.get(id + ".face") != null) {
            Object x = conf.get(id + ".face.x");
            Object y = conf.get(id + ".face.y");
            Object z = conf.get(id + ".face.z");
            if (x != null && y != null && z != null) {
                Location face = new Location(world, (Double) x, (Double) y, (Double) z);
                if (face != null)
                    npc.setFaceLocation(face);
            }
        }
        loadNPCMessage(conf.getConfigurationSection(id + ".message"), npc);
        loadNPCItems(plugin, conf.getConfigurationSection(id + ".items"), npc);
        npc.spawn(loc.getWorld());
        npcList.add(npc);
        if (plugin.getConfig().getBoolean("debug-mode"))
            plugin.log("&6NPC Loaded: &e" + npc.getName());
    }

    public static void loadNPCItems(Main plugin, ConfigurationSection conf, NPC npc) {
        if (conf== null)
            return;
        if (npc == null)
            return;
        for (InventoryType.SlotType type : InventoryType.SlotType.values()) {
            String typeStr = type.toString().toLowerCase();
            if (conf.get(typeStr + ".material") != null) {
                Material mat = Material.getMaterial(conf.getString(typeStr + ".material"));
                if (mat == null) {
                    if (plugin.getConfig().getBoolean("debug-mode"))
                        Logger.log("&4The " + typeStr + " material is unknown! Not equipping!");
                    continue;
                }
                ItemStack item = new ItemStack(mat, 1);
                if (conf.get(typeStr + ".displayname") != null) {
                    String name = conf.getString(typeStr + ".displayname");
                    item.getItemMeta().setDisplayName(name);
                }
                if (conf.get(typeStr + ".durability") != null) {
                    String dura = conf.getString(typeStr + ".durability");
                    try {
                        short durability = Short.parseShort(dura);
                        item.setDurability(durability);
                    } catch(Exception e) {
                        if (plugin.getConfig().getBoolean("debug-mode"))
                            Logger.log("&4The " + typeStr + " durability is unknown! Not setting value!");
                    }
                }
                if (conf.get(typeStr + ".data") != null) {
                    String d = conf.getString(typeStr + ".data");
                    try {
                        Byte data = Byte.valueOf(d);
                        item.getData().setData(data);
                    } catch (Exception e) {
                        if (plugin.getConfig().getBoolean("debug-mode"))
                            Logger.log("&4The " + typeStr + " data is unknown! Not setting value!");
                    }
                }
                if (conf.getStringList(typeStr + ".lore") != null) {
                    List<String> lores = conf.getStringList(typeStr + ".lore");
                    item.getItemMeta().setLore(lores);
                }
                if (conf.get(typeStr + ".enchantment") != null) {
                    Map<String, Object> opts = conf.getConfigurationSection(typeStr + ".enchantment").getValues(true);
                    for (Map.Entry<String, Object> entry : opts.entrySet()) {
                        try {
                            Integer id = Integer.valueOf(entry.getKey().toString());
                            Integer level = Integer.valueOf(entry.getValue().toString());
                            Enchantment enchantment = new EnchantmentWrapper(id);
                            item.addEnchantment(enchantment, level);
                        } catch (Exception e) {
                            if (plugin.getConfig().getBoolean("debug-mode"))
                                plugin.log("&4The " + typeStr + " enchantment " + entry.getKey() + " is unknown! Not setting value!");
                        }
                    }
                }
                if (Utils.isLeatherArmor(item) && conf.get(typeStr + ".color") != null) {
                    Integer rgb = conf.getInt(typeStr + ".color");
                    Utils.setLeatherColor(item, rgb);
                }
                if (type == SlotType.IN_HAND) {
                    npc.setItem(item, type);
                    if (plugin.getConfig().getBoolean("debug-mode"))
                        plugin.log("&6Set item for " + type.toString().toLowerCase() + ": " + mat.toString());
                    continue;
                }
                if (Utils.isValidArmor(plugin, type, mat)) {
                    npc.setItem(item, type);
                    if (plugin.getConfig().getBoolean("debug-mode"))
                        plugin.log("&6Set item for " + type.toString().toLowerCase() + ": " + mat.toString());
                    continue;
                }
                if (plugin.getConfig().getBoolean("debug-mode"))
                    plugin.log("&4Invalid item for " + type.toString().toLowerCase() + "! Not equipping!");
            }
        }
    }

    public static void loadNPCMessage(ConfigurationSection conf, NPC npc) {
        if (conf == null)
            return;
        if (npc == null)
            return;
        if (conf.get("radius") != null)
            npc.setMsgRadius(conf.getDouble("radius"));
        if (conf.get("say") != null)
            npc.setMsg(conf.getString("say"));
    }

    @SuppressWarnings("deprecation")
    public static void subtractFromInventory(Player p, ItemStack item, Boolean click) {
        Integer slot = -1;
        if (click)
            slot = p.getInventory().getHeldItemSlot();
        else
            slot = inventoryHasItem(p.getInventory(), item, false);
        if (slot >= 0) {
            ItemStack stack = item.clone();
            stack.setAmount(1);
            ItemStack slotStack = p.getInventory().getItem(slot);
            Integer amount = slotStack.getAmount();
            if (amount <= 1)
                p.getInventory().clear(slot);
            else
                slotStack.setAmount(amount - 1);
            p.updateInventory(); // temp fix
            return;
        }
        return;
    }

    @SuppressWarnings("deprecation")
    public static void giveItemBack(Player p, Location dropLoc, ItemStack item) {
        Integer slot = inventoryHasItem(p.getInventory(), item, true);
        if (slot >= 0) {
            ItemStack stack = p.getInventory().getItem(slot);
            if (stack == null || stack.getTypeId() == 0)
                p.getInventory().addItem(item);
            else
                stack.setAmount(stack.getAmount() + 1);
            p.updateInventory(); // temp fix
            return;
        }
        dropLoc.getWorld().dropItemNaturally(dropLoc, item);
    }

    public static Integer inventoryHasItem(PlayerInventory inventory, ItemStack item, Boolean allowAir) {
        for (ItemStack stack : inventory.getContents()) {
            if (stack == null || stack.getTypeId() == 0) {
                if (allowAir)
                    return inventory.firstEmpty();
                continue;
            }
            if (stack.getType() != item.getType())
                continue;
            if (stack.getData().getData() != item.getData().getData())
                continue;
            if (stack.getDurability() != item.getDurability())
                continue;
            if (stack.getEnchantments() != item.getEnchantments())
                continue;
            if (stack.getItemMeta().getDisplayName() != item.getItemMeta().getDisplayName())
                continue;
            if (stack.getItemMeta().getLore() != item.getItemMeta().getLore())
                continue;
            if (stack.getAmount() < 1)
                continue;
            return inventory.first(stack);
        }
        return -1;
    }

    public static boolean setItem(Main plugin, ItemStack item, Player p, NPC npc, Boolean canSpawn, SlotType type, Boolean click) {
        if (type != SlotType.IN_HAND && !Utils.isValidArmor(plugin, type, item.getType())) {
            p.sendMessage(Lang.colorize("&4Thats not a valid item for a " + type.toString().toLowerCase() + "!"));
            return true;
        }
        ItemStack newItem = item.clone();
        newItem.setAmount(1);
        if (!canSpawn)
            subtractFromInventory(p, item, click);
        ItemStack oldItem = npc.setItem(newItem, type);
        if (oldItem != null && oldItem.getTypeId() != 0 && !canSpawn)
            giveItemBack(p, npc.getLocation(), oldItem);
        Integer npcID = npc.getId();
        String typeString = type.toString().toLowerCase();
        Material material = item.getType();
        if (material == null || material.equals(Material.AIR))
            plugin.getConfig().set("npcs." + npcID + ".items." + typeString, null);
        else {
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            List<String> lore = item.getItemMeta().getLore();
            Color color = Utils.getLeatherColor(item);
            plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".id", item.getTypeId());
            plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".material", material.toString());
            plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".displayname", item.getItemMeta().getDisplayName());
            plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".durability", item.getDurability());
            plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".data", item.getData().getData());
            plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".lore", lore);
            if (color == null)
                plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".color", null);
            else
                plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".color", color.asRGB());
            if (enchantments.isEmpty())
                plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".enchantment", null);
            else {
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    Integer level = entry.getValue();
                    plugin.getConfig().set("npcs." + npcID + ".items." + typeString + ".enchantment." + enchantment.getId(), level);
                }
            }
        }
        plugin.saveConfig();
        p.sendMessage(Lang.colorize("&dItem set for NPC " + typeString + "."));
        if (plugin.getConfig().getBoolean("debug-mode"))
            Logger.log("&dNPC changed " + typeString + " &7" + item.getType().toString());
        return true;
    }
}
