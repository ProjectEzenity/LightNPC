package com.ezenity.lightnpc.util.npcFx;

import com.ezenity.lightnpc.util.ReflectionUtil;
import net.minecraft.server.v1_16_R2.DataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * NPC Class
 *
 * @version 0.0.1
 * @since 0.2.0
 */
public class NPC implements InventoryHolder {
    private Integer id;
    private MobType mob;
    private Location loc;
    private Integer entityID;
    private String name;
    private Float yaw;
    private Float pitch;
    private Float headyaw;
    private String owner;
    private Double lookAtRadius;
    private Location faceLocation;
    private Double messageRadius;
    private String message = "";
    private Boolean showMobName = true;
    private Inventory inventory;
    private PacketGenerator packetGenerator;
    private DataWatcher datawatcher;

    public NPC(Integer id, Integer entityID, Location loc, String name, Player player) {
        this.id = id;
        this.loc = loc;
        if (name.length() > 16)
            name = name.substring(0, 15);
        this.name = name;
        this.entityID = entityID;
        if (player != null) {
            this.yaw = ReflectionUtil.getYaw(player);
            this.pitch = ReflectionUtil.getPitch(player);
            this.headyaw = ReflectionUtil.getHeadYaw(player);
            this.owner = player.getName();
        }
        this.inventory = Bukkit.getServer().createInventory(this, 9, "NPC Inventory");
        this.packetGenerator = new PacketGenerator(this);
        this.createDefaultDatawatcher();
    }

    public int getId() {
        return id;
    }

    public int getEntityId() {
        return entityID;
    }

    public Location getLocation() {
        return loc;
    }

    public void setMob(MobType mob) {
        this.mob = mob;
        createDefaultDatawatcher();
        if (mob != null) {
            if (showMobName) {
                this.getDataWatcher().set(10, name);
                this.getDataWatcher().set(11, (byte) 1);
            } else {
                this.getDataWatcher().set(10, "");
                this.getDataWatcher().set(11, (byte) 0);
            }
        }
    }

    public MobType getMob() {
        return mob;
    }

    public Sound getSoundSelect() {
        if (mob != null)
            return getMob().getSoundSelect();
        return null;
    }

    public void showMobName(Boolean flag) {
        showMobName = flag;
        setMob(mob);
    }

    public void setName(String name) {
        if (name.length() > 16)
            name = name.substring(0, 15);
        this.name = name;
        if (mob != null)
            setMob(mob);
        despawn();
        spawn(loc.getWorld());
    }

