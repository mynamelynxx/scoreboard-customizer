package com.example.scoreboardcustomizer.mixin;

import com.example.scoreboardcustomizer.ScoreboardCustomizer;
import com.example.scoreboardcustomizer.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class ScoreboardMixin {
    @Shadow @Final private MinecraftClient client;
    
    @Shadow protected abstract void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective);
    
    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void onRenderScoreboard(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        ModConfig config = ScoreboardCustomizer.getConfig();
        
        if (!config.enabled || objective == null) {
            return;
        }
        
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<ScoreboardPlayerScore> scores = scoreboard.getAllPlayerScores(objective);
        
        List<String> customLines = new ArrayList<>();
        
        // Добавляем кастомные строки
        customLines.add("§l" + config.customNickname);
        customLines.add(config.rankColor + "§l" + config.rank);
        customLines.add("§6Монет: §f" + config.coins);
        customLines.add("§dТокенов: §f" + config.tokens);
        customLines.add("§5Черепков: §f" + config.shards);
        customLines.add("");
        customLines.add("§4§lСтатистика");
        customLines.add("§aУбийств: §f" + config.kills + " §c✖");
        customLines.add("§cСмертей: §f" + config.deaths + " §c✖");
        customLines.add("§eНаиграно: §f" + config.playtime);
        customLines.add("");
        
        // Ищем строку с режимом в оригинальном scoreboard
        String gameMode = "";
        for (ScoreboardPlayerScore score : scores) {
            String playerName = score.getPlayerName();
            Team team = scoreboard.getPlayerTeam(playerName);
            String line = Team.decorateName(team, Text.literal(playerName)).getString();
            
            // Ищем строку с режимом (например "Анархия - 505")
            if (line.contains("Анархия") || line.contains("Режим")) {
                gameMode = line;
                break;
            }
        }
        
        if (!gameMode.isEmpty()) {
            customLines.add(gameMode);
        }
        
        // Рендерим кастомный scoreboard
        renderCustomScoreboard(context, objective, customLines);
        
        ci.cancel();
    }
    
    private void renderCustomScoreboard(DrawContext context, ScoreboardObjective objective, List<String> lines) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        
        int maxWidth = 0;
        for (String line : lines) {
            int width = this.client.textRenderer.getWidth(line);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        
        int boardWidth = maxWidth + 4;
        int boardHeight = lines.size() * 11;
        int x = screenWidth - boardWidth - 3;
        int y = screenHeight / 2 - boardHeight / 2;
        
        // Фон
        for (int i = 0; i < lines.size(); i++) {
            int lineY = y + i * 11;
            context.fill(x - 2, lineY, x + boardWidth, lineY + 11, 0x50000000);
        }
        
        // Текст
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineY = y + i * 11 + 2;
            context.drawTextWithShadow(this.client.textRenderer, line, x, lineY, 0xFFFFFF);
        }
    }
}
