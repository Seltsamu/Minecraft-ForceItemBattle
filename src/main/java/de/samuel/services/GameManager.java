package de.samuel.services;

import de.samuel.ui.ItemDisplayService;
import de.samuel.ui.MessageService;
import de.samuel.ui.SoundService;
import de.samuel.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.logging.Logger;

public class GameManager {

    private final Map<UUID, PlayerProgress> progressMap = new HashMap<>();
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private final MessageService messageService = new MessageService();
    private final SoundService soundService = new SoundService();
    private final ItemDisplayService itemDisplayService = new ItemDisplayService();
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
        messageService.sendStartMessage();

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        players.forEach(this::AddPlayerToProgressMap);

        progressMap.forEach((UUID u, PlayerProgress p) -> {
            p.completedItems = new ArrayList<>();
            assignNewItem(p);

            Player player = getPlayerToProgress(p);
            addItemToPlayerInventory(player, createSkipItem());
        });

        timer.start(startTime);

        gameIsRunning = true;
    }

    public void stopGame() {
        timer.stop();

        progressMap.forEach((UUID u, PlayerProgress p) ->
                itemDisplayService.removeItemBossBar(getPlayerToProgress(p)));

        gameIsRunning = false;
        messageService.sendEndMessage();
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
        PlayerProgress progress = getProgressToPlayer(player);
        if (gameIsRunning && ItemUtils.isTargetItem(itemStack, progress.currentItem)) {
            playerFoundItem(progress);
        }
    }

    public void skipItem(@NonNull Player player) {
        if (!gameIsRunning)
            return;

        PlayerProgress progress = getProgressToPlayer(player);
        ItemStack skippedItem = new ItemStack(progress.currentItem, 1);

        logger.info("[ " + player.getName() + "]: skipped item: " + skippedItem.getType().name());

        playerFoundItem(progress);

        addItemToPlayerInventory(player, skippedItem);
    }

    void playerFoundItem(@NonNull PlayerProgress progress) {
        progress.completedItems.add(progress.currentItem);

        Player player = getPlayerToProgress(progress);
        messageService.sendItemFoundMessage(player, progress.currentItem);

        soundService.playItemFoundSound(player);

        String playerName = player.getName();
        logger.info("[" + playerName + "]: found his current item");

        assignNewItem(progress);
    }

    void assignNewItem(@NonNull PlayerProgress progress) {
        progress.currentItem = itemGenerator.generateItem(progress.completedItems);

        Player player = getPlayerToProgress(progress);
        Material currentItem = progress.currentItem;
        itemDisplayService.displayItem(player, currentItem);

        logger.info("[" + player.getName() + "]: assigned new item: " + currentItem.name());
    }

    PlayerProgress getProgressToPlayer(@NonNull Player player) {
        UUID playerID = player.getUniqueId();
        if (!progressMap.containsKey(playerID)) {
            logger.info("UUID not found in progressMap");
            return null;
        }

        return progressMap.get(playerID);
    }

    Player getPlayerToProgress(@NonNull PlayerProgress progress) {
        return Objects.requireNonNull(Bukkit.getPlayer(progress.getPlayerID()));
    }

    ItemStack createSkipItem() {
        ItemStack skipItem = new ItemStack(Material.BARRIER, 5);
        ItemMeta itemMeta = skipItem.getItemMeta();

        TextComponent skipItemName = Component.text("Skip")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD);
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
