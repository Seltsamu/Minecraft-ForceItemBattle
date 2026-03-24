package de.samuel.listeners;

import de.samuel.services.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.jspecify.annotations.NonNull;

public class ItemPickupListener implements Listener {

    private GameManager gm;

    public ItemPickupListener(GameManager gm) {
        this.gm = gm;
    }

    @EventHandler
    public void OnItemPickup(@NonNull PlayerPickupItemEvent event) {
        gm.handleItemPickup(event.getItem().getItemStack(), event.getPlayer());
    }
}
