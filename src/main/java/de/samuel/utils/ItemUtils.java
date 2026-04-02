package de.samuel.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    private ItemUtils() {}

    public static boolean isTargetItem(ItemStack stack, Material target) {
        if (stack == null) return false;
        return stack.getType() == target;
    }
}
