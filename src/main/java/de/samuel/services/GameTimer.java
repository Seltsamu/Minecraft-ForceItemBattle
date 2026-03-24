package de.samuel.services;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class GameTimer {

    private boolean isStoppedByCommand;
    private final JavaPlugin plugin;
    private BukkitRunnable timerTask;
    private int currentTime;

    public GameTimer(JavaPlugin plugin) {
        this.plugin = plugin;
        currentTime = plugin.getConfig().getInt("timeElapsed", 0);
        isStoppedByCommand = plugin.getConfig().getBoolean("timerIsStoppedByCommand");
    }

    public boolean isNotStoppedByCommand(){
        return !isStoppedByCommand;
    }

    public void setStoppedByCommand(boolean value){
        isStoppedByCommand = value;
        plugin.getConfig().set("timerIsStoppedByCommand", value);
        plugin.saveConfig();
    }

    public void setCurrentTime(int value){
        currentTime = value;
        plugin.getConfig().set("timeElapsed", value);
        plugin.saveConfig();
    }

    public void start() {

        if (timerTask != null) return;

        timerTask = new BukkitRunnable() {
            @Override
            public void run() {

                int seconds = currentTime % 60;
                int minutes = (currentTime % 3600) / 60;
                int hours = currentTime / 3600;
                String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(formattedTime));

                currentTime++;
            }
        };

        timerTask.runTaskTimer(plugin, 0L, 20L); // 20 Ticks = 1 second
    }

    public void stop() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
            saveCurrentTime();
        }
    }

    public void reset() {
        if (timerTask != null) {
            currentTime = 0;
            timerTask.cancel();
            timerTask = null;
            saveCurrentTime();
        }
    }

    private void saveCurrentTime() {
        plugin.getConfig().set("timeElapsed", currentTime);
        plugin.saveConfig();
        isStoppedByCommand = false;
    }
}
