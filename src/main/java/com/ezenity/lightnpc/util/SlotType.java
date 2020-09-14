package com.ezenity.lightnpc.util;

/**
 * @version 0.0.1
 * @since 0.2.0
 */
public enum SlotType {
    HELMET(4),
    CHESTPLATE(3),
    LEGGINGS(2),
    BOOTS(1),
    IN_HAND(0);

    private int id;

    SlotType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
