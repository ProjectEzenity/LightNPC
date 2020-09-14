package com.ezenity.lightnpc.util;

import com.bergerkiller.bukkit.common.controller.PlayerDataController;
import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;

import com.ezenity.lightnpc.Main;

/**
 * @version 0.0.1
 * @since 0.2.0
 */
public class PacketHandler {
    public PacketHandler(Main plugin) {
//        PacketUtil.addPacketListener(plugin, new PlayerPacketListener(plugin), PacketType.USE_ENTITY);
//        PacketUtil.addPacketListener(plugin, new PlayerDataController(), PacketType.IN_USE_ENTITY);
        PacketUtil.addPacketListener(plugin, new PacketListener() {
            @Override
            public void onPacketReceive(PacketReceiveEvent packetReceiveEvent) {
                // TODO
            }

            @Override
            public void onPacketSend(PacketSendEvent packetSendEvent) {
                //
            }
        }, PacketType.IN_USE_ENTITY);
    }
}
