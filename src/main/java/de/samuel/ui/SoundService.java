package de.samuel.ui;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundService {

        public void playItemFoundSound(Player player) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
        }
}
