package me.matthewe.atherial.narcos.narcosdrugs.player;

import me.matthewe.atherial.narcos.narcosdrugs.level.Level;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import net.atherial.api.storage.AtherialPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Matthew E on 6/10/2019 at 8:10 PM for the project NarcosDrugs
 */
public class DrugPlayer extends AtherialPlayer<DrugPlayer> {
    private String viewedDrugPack;
    private int level;
    private long experience;

    public DrugPlayer(UUID uuid, String viewedDrugPack, int level, long experience) {
        super(uuid);
        this.viewedDrugPack = viewedDrugPack;
        this.level = level;
        this.experience = experience;
    }

    public void addExperience(Player player, int amount) {
        LevelHandler levelHandler = LevelHandler.get();
        if (this.level == levelHandler.getLevelSettings().getMaxLevel()) {
            return;
        }
        this.experience += amount;
        if (this.experience >= levelHandler.getLevel(this.level + 1).getNeededExperience()) {
            this.experience = 0;
            this.level++;
            Level level = levelHandler.getLevel(this.level);
            Level.OnLevelUp onLevelUp = level.getOnLevelUp();
            if (onLevelUp != null) {
                if (onLevelUp.effects != null) {
                    if (onLevelUp.effects.fireworks != null && onLevelUp.effects.fireworks.enabled) {
                        onLevelUp.effects.fireworks.detonate(player.getEyeLocation());
                    }
                    if (onLevelUp.effects.sound != null && onLevelUp.effects.sound.enabled) {
                        onLevelUp.effects.sound.play(player);
                    }
                }
                if (onLevelUp.broadcast != null) {
                    onLevelUp.sendBroadcast(player, this.level);
                }
                if ((onLevelUp.commands != null) && !onLevelUp.commands.isEmpty()) {
                    onLevelUp.runCommands(player, this.level);
                }
                if (onLevelUp.message != null && onLevelUp.message.enabled) {
                    if (onLevelUp.message.type == Level.OnLevelUp.Type.MESSAGE && onLevelUp.message.message != null) {
                        onLevelUp.message.send(player, this.level);
                    } else if (onLevelUp.message.type == Level.OnLevelUp.Type.TITLE && onLevelUp.message.title != null) {
                        onLevelUp.message.send(player, this.level);
                    }
                }
            }
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public long getExperience() {
        return experience;
    }

    public static DrugPlayer get(Player player) {
        return PlayerHandler.get().getPlayerManager().getPlayer(player);
    }

    public void setViewedDrugPack(String viewedDrugPack) {
        this.viewedDrugPack = viewedDrugPack;
    }

    public boolean isViewingDrugPack() {
        return viewedDrugPack != null;
    }

    public String getViewedDrugPack() {
        return viewedDrugPack;
    }

    @Override
    public Class<DrugPlayer> getValueClass() {
        return DrugPlayer.class;
    }

}
