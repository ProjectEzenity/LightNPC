package com.ezenity.lightnpc.util.nms;

import java.io.IOException;
import java.lang.reflect.Field;
import net.minecraft.server.v1_5_R2.Connection;
import net.minecraft.server.v1_5_R2.ConsoleLogManager;
import net.minecraft.server.v1_5_R2.NetworkManager;
import net.minecraft.server.v1_5_R2.Packet;

/**
 *
 * @author martin
 * @version 0.0.1
 */
public class NPCNetworkManager extends NetworkManager {
    public NPCNetworkManager() throws IOException {
        // (IConsoleLogManager iconsolelogmanager, Socket socket, String s, Connection connection, PrivateKey privatekey)
        // socket = new NullSocket();  s = "NPC Manager";  connection = new Connection()
        super(new ConsoleLogManager("Minecraft-Server", (String) null, (String) null),new NullSocket(), "NPC Manager", new Connection() {
            @Override
            public boolean a() {
                return true;
            }
        }, null);
        try {
            Field f = NetworkManager.class.getDeclaredField("m");
            f.setAccessible(true);
            f.set(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void a(Connection nethandler) {
    }

    @Override
    public void queue(Packet packet) {
    }

    @Override
    public void a(String s, Object... aobject) {
    }

    @Override
    public void a() {
    }
}