package com.example.scoreboardcustomizer;

import com.example.scoreboardcustomizer.config.ModConfig;
import com.example.scoreboardcustomizer.gui.CustomScoreboardScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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
        LOGGER.info("=== Scoreboard Customizer загружается ===");
        
        // Загрузка конфига
        CONFIG.load();
        LOGGER.info("Конфиг загружен");
        
        // Регистрация кнопки
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.scoreboardcustomizer.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.scoreboardcustomizer.general"
        ));
        LOGGER.info("Клавиша зарегистрирована: Right Shift");
        
        // Обработка нажатий клавиш
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                LOGGER.info("Клавиша нажата, открываем GUI");
                if (client.currentScreen == null) {
                    client.setScreen(new CustomScoreboardScreen());
                }
            }
        });
        
        LOGGER.info("=== Scoreboard Customizer загружен успешно! ===");
    }
    
    public static ModConfig getConfig() {
        return CONFIG;
    }
}
