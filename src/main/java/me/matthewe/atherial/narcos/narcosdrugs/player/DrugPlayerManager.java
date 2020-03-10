package me.matthewe.atherial.narcos.narcosdrugs.player;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import net.atherial.api.storage.PlayerYamlManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

/**
 * Created by Matthew E on 6/10/2019 at 8:11 PM for the project NarcosDrugs
 */
public class DrugPlayerManager extends PlayerYamlManager<NarcosDrugs, DrugPlayer> {

    public DrugPlayerManager(NarcosDrugs plugin) {
        super(plugin, "drug_players.yml", true);
    }

    @Override
    public DrugPlayer getTemplateData(UUID uuid) {
        return new DrugPlayer(uuid, null, LevelHandler.get().getLevelSettings().getStartingLevel(), LevelHandler.get().getLevelSettings().getStartingExperience());
    }

    @Override
    public ConfigurationSection saveToFile(DrugPlayer drugPlayer, String s, ConfigurationSection configurationSection) {
        configurationSection.set("level", drugPlayer.getLevel());
        configurationSection.set("experience", drugPlayer.getExperience());
        return configurationSection;
    }

    @Override
    public DrugPlayer loadFromFile(UUID uuid, String s, ConfigurationSection configurationSection) {
        return new DrugPlayer(uuid, null, configurationSection.getInt("level", LevelHandler.get().getLevelSettings().getStartingLevel()), configurationSection.getLong("experience",(long)LevelHandler.get().getLevelSettings().getStartingExperience()));
    }

}
