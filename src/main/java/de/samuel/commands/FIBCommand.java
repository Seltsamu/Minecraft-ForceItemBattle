package de.samuel.commands;

import de.samuel.services.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

public class FIBCommand implements CommandExecutor {

    private final GameManager gameManager;

    public FIBCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Usage: /fib <start|stop>");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            gameManager.startGame();
        }

        if (args[0].equalsIgnoreCase("stop")) {
            gameManager.stopGame();
        }

        sender.sendMessage("Unknown subcommand");
        return true;
    }
}
