package com.ezenity.heavynpc.util.nms;

//import net.minecraft.server.v1_16_R2.Connection; // 1_5_R1
import net.minecraft.server.v1_16_R2.PlayerConnection;
import net.minecraft.server.v1_16_R2.ConsoleLogManager;
import net.minecraft.server.v1_16_R2.NetHandler;
import net.minecraft.server.v1_16_R2.NetworkManager;
import net.minecraft.server.v1_16_R2.Packet;


import java.io.IOException;
import java.lang.reflect.Field;

/**
 *
 * @author martin
 * @version 0.1.0
 * @since 0.0.1
 */
public class NPCNetworkManager extends NetworkManager /* NetworkManagerServer */ {
    public NPCNetworkManager() throws IOException {
        // (IConsoleLogManager iconsolelogmanager, Socket socket, String s, Connection connection, PrivateKey privatekey)
        // socket = new NullSocket();  s = "NPC Manager";  connection = new Connection()
//        super(new ConsoleLogManager("Minecraft-Server", (String) null, (String) null),new NullSocket(), "NPC Manager", new Connection() { // 1_5_R1
        super(new NullSocket(), "NPC Manager", new PlayerConnection() {
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
    public void a() {    }

    @Override
    public void sendPacket(Packet<?> packet) {    }

    @Override
    protected void b() {    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }
}