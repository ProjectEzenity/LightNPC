package com.ezenity.lightnpc.util.nms;

import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import net.minecraft.server.v1_16_R2.NetworkManager;
import net.minecraft.server.v1_16_R2.PacketPlayInAbilities;
import net.minecraft.server.v1_16_R2.PacketPlayInAdvancements;
import net.minecraft.server.v1_16_R2.PacketPlayInAutoRecipe;
import net.minecraft.server.v1_16_R2.PacketPlayInBEdit;
import net.minecraft.server.v1_16_R2.PacketPlayInBeacon;
import net.minecraft.server.v1_16_R2.PacketPlayInBoatMove;
import net.minecraft.server.v1_16_R2.PacketPlayInClientCommand;
import net.minecraft.server.v1_16_R2.PacketPlayInCustomPayload;
import net.minecraft.server.v1_16_R2.PacketPlayInDifficultyChange;
import net.minecraft.server.v1_16_R2.PacketPlayInDifficultyLock;
import net.minecraft.server.v1_16_R2.PacketPlayInEnchantItem;
import net.minecraft.server.v1_16_R2.PacketPlayInEntityNBTQuery;
import net.minecraft.server.v1_16_R2.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_16_R2.PacketPlayInItemName;
import net.minecraft.server.v1_16_R2.PacketPlayInJigsawGenerate;
import net.minecraft.server.v1_16_R2.PacketPlayInKeepAlive;
import net.minecraft.server.v1_16_R2.PacketPlayInPickItem;
import net.minecraft.server.v1_16_R2.PacketPlayInRecipeDisplayed;
import net.minecraft.server.v1_16_R2.PacketPlayInRecipeSettings;
import net.minecraft.server.v1_16_R2.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_16_R2.PacketPlayInSetCommandBlock;
import net.minecraft.server.v1_16_R2.PacketPlayInSetCommandMinecart;
import net.minecraft.server.v1_16_R2.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_16_R2.PacketPlayInSetJigsaw;
import net.minecraft.server.v1_16_R2.PacketPlayInSettings;
import net.minecraft.server.v1_16_R2.PacketPlayInSpectate;
import net.minecraft.server.v1_16_R2.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_16_R2.PacketPlayInStruct;
import net.minecraft.server.v1_16_R2.PacketPlayInTabComplete;
import net.minecraft.server.v1_16_R2.PacketPlayInTeleportAccept;
import net.minecraft.server.v1_16_R2.PacketPlayInTileNBTQuery;
import net.minecraft.server.v1_16_R2.PacketPlayInTrSel;
import net.minecraft.server.v1_16_R2.PacketPlayInUseItem;
import net.minecraft.server.v1_16_R2.PacketPlayInVehicleMove;
import net.minecraft.server.v1_16_R2.PacketPlayOutPosition;
import net.minecraft.server.v1_16_R2.PlayerConnection;
import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PacketPlayInCloseWindow;
import net.minecraft.server.v1_16_R2.PacketPlayInWindowClick;
import net.minecraft.server.v1_16_R2.PacketPlayInTransaction;
import net.minecraft.server.v1_16_R2.PacketPlayInFlying;
import net.minecraft.server.v1_16_R2.PacketPlayInUpdateSign;
import net.minecraft.server.v1_16_R2.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R2.PacketPlayInBlockPlace;
import net.minecraft.server.v1_16_R2.PacketPlayInArmAnimation;
import net.minecraft.server.v1_16_R2.PacketPlayInEntityAction;
import net.minecraft.server.v1_16_R2.PacketPlayInChat;
import net.minecraft.server.v1_16_R2.PacketPlayInUseEntity;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Set;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;

import com.ezenity.lightnpc.util.NPCManager;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author martin
 * @version 0.1.0
 *  - Edited by Ezenity
 * @since 0.0.1
 */
public class NPCNetHandler extends PlayerConnection {

    public NPCNetHandler(NPCManager npcManager, EntityPlayer entityplayer) {
        super(npcManager.getServer().getMCServer(), npcManager.getNPCNetworkManager(),
                entityplayer);
    }

