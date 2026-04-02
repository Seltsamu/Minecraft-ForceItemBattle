package de.samuel.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MessageService {

    public void sendStartMessage() {
        TextComponent text = Component.text("The Force Item Battle has started")
                .color(NamedTextColor.DARK_GREEN)
                .decorate(TextDecoration.BOLD);
        Bukkit.broadcast(text);
    }

    public void sendEndMessage() {
        TextComponent text = Component.text("The Force Item Battle has ended")
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD);
        Bukkit.broadcast(text);
    }

    public void sendItemFoundMessage(Player player ,Material item) {
        TextComponent text = Component.text("You have successfully found ").color(NamedTextColor.GREEN)
                .append(Component.text(item.name()).color(NamedTextColor.GOLD))
                .append(Component.text("!").color(NamedTextColor.GREEN));
        player.sendMessage(text);
    }
}
