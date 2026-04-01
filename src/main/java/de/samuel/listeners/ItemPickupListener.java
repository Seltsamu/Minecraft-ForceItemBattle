package de.samuel.listeners;

import de.samuel.services.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.jspecify.annotations.NonNull;

public class ItemPickupListener implements Listener {

    private final GameManager gameManager;

    public ItemPickupListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void OnItemPickup(@NonNull PlayerPickupItemEvent event) {
        gameManager.handleItemPickup(event.getItem().getItemStack(), event.getPlayer());
    }
}
