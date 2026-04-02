package de.samuel.commands;

import de.samuel.services.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class FIBCommand implements CommandExecutor {

    private final GameManager gameManager;

    public FIBCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {

        if (args.length == 0) {
            sender.sendMessage("Usage: /fib <start|stop>");
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            int startTime = 3600; // Default start time for 1h

            if (args.length == 2) {
                try {
                    startTime = Integer.parseInt(args[1]) * 60; // Convert minutes to seconds
                } catch (NumberFormatException e) {
                    sender.sendMessage("Not a real number");
                    return true;
                }
                gameManager.startGame(startTime);
                return true;
            }

            gameManager.startGame(startTime);
            return true;
        }

        if (args[0].equalsIgnoreCase("stop")) {
            gameManager.stopGame();
            return true;
        }

        if (args[0].equalsIgnoreCase("skipItem")) {

            if (args.length != 2) {
                sender.sendMessage("Player-name missing");
                return true;
            }

            gameManager.skipItem((Player) sender);
            return true;
        }

        sender.sendMessage("Unknown subcommand");
        return true;
    }
}
