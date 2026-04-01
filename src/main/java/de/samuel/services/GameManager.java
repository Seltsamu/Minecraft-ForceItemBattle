package de.samuel.services;

import de.samuel.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

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
        Bukkit.broadcastMessage("Force Item Battle started");
    }

    public void stopGame() {
        gameIsRunning = false;
        Bukkit.broadcastMessage("Force Item Battle ended");
    }

    public void handlePlayerJoin(@NonNull Player player) {
        UUID playerID = player.getUniqueId();
        if (!progressMap.containsKey(playerID)) {
            PlayerProgress newProgress = new PlayerProgress(playerID);
            progressMap.put(playerID, newProgress);

            if (gameIsRunning) {
                assignNewItem(newProgress);
            }
        }
    }

    public void handleItemPickup(@NonNull ItemStack itemStack, @NonNull Player player) {
        PlayerProgress progress = getPlayerProgress(player);
        if (gameIsRunning && ItemUtils.isTargetItem(itemStack, progress.currentItem)) {
            playerFoundItem(progress);
        }
    }

    void playerFoundItem(@NonNull PlayerProgress progress) {
        progress.completedItems.add(progress.currentItem);
        logger.info("player: " + progress.getPlayerID() + " found his current item");
        assignNewItem(progress);
    }

    void assignNewItem(@NonNull PlayerProgress progress) {
        progress.currentItem = itemGenerator.generateItem(progress.completedItems);
        logger.info("assigned new item: " + progress.currentItem.name() + " to: " + progress.getPlayerID());
    }

    PlayerProgress getPlayerProgress(@NonNull Player player) {
        UUID playerID = player.getUniqueId();
        if (!progressMap.containsKey(playerID)) {
            logger.info("UUID not found in progressMap");
            return null;
        }

        return progressMap.get(playerID);
    }
}
