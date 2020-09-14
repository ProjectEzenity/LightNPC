package com.ezenity.lightnpc.util;

import com.bergerkiller.mountiplex.reflection.FieldAccessor;
import org.bukkit.entity.Player;

import com.bergerkiller.mountiplex.reflection.ClassTemplate;
import com.bergerkiller.bukkit.common.conversion.Conversion;

/**
 * @version 0.0.1
 * @version 0.2.0
 */
public class ReflectionUtil {
    private static ClassTemplate<Object> TEMPLATE = new NMSClassTemplate("EntityPlayer");
    private static FieldAccessor<Float> yaw = TEMPLATE.getField("yaw");
    private static FieldAccessor<Float> pitch = TEMPLATE.getField("pitch");
    private static FieldAccessor<Float> headyaw = TEMPLATE.getField("aO");

    public static float getYaw(Player player) {
        Object e = Conversion.toEntityHandle.convert(player);
        return yaw.get(e);
    }

    public static float getPitch(Player player) {
        Object e = Conversion.toEntityHandle.convert(player);
        return pitch.get(e);
    }

    public static float getHeadYaw(Player player) {
        Object e = Conversion.toEntityHandle.convert(player);
        return headyaw.get(e);
    }
}
