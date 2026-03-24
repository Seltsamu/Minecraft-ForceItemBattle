package de.samuel.services;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerProgress {
    private UUID playerID;
    public Material currentItem;
    public List<Material> completedItems = new ArrayList<>();

    public PlayerProgress(UUID playerID){
        this.playerID = playerID;
    }
}