    public String getName() {
        return name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setLookAtRadius(Double radius) {
        lookAtRadius = radius;
    }

    public Double getLookAtRadius(Double radius) {
        if (radius < 1 || radius > 50)
            radius = 10D;
        return (lookAtRadius != null) ? lookAtRadius : radius;
    }

    public Location getFaceLocation() {
        return faceLocation;
    }

    public void setFaceLocation(Location loc) {
        faceLocation = loc;
    }

    public void setMsgRadius(Double radius) {
        messageRadius = radius;
    }

    public Double getMsgRadius(Double radius) {
        if (radius < 1 || radius > 50)
            radius = 5D;
        return (messageRadius != null) ? messageRadius : radius;
    }

    public void setMsg(String msg) {
        message = msg;
    }

    public String getMsg() {
        return message;
    }

    public DataWatcher getDataWatcher() {
        return datawatcher;
    }

    public Float getYaw() {
        return yaw;
    }

    public Float getPitch() {
        return pitch;
    }

    public Float getHeadYaw() {
        return headyaw;
    }

    public ItemStack getItem(InventoryType.SlotType type) {
        return inventory.getItem(type.getId());
    }

    public ItemStack setItem(ItemStack stack, InventoryType.SlotType type) {
        ItemStack item = stack.clone();
        item.setAmount(1);
        ItemStack oldItem = inventory.getItem(type.getId());
        inventory.setItem(type.getId(), item);
        showItems();
        return oldItem;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void lookAt(Location point) {
        Location npcLoc = getEyeLocation();

        double xDiff = point.getX() - npcLoc.getX();
        double yDiff = point.getY() - npcLoc.getY();
        double zDiff = point.getZ() - npcLoc.getZ();

        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
        double newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
        if (zDiff < 0.0)
            newYaw = newYaw + Math.abs(180 - newYaw) * 2;

        newYaw = (newYaw - 90);
        yaw = (float) newYaw;
        pitch = (float) newPitch;
        headyaw = (float) newYaw;

        updatePosition();
    }

    public Location getEyeLocation() {
        Location l = getLocation().clone();
        l.setY(l.getY() + getHeadHeight());
        return l;
    }

    public Float getHeadHeight() {
        Float height = (float) 1.62D;
        if (mob != null)
            height = mob.getLength();
        return height * 0.85F;
    }

    public void soundSelect(Player player) {
        Sound sound = getSoundSelect();
        if (sound != null)
            loc.getWorld().playSound(loc, sound, 1.0F, 1.0F);
    }

    public PacketGenerator getPacketGenerator() {
        return packetGenerator;
    }

    private void createDefaultDatawatcher() {
        datawatcher = new DataWatcher();
        datawatcher.set(0, (byte) 0);
        if(mob != null) {
            if (mob == MobType.Zombie)
                datawatcher.set(12, (byte) 0);
            if (mob == MobType.PigZombie)
                datawatcher.set(12, (byte) 0);
            if (mob == MobType.WitherSkeleton)
                datawatcher.set(13, (byte) 1);
            if (mob == MobType.Horse) {
                datawatcher.set(16, (int) 	0);
            }
            if (mob == MobType.Donkey) {
                datawatcher.set(16, 0);
                datawatcher.set(19, (byte) 1);
            }
            if (mob == MobType.Mule) {
                datawatcher.set(16, 0);
                datawatcher.set(19, (byte) 2);
            }
            if (mob == MobType.SkeletonHorse) {
                datawatcher.set(16, 0);
                datawatcher.set(19, (byte) 4);
            }
            if (mob == MobType.ZombieHorse) {
                datawatcher.set(16, 0);
                datawatcher.set(19, (byte) 3);
            }
        } else
            datawatcher.set(12, (int) 0);
    }

    public void spawn(World world) {
        CommonPacket packet = getSpawnPacket();
        for(Player player : world.getPlayers())
            PacketUtil.sendPacket(player, packet, true);
        updatePosition();
        showItems();
    }

    public void spawn(Player player) {
        CommonPacket packet = getSpawnPacket();
        PacketUtil.sendPacket(player, packet, true);
        updatePosition(player);
        showItems();
    }

    public void despawn() {
        CommonPacket packet = packetGenerator.getDespawnPacket();
        for(Player player : loc.getWorld().getPlayers())
            PacketUtil.sendPacket(player, packet, false);
    }

    public void despawn(Player player) {
        CommonPacket packet = packetGenerator.getDespawnPacket();
        PacketUtil.sendPacket(player, packet, false);
    }

    public void updateDataWatcher() {
        CommonPacket packet = packetGenerator.getMetadataPacket();
        for(Player player : loc.getWorld().getPlayers())
            PacketUtil.sendPacket(player, packet, true);
    }

    public void updateDataWatcher(Player player) {
        CommonPacket packet = packetGenerator.getMetadataPacket();
        PacketUtil.sendPacket(player, packet, true);
    }

    public void animateArmSwing(Player player) {
        CommonPacket packet = packetGenerator.getArmAnimationPacket(1);
        PacketUtil.sendPacket(player, packet, true);
    }

    public void updatePosition() {
        CommonUtil.nextTick(new RotationFix(loc.getWorld(), this));
    }

    public void updatePosition(Player player) {
        CommonUtil.nextTick(new RotationFix(player, this));
    }

    public void showItems() {
        for (SlotType type : SlotType.values()) {
            CommonPacket packet = packetGenerator.getEquipmentChangePacket(inventory.getItem(type.getId()), type.getId());
            for (Player p : getLocation().getWorld().getPlayers()) {
                PacketUtil.sendPacket(p, packet, true);
            }
        }
    }

    private CommonPacket getSpawnPacket() {
        if(mob != null)
            return packetGenerator.getMobSpawnPacket();
        else
            return packetGenerator.getPlayerSpawnPacket();
    }

    private static class RotationFix implements Runnable {
        private PacketGenerator gen;
        private List<Player> players = new ArrayList<Player>();
        public RotationFix(Player player, NPC npc) {
            players.add(player);
            gen = npc.getPacketGenerator();
        }
        public RotationFix(World world, NPC npc) {
            players = world.getPlayers();
            gen = npc.getPacketGenerator();
        }
        @Override
        public void run() {
            CommonPacket bodyPacket = gen.getBodyRotationPacket();
            CommonPacket headPacket = gen.getHeadRotationPacket();
            for(Player player : players) {
                PacketUtil.sendPacket(player, bodyPacket);
                PacketUtil.sendPacket(player, headPacket);
            }
        }
    }
}
