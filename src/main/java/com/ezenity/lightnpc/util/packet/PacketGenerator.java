package com.ezenity.lightnpc.util.packet;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.ezenity.lightnpc.util.npcFx.MobType;
import com.ezenity.lightnpc.util.npcFx.NPC;
import net.minecraft.server.v1_16_R2.PacketPlayInAbilities;
import net.minecraft.server.v1_16_R2.PacketPlayInUseEntity;
import net.minecraft.server.v1_16_R2.PlayerInteractManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * @version 0.0.1
 * @since 0.2.0
 */
public class PacketGenerator {
    private NPC npc;

    public PacketGenerator(NPC npc) {
        this.npc = npc;
    }

    public CommonPacket getMobSpawnPacket() {
        Location loc = npc.getLocation();
        int EntityId = npc.getEntityId();
        EntityType type = MobType.toEntityType(npc.getMob());
        int typeId = type.getTypeId();
        CommonPacket packet = new CommonPacket(PacketType.MOB_SPAWN);
        int x = MathUtil.floor(loc.getX() * 32D);
        int y = MathUtil.floor(loc.getY() * 32D);
        int z = MathUtil.floor(loc.getZ() * 32D);
        byte yaw = this.getByteFromDegree(loc.getYaw());
        byte pitch = this.getByteFromDegree(loc.getPitch());
        packet.write(PacketFields.MOB_SPAWN.entityId, EntityId);
        packet.write(PacketFields.MOB_SPAWN.entityType, typeId);
        packet.write(PacketFields.MOB_SPAWN.x, x);
        packet.write(PacketFields.MOB_SPAWN.y, y);
        packet.write(PacketFields.MOB_SPAWN.z, z);
        packet.write(PacketFields.MOB_SPAWN.motX, 0);
        packet.write(PacketFields.MOB_SPAWN.motY, 0);
        packet.write(PacketFields.MOB_SPAWN.motZ, 0);
        packet.write(PacketFields.MOB_SPAWN.yaw, yaw);
        packet.write(PacketFields.MOB_SPAWN.pitch, pitch);
        packet.write(PacketFields.MOB_SPAWN.headYaw, yaw);
        packet.setDatawatcher(npc.getDataWatcher());
        return packet;
    }

    public CommonPacket getPlayerSpawnPacket() {
        Location loc = npc.getLocation();
        int EntityId = npc.getEntityId();
        String name = Pl3xNPC.colorize(npc.getName());
        int itemInHand = 0;
        CommonPacket packet = new CommonPacket(PacketType.NAMED_ENTITY_SPAWN);
        int x = MathUtil.floor(loc.getX() * 32D);
        int y = MathUtil.floor(loc.getY() * 32D);
        int z = MathUtil.floor(loc.getZ() * 32D);
        byte yaw = this.getByteFromDegree(loc.getYaw());
        byte pitch = this.getByteFromDegree(loc.getPitch());
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.entityId, EntityId);
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.entityName, name);
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.x, x);
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.y, y);
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.z, z);
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.yaw, yaw);
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.pitch, pitch);
        packet.write(PacketFields.NAMED_ENTITY_SPAWN.heldItemId, itemInHand);
        packet.setDatawatcher(npc.getDataWatcher());
        return packet;
    }

    public CommonPacket getMetadataPacket() {
        int EntityId = npc.getEntityId();
        DataWatcher datawatcher = npc.getDataWatcher();
        CommonPacket packet = new CommonPacket(PacketType.ENTITY_METADATA);
        packet.write(PacketFields.ENTITY_METADATA.entityId, EntityId);
        packet.write(PacketFields.ENTITY_METADATA.watchedObjects, datawatcher.getAllWatched());
        return packet;
    }

    public CommonPacket getDespawnPacket() {
        int[] EntityId = new int[] {npc.getEntityId()};
        CommonPacket packet = new CommonPacket(PacketType.DESTROY_ENTITY);
        packet.write(PacketFields.DESTROY_ENTITY.entityIds, EntityId);
        return packet;
    }

    public CommonPacket getBodyRotationPacket() {
        int EntityId = npc.getEntityId();
        Float y = npc.getYaw();
        Float p = npc.getPitch();
        if (y == null)
            y = 0F;
        if (p == null)
            p = 0F;
        byte yaw = this.getByteFromDegree(y);
        byte pitch = this.getByteFromDegree(p);
        CommonPacket packet = new CommonPacket(PacketType.ENTITY_LOOK);
        packet.write(PacketFields.ENTITY_LOOK.entityId, EntityId);
        packet.write(PacketFields.ENTITY_LOOK.dyaw, yaw);
        packet.write(PacketFields.ENTITY_LOOK.dpitch, pitch);
        return packet;
    }

    public CommonPacket getHeadRotationPacket() {
        int EntityId = npc.getEntityId();
        Float y = npc.getHeadYaw();
        if (y == null)
            y = 0F;
        byte yaw = this.getByteFromDegree(y);
        CommonPacket packet = new CommonPacket(PacketType.ENTITY_HEAD_ROTATION);
        packet.write(PacketFields.ENTITY_HEAD_ROTATION.entityId, EntityId);
        packet.write(PacketFields.ENTITY_HEAD_ROTATION.headYaw, yaw);
        return packet;
    }

    public CommonPacket getEquipmentChangePacket(ItemStack item, Integer slot) {
        int EntityId = npc.getEntityId();
        CommonPacket packet = new CommonPacket(PacketType.ENTITY_EQUIPMENT);
        packet.write(PacketFields.ENTITY_EQUIPMENT.entityId, EntityId);
        packet.write(PacketFields.ENTITY_EQUIPMENT.item, item);
        packet.write(PacketFields.ENTITY_EQUIPMENT.slot, slot);
        return packet;
    }

    public CommonPacket getArmAnimationPacket(Integer animation) {
        int EntityId = npc.getEntityId();
        CommonPacket packet = new CommonPacket(PacketType.ARM_ANIMATION);
        packet.write(PacketFields.ARM_ANIMATION.entityId, EntityId);
        packet.write(PacketFields.ARM_ANIMATION.animation, animation);
        return packet;
    }

    public byte getByteFromDegree(float degree) {
        return (byte) (int)(degree * 256.0F / 360.0F);
    }
}
