package de.samuel.services;

import de.samuel.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.awt.*;
import java.util.*;
import java.util.logging.Logger;

public class GameManager {

    private final Map<UUID, PlayerProgress> progressMap = new HashMap<>();
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private final Logger logger;
    private boolean gameIsRunning = false;
    private final JavaPlugin plugin;
    private final GameTimer timer;

    public GameManager(Logger logger, JavaPlugin plugin) {
        this.logger = logger;
        this.plugin = plugin;
        timer = new GameTimer(plugin, this);
    }

    public boolean isGameRunning() {
        return gameIsRunning;
    }

    public void startGame(int startTime) {
        Bukkit.broadcastMessage("Force Item Battle started");

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        players.forEach(this::AddPlayerToProgressMap);

        progressMap.forEach((UUID u, PlayerProgress p) -> {
            p.completedItems = new ArrayList<>();
            assignNewItem(p);

            Player player = Objects.requireNonNull(Bukkit.getPlayer(u));
            addItemToPlayerInventory(player, createSkipItem());
        });

        timer.start(startTime);

        gameIsRunning = true;
    }

    public void stopGame() {
        timer.stop();
        gameIsRunning = false;
        Bukkit.broadcastMessage("Force Item Battle ended");
    }

    public void AddPlayerToProgressMap(@NonNull Player player) {
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

    public void skipItem(@NonNull Player player) {
        if (!gameIsRunning)
            return;

        PlayerProgress progress = getPlayerProgress(player);
        ItemStack skippedItem = new ItemStack(progress.currentItem, 1);

        logger.info("[ " + player.getName() + "]: skipped item: " + skippedItem.getType().name());

        playerFoundItem(progress);

        addItemToPlayerInventory(player, skippedItem);
    }

    void playerFoundItem(@NonNull PlayerProgress progress) {
        progress.completedItems.add(progress.currentItem);

        String playerName = Objects.requireNonNull(Bukkit.getPlayer(progress.getPlayerID())).getName();
        logger.info("[" + playerName + "]: found his current item");

        assignNewItem(progress);
    }

    void assignNewItem(@NonNull PlayerProgress progress) {
        progress.currentItem = itemGenerator.generateItem(progress.completedItems);

        String playerName = Objects.requireNonNull(Bukkit.getPlayer(progress.getPlayerID())).getName();
        logger.info("[" + playerName + "]: assigned new item: " + progress.currentItem.name());
    }

    PlayerProgress getPlayerProgress(@NonNull Player player) {
        UUID playerID = player.getUniqueId();
        if (!progressMap.containsKey(playerID)) {
            logger.info("UUID not found in progressMap");
            return null;
        }

        return progressMap.get(playerID);
    }

    ItemStack createSkipItem() {
        ItemStack skipItem = new ItemStack(Material.BARRIER, 5);
        ItemMeta itemMeta = skipItem.getItemMeta();

        TextComponent skipItemName = Component.text("Skip").color(NamedTextColor.RED);
        itemMeta.displayName(skipItemName);

        NamespacedKey key = new NamespacedKey(plugin, "skip_item");
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        skipItem.setItemMeta(itemMeta);

        return skipItem;
    }

    static void addItemToPlayerInventory(@NonNull Player player, ItemStack itemStack) {
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(itemStack);
        if (!leftover.isEmpty()) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }
    }
}
