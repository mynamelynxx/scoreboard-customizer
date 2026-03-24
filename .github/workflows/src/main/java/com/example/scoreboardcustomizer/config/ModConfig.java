package com.example.scoreboardcustomizer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("scoreboard-customizer.json");
    
    // Настройки
    public boolean enabled = true;
    public String customNickname = "yavklynxx";
    public String rank = "Игрок";
    public String rankColor = "§a"; // Зеленый по умолчанию
    
    public int coins = 0;
    public int tokens = 0;
    public int shards = 0;
    
    public int kills = 0;
    public int deaths = 0;
    public String playtime = "0ч";
    
    // Цвета рангов
    public static final String[] RANK_COLORS = {
        "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7",
        "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f"
    };
    
    public static final String[] RANK_COLOR_NAMES = {
        "Черный", "Темно-синий", "Темно-зеленый", "Темно-бирюзовый",
        "Темно-красный", "Темно-фиолетовый", "Золотой", "Серый",
        "Темно-серый", "Синий", "Зеленый", "Бирюзовый",
        "Красный", "Розовый", "Желтый", "Белый"
    };
    
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("Не удалось сохранить конфиг: " + e.getMessage());
        }
    }
    
    public void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = new FileReader(CONFIG_PATH.toFile())) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                if (loaded != null) {
                    this.enabled = loaded.enabled;
                    this.customNickname = loaded.customNickname;
                    this.rank = loaded.rank;
                    this.rankColor = loaded.rankColor;
                    this.coins = loaded.coins;
                    this.tokens = loaded.tokens;
                    this.shards = loaded.shards;
                    this.kills = loaded.kills;
                    this.deaths = loaded.deaths;
                    this.playtime = loaded.playtime;
                }
            } catch (IOException e) {
                System.err.println("Не удалось загрузить конфиг: " + e.getMessage());
            }
        } else {
            save();
        }
    }
}
