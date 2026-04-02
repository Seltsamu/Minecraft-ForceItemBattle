package de.samuel.listeners;

import de.samuel.services.GameManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

public class SkipItemListener implements Listener {

    private final JavaPlugin plugin;
    private final GameManager gameManager;

    public SkipItemListener(JavaPlugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerInteract(@NonNull PlayerInteractEvent event) {
        if (event.getItem() == null || !gameManager.isGameRunning()) return;

        ItemStack item = event.getItem();

        if (item.getType() != Material.BARRIER || !item.hasItemMeta()) return;

        ItemMeta itemMeta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "skip_item");

        if (!itemMeta.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) return;

        // filter only for right click action
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        event.setUseItemInHand(Event.Result.DENY);
        event.setUseInteractedBlock(Event.Result.DENY);

        event.setCancelled(true);

        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
        gameManager.skipItem(player);

        item.setAmount(item.getAmount() - 1); // remove one skip item
    }
}
