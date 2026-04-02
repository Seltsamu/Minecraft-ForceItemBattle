package de.samuel.services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
                TextComponent time = Component.text(formattedTime)
                        .color(NamedTextColor.BLUE)
                        .decorate(TextDecoration.BOLD);

                Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(time));

                currentTime--;
            }
        };

        timerTask.runTaskTimer(plugin, 0L, 20L); // 20 Ticks = 1 second
    }

    public void stop() {
        if (timerTask != null) {
            timerTask.cancel();

            TextComponent text = Component.text("Game ended")
                    .color(NamedTextColor.BLUE)
                    .decorate(TextDecoration.BOLD);
            Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(text));

            currentTime = 0;
            timerTask = null;
        }
    }
}