    public NPCNetHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
    }

    @Override
    public CraftPlayer getPlayer() {
        return new CraftPlayer((CraftServer) Bukkit.getServer(), player); // Fake player prevents spout NPEs
    }

    @Override
    public void a(PacketPlayInFlying packetPlayInFlying) {    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1) {    }

    @Override
    public void a(PacketPlayInBlockDig packetPlayInBlockDig) {    }

    @Override
    public void a(PacketPlayInBlockPlace packetPlayInBlockPlace) {    }

    @Override
    public void a(PacketPlayInChat packetPlayInChat) {    }

    @Override
    public void a(PacketPlayInArmAnimation packetPlayInArmAnimation) {    }

    @Override
    public void a(PacketPlayInEntityAction playInEntityAction) {    }

    @Override
    public void sendPacket(Packet packet) {    }

    @Override
    public void a(PacketPlayInUseEntity packetPlayInUseEntity) {    }

    @Override
    public void a(PacketPlayInWindowClick packetPlayInWindowClick) {    }

    @Override
    public void a(PacketPlayInTransaction playInTransaction) {    }

    @Override
    public void a(PacketPlayInUpdateSign playInUpdateSign) {    }

    @Override
    public void tick() {    }

    @Override
    public void syncPosition() {    }

    @Override
    public NetworkManager a() {    }

    /**
     * @deprecated
     */
    @Override
    public void disconnect(IChatBaseComponent ichatbasecomponent) {    }

    @Override
    public void disconnect(String s) {    }

    @Override
    public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {    }

    @Override
    public void a(PacketPlayInVehicleMove packetplayinvehiclemove) {    }

    @Override
    public void a(PacketPlayInTeleportAccept packetplayinteleportaccept) {    }

    @Override
    public void a(PacketPlayInRecipeDisplayed packetplayinrecipedisplayed) {    }

    @Override
    public void a(PacketPlayInRecipeSettings packetplayinrecipesettings) {    }

    @Override
    public void a(PacketPlayInAdvancements packetplayinadvancements) {    }

    @Override
    public void a(PacketPlayInTabComplete packetplayintabcomplete) {    }

    @Override
    public void a(PacketPlayInSetCommandBlock packetplayinsetcommandblock) {   }

    @Override
    public void a(PacketPlayInSetCommandMinecart packetplayinsetcommandminecart) {    }

    @Override
    public void a(PacketPlayInPickItem packetplayinpickitem) {    }

    @Override
    public void a(PacketPlayInItemName packetplayinitemname) {    }

    @Override
    public void a(PacketPlayInBeacon packetplayinbeacon) {    }

    @Override
    public void a(PacketPlayInStruct packetplayinstruct) {    }

    @Override
    public void a(PacketPlayInSetJigsaw packetplayinsetjigsaw) {    }

    @Override
    public void a(PacketPlayInJigsawGenerate packetplayinjigsawgenerate) {    }

    @Override
    public void a(PacketPlayInTrSel packetplayintrsel) {    }

    @Override
    public void a(PacketPlayInBEdit packetplayinbedit) {    }

    @Override
    public void a(PacketPlayInEntityNBTQuery packetplayinentitynbtquery) {    }

    @Override
    public void a(PacketPlayInTileNBTQuery packetplayintilenbtquery) {    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1, PlayerTeleportEvent.TeleportCause cause) {    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set) {    }

    @Override
    public void a(double d0, double d1, double d2, float f, float f1, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set, PlayerTeleportEvent.TeleportCause cause) {    }

    @Override
    public void teleport(Location dest) {    }

    @Override
    public void a(PacketPlayInUseItem packetplayinuseitem) {    }

    @Override
    public void a(PacketPlayInSpectate packetplayinspectate) {    }

    @Override
    public void a(PacketPlayInResourcePackStatus packetplayinresourcepackstatus) {    }

    @Override
    public void a(PacketPlayInBoatMove packetplayinboatmove) {    }

    @Override
    public void a(IChatBaseComponent ichatbasecomponent) {    }

    @Override
    public void a(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> genericfuturelistener) {    }

    @Override
    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {    }

    @Override
    public void chat(String s, boolean async) {    }

    @Override
    public void a(PacketPlayInClientCommand packetplayinclientcommand) {    }

    @Override
    public void a(PacketPlayInCloseWindow packetplayinclosewindow) {    }

    @Override
    public void a(PacketPlayInAutoRecipe packetplayinautorecipe) {    }

    @Override
    public void a(PacketPlayInEnchantItem packetplayinenchantitem) {    }

    @Override
    public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {    }

    @Override
    public void a(PacketPlayInKeepAlive packetplayinkeepalive) {    }

    @Override
    public void a(PacketPlayInAbilities packetplayinabilities) {    }

    @Override
    public void a(PacketPlayInSettings packetplayinsettings) {    }

    @Override
    public void a(PacketPlayInCustomPayload packetplayincustompayload) {    }

    @Override
    public void a(PacketPlayInDifficultyChange packetplayindifficultychange) {    }

    @Override
    public void a(PacketPlayInDifficultyLock packetplayindifficultylock) {    }
}