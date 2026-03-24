package com.example.scoreboardcustomizer.gui;

import com.example.scoreboardcustomizer.ScoreboardCustomizer;
import com.example.scoreboardcustomizer.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class CustomScoreboardScreen extends Screen {
    private final ModConfig config = ScoreboardCustomizer.getConfig();
    
    private TextFieldWidget nicknameField;
    private TextFieldWidget rankField;
    private TextFieldWidget coinsField;
    private TextFieldWidget tokensField;
    private TextFieldWidget shardsField;
    private TextFieldWidget killsField;
    private TextFieldWidget deathsField;
    private TextFieldWidget playtimeField;
    
    private ButtonWidget enabledButton;
    private ButtonWidget rankColorButton;
    private int currentColorIndex = 0;
    
    public CustomScoreboardScreen() {
        super(Text.literal("Настройка Scoreboard"));
        
        // Находим индекс текущего цвета
        for (int i = 0; i < ModConfig.RANK_COLORS.length; i++) {
            if (ModConfig.RANK_COLORS[i].equals(config.rankColor)) {
                currentColorIndex = i;
                break;
            }
        }
    }
    
    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 40;
        int fieldWidth = 200;
        int spacing = 25;
        
        // Кнопка включения/выключения
        enabledButton = ButtonWidget.builder(
            Text.literal("Мод: " + (config.enabled ? "§aВКЛ" : "§cВЫКЛ")),
            button -> {
                config.enabled = !config.enabled;
                button.setMessage(Text.literal("Мод: " + (config.enabled ? "§aВКЛ" : "§cВЫКЛ")));
            })
            .dimensions(centerX - 100, startY, 200, 20)
            .build();
        addDrawableChild(enabledButton);
        
        // Никнейм
        nicknameField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing, fieldWidth, 20, Text.literal("Nickname"));
        nicknameField.setMaxLength(16);
        nicknameField.setText(config.customNickname);
        addDrawableChild(nicknameField);
        
        // Ранг
        rankField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing * 2, fieldWidth, 20, Text.literal("Rank"));
        rankField.setMaxLength(32);
        rankField.setText(config.rank);
        addDrawableChild(rankField);
        
        // Цвет ранга
        rankColorButton = ButtonWidget.builder(
            Text.literal("Цвет: " + config.rankColor + ModConfig.RANK_COLOR_NAMES[currentColorIndex]),
            button -> {
                currentColorIndex = (currentColorIndex + 1) % ModConfig.RANK_COLORS.length;
                config.rankColor = ModConfig.RANK_COLORS[currentColorIndex];
                button.setMessage(Text.literal("Цвет: " + config.rankColor + ModConfig.RANK_COLOR_NAMES[currentColorIndex]));
            })
            .dimensions(centerX - 100, startY + spacing * 3, 200, 20)
            .build();
        addDrawableChild(rankColorButton);
        
        // Монеты
        coinsField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing * 4, fieldWidth, 20, Text.literal("Coins"));
        coinsField.setMaxLength(10);
        coinsField.setText(String.valueOf(config.coins));
        addDrawableChild(coinsField);
        
        // Токены
        tokensField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing * 5, fieldWidth, 20, Text.literal("Tokens"));
        tokensField.setMaxLength(10);
        tokensField.setText(String.valueOf(config.tokens));
        addDrawableChild(tokensField);
        
        // Черепки
        shardsField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing * 6, fieldWidth, 20, Text.literal("Shards"));
        shardsField.setMaxLength(10);
        shardsField.setText(String.valueOf(config.shards));
        addDrawableChild(shardsField);
        
        // Убийства
        killsField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing * 7, fieldWidth, 20, Text.literal("Kills"));
        killsField.setMaxLength(10);
        killsField.setText(String.valueOf(config.kills));
        addDrawableChild(killsField);
        
        // Смерти
        deathsField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing * 8, fieldWidth, 20, Text.literal("Deaths"));
        deathsField.setMaxLength(10);
        deathsField.setText(String.valueOf(config.deaths));
        addDrawableChild(deathsField);
        
        // Наиграно часов
        playtimeField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + spacing * 9, fieldWidth, 20, Text.literal("Playtime"));
        playtimeField.setMaxLength(10);
        playtimeField.setText(config.playtime);
        addDrawableChild(playtimeField);
        
        // Кнопка сохранения
        addDrawableChild(ButtonWidget.builder(Text.literal("Сохранить"), button -> {
            saveConfig();
            this.close();
        }).dimensions(centerX - 100, startY + spacing * 10, 95, 20).build());
        
        // Кнопка отмены
        addDrawableChild(ButtonWidget.builder(Text.literal("Отмена"), button -> {
            this.close();
        }).dimensions(centerX + 5, startY + spacing * 10, 95, 20).build());
    }
    
    private void saveConfig() {
        config.customNickname = nicknameField.getText();
        config.rank = rankField.getText();
        
        try {
            config.coins = Integer.parseInt(coinsField.getText());
        } catch (NumberFormatException ignored) {}
        
        try {
            config.tokens = Integer.parseInt(tokensField.getText());
        } catch (NumberFormatException ignored) {}
        
        try {
            config.shards = Integer.parseInt(shardsField.getText());
        } catch (NumberFormatException ignored) {}
        
        try {
            config.kills = Integer.parseInt(killsField.getText());
        } catch (NumberFormatException ignored) {}
        
        try {
            config.deaths = Integer.parseInt(deathsField.getText());
        } catch (NumberFormatException ignored) {}
        
        config.playtime = playtimeField.getText();
        config.save();
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        int centerX = this.width / 2;
        int startY = 40;
        int spacing = 25;
        
        // Заголовки полей
        context.drawTextWithShadow(this.textRenderer, "Никнейм:", centerX - 100, startY + spacing - 12, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Ранг:", centerX - 100, startY + spacing * 2 - 12, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Монеты:", centerX - 100, startY + spacing * 4 - 12, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Токены:", centerX - 100, startY + spacing * 5 - 12, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Черепки:", centerX - 100, startY + spacing * 6 - 12, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Убийства:", centerX - 100, startY + spacing * 7 - 12, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Смерти:", centerX - 100, startY + spacing * 8 - 12, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Наиграно:", centerX - 100, startY + spacing * 9 - 12, 0xFFFFFF);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
