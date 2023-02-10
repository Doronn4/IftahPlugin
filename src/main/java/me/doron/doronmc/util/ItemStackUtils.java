package me.doron.doronmc.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtils {

    public static boolean isBoots(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_BOOTS:
            case CHAINMAIL_BOOTS:
            case IRON_BOOTS:
            case GOLDEN_BOOTS:
            case DIAMOND_BOOTS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isHelmet(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_HELMET:
            case CHAINMAIL_HELMET:
            case IRON_HELMET:
            case GOLDEN_HELMET:
            case DIAMOND_HELMET:
                return true;
            default:
                return false;
        }
    }

    public static boolean isChestplate(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
            case IRON_CHESTPLATE:
            case GOLDEN_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isLeggings(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case IRON_LEGGINGS:
            case GOLDEN_LEGGINGS:
            case DIAMOND_LEGGINGS:
                return true;
            default:
                return false;
        }
    }
}

