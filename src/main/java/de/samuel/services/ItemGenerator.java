package de.samuel.services;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemGenerator {

    Random random = new Random();
    private final List<Material> possibleItems = Arrays.stream(Material.values())
            .filter(Material::isItem)
            .filter(m -> m != Material.AIR)
            .filter(m -> m.getMaxStackSize() > 0)
            .filter(m -> m != Material.STRUCTURE_BLOCK)
            .filter(m -> m != Material.COMMAND_BLOCK)
            .filter(m -> m != Material.COMMAND_BLOCK_MINECART)
            .filter(m -> m != Material.REPEATING_COMMAND_BLOCK)
            .filter(m -> m != Material.CHAIN_COMMAND_BLOCK)
            .filter(m -> m != Material.STRUCTURE_VOID)
            .filter(m -> m != Material.VOID_AIR)
            .filter(m -> m != Material.ENCHANTED_BOOK)
            .filter(m -> m != Material.JIGSAW)
            .filter(m -> m != Material.KNOWLEDGE_BOOK)
            .filter(m -> m != Material.PETRIFIED_OAK_SLAB)
            .filter(m -> m != Material.PLAYER_HEAD)
            .filter(m -> m != Material.LIGHT)
            .filter(m -> m != Material.BARRIER)
            .filter(m -> m != Material.DEBUG_STICK)
            .toList();

    public Material generateItem(List<Material> excludes) {
        Material randomItem = possibleItems.get(random.nextInt(possibleItems.size()));

        if (excludes.contains(randomItem)) {
            randomItem = generateItem(excludes);
        }

        return randomItem;
    }
}
