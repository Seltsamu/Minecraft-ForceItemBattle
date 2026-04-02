package de.samuel.services;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class GameTimer {

    private final JavaPlugin plugin;
    private final GameManager gameManager;
    private BukkitRunnable timerTask;
    private int currentTime = 0;

    public GameTimer(JavaPlugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    public void start(int startTime) {

        if (timerTask != null) return;

        currentTime = startTime;

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {

                if (currentTime == 0) {
                    gameManager.stopGame();
                }

                int seconds = currentTime % 60;
                int minutes = (currentTime % 3600) / 60;
                int hours = currentTime / 3600;
                String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(formattedTime));

                currentTime--;
            }
        };

        timerTask.runTaskTimer(plugin, 0L, 20L); // 20 Ticks = 1 second
    }

    public void stop() {
        if (timerTask != null) {
            timerTask.cancel();
            Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar("Game ended"));
            currentTime = 0;
            timerTask = null;
        }
    }
}
