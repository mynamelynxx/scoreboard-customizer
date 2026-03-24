package com.example.scoreboardcustomizer;

import com.example.scoreboardcustomizer.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScoreboardCustomizer implements ClientModInitializer {
    public static final String MOD_ID = "scoreboardcustomizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    public static ModConfig CONFIG = new ModConfig();
    
    private static KeyBinding openGuiKey;
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Scoreboard Customizer загружен!");
        
        // Загрузка конфига
        CONFIG.load();
        
        // Регистрация кнопки
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.scoreboardcustomizer.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.scoreboardcustomizer.general"
        ));
        
        // Обработка нажатий клавиш
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new com.example.scoreboardcustomizer.gui.CustomScoreboardScreen());
                }
            }
        });
    }
    
    public static ModConfig getConfig() {
        return CONFIG;
    }
}
