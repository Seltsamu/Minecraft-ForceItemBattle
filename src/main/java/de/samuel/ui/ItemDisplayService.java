package de.samuel.ui;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemDisplayService {

    public void displayItem(Player player, Material item) {
        BossBar bossBar = BossBar.bossBar(
                Component.text(item.name())
                        .color(NamedTextColor.DARK_PURPLE)
                        .decorate(TextDecoration.BOLD),
                1.0f,
                BossBar.Color.WHITE,
                BossBar.Overlay.PROGRESS
        );

        removeItemBossBar(player);
        player.showBossBar(bossBar);
    }

    public void removeItemBossBar(Player player) {
        player.activeBossBars().forEach(player::hideBossBar);
    }
}
