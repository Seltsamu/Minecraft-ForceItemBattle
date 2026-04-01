package de.samuel;

import de.samuel.commands.FIBCommand;
import de.samuel.listeners.ItemPickupListener;
import de.samuel.listeners.PlayerJoinListener;
import de.samuel.services.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class ForceItemPlugin extends JavaPlugin {

    private final Logger logger = getLogger();
    private final GameManager gameManager = new GameManager(logger, this);

    @Override
    public void onEnable() {
        logger.info("Plugin activated");

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new ItemPickupListener(gameManager), this);

        Objects.requireNonNull(getCommand("fib")).setExecutor(new FIBCommand(gameManager));
    }

    @Override
    public void onDisable() {
    }
}
