package de.samuel.listeners;

import de.samuel.services.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jspecify.annotations.NonNull;

public class PlayerJoinListener implements Listener {

    private GameManager gm;

    public PlayerJoinListener(GameManager gm) {
        this.gm = gm;
    }

    @EventHandler
    public void onJoin(@NonNull PlayerJoinEvent event) {
        gm.handlePlayerJoin(event.getPlayer());
    }
}
