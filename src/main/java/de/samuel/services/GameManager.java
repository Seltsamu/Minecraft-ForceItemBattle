package de.samuel.services;

import de.samuel.util.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class GameManager {
    private final Map<UUID, PlayerProgress> progressMap = new HashMap<>();
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private final Logger logger;
    private boolean gameIsRunning = false;


    public GameManager(Logger logger) {
        this.logger = logger;
    }

    public void startGame() {
        progressMap.forEach((UUID u, PlayerProgress p) -> assignNewItem(p));
        gameIsRunning = true;
    }

    public void handlePlayerJoin(Player player) {
        UUID playerID = player.getUniqueId();
        if (!progressMap.containsKey(playerID)) {
            progressMap.put(playerID, new PlayerProgress(playerID));
        }
    }

    public void handleItemPickup(@NotNull ItemStack itemStack, @NotNull Player player) {
        PlayerProgress progress = getPlayerProgress(player);
        if (gameIsRunning && ItemUtils.isTargetItem(itemStack, progress.currentItem)) {
            playerFoundItem(progress);
        }
    }

    void playerFoundItem(PlayerProgress progress) {
        // TODO hier gehts weiter
    }

    void assignNewItem(PlayerProgress progress) {
        progress.currentItem = itemGenerator.generateItem(progress.completedItems);
    }

    PlayerProgress getPlayerProgress(Player player) {
        UUID playerID = player.getUniqueId();
        if (!progressMap.containsKey(playerID)) {
            logger.info("UUID not found in progressMap");
            return null;
        }

        return progressMap.get(playerID);
    }
}
