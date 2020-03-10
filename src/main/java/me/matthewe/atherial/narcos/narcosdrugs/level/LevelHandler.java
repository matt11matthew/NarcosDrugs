package me.matthewe.atherial.narcos.narcosdrugs.level;

import com.google.gson.Gson;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.quality.QualityHandler;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.ConfigUtils;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.IntegerRange;
import net.atherial.api.config.BukkitConfig;
import net.atherial.api.plugin.utilities.GenericUtils;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 6/20/2019 at 10:33 PM for the project NarcosDrugs
 */
public class LevelHandler extends NarcosHandler {
    private LevelSettings levelSettings;
    private Map<Integer, Level> levelMap;

    public LevelHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    public static LevelHandler get() {
        return NarcosDrugs.getInstance().getHandler(LevelHandler.class);
    }

    public Map<Integer, Level> getLevelMap() {
        return levelMap;
    }

    @Override
    public void onEnable() {
        loadLevels();

    }

    private void loadLevels() {
        if (!new File(this.plugin.getDataFolder(), "levels.yml").exists()) {
            generateDefaultConfig();
        }
        this.levelMap = new ConcurrentHashMap<>();

        BukkitConfig bukkitConfig = new BukkitConfig("levels.yml", this.narcosDrugs);
        FileConfiguration configuration = bukkitConfig.getConfiguration();

        ConfigurationSection settings = configuration.getConfigurationSection("settings");
        this.levelSettings = new LevelSettings(settings.getInt("maxLevel"), settings.getInt("startingLevel"), settings.getInt("startingExperience"));
        this.logger.info("&6Loaded level settings.");


        for (String levelString : configuration.getConfigurationSection("levels").getKeys(false)) {
            int level = Integer.parseInt(levelString);

            ConfigurationSection levelSection = configuration.getConfigurationSection("levels." + levelString);

            List<Drug> drugList = new ArrayList<>(DrugHandler.get().getDrugConfig().getDrugs().values());

            List<String> unlockedDrugs = levelSection.getStringList("unlockedDrugs");
            int neededExperience = levelSection.getInt("neededExperience");

            int nextLevel = -1;
            if (this.levelSettings.getMaxLevel() != level) {
                nextLevel = levelSection.getInt("nextLevel");
            }
            Map<String, Map<String, Double>> qualityChances = new ConcurrentHashMap<>();


            Map<String, Double> prices = new ConcurrentHashMap<>();
            drugList.forEach(drug -> {
                Map<String, Double> qualityChanceMap = new ConcurrentHashMap<>();

                prices.put(drug.getName(), levelSection.getDouble("prices." + drug.getName(), 0));

                QualityHandler.get().getQualityMap().values().forEach(quality -> {
                    double chance = ConfigUtils.getChance("qualityChances." + drug.getName() + "." + quality.getName(), levelSection);
                    qualityChanceMap.put(quality.getName(), chance);
                });

                qualityChances.put(drug.getName(), qualityChanceMap);
            });

            Map<String, Level.ExperienceGain> experienceGain = new ConcurrentHashMap<>();
            for (Drug drug : drugList) {
                IntegerRange integerRange = null;
                if (levelSection.isString("experienceGain." + drug.getName() + ".amount")) {
                    String amountString = levelSection.getString("experienceGain." + drug.getName() + ".amount");
                    if (amountString.contains("-")) {

                        int min = GenericUtils.getInteger(amountString.split("-")[0].trim());
                        int max = GenericUtils.getInteger(amountString.split("-")[1].trim());
                        if (min > max) {
                            min = max;
                        }
                        integerRange = new IntegerRange(min, max);
                    }
                } else if (levelSection.isDouble("experienceGain." + drug.getName() + ".amount")) {
                    integerRange = new IntegerRange((int) levelSection.getDouble("experienceGain." + drug.getName() + ".amount"), (int) levelSection.getDouble("experienceGain." + drug.getName() + ".amount"));
                } else if (levelSection.isInt("experienceGain." + drug.getName() + ".amount")) {
                    integerRange = new IntegerRange(levelSection.getInt("experienceGain." + drug.getName() + ".amount"), levelSection.getInt("experienceGain." + drug.getName() + ".amount"));
                }
                if (integerRange == null) {
                    return;
                }
                Level.ExperienceGain.Piston piston = null;
                if (levelSection.isSet("experienceGain." + drug.getName() + ".piston.enabled") && levelSection.getBoolean("experienceGain." + drug.getName() + ".piston.enabled")) {
                    piston = new Level.ExperienceGain.Piston(levelSection.getBoolean("experienceGain." + drug.getName() + ".piston.enabled"), levelSection.getDouble("experienceGain." + drug.getName() + ".piston.multiplier"));
                }
                experienceGain.put(drug.getName(), new Level.ExperienceGain(drug.getName(), integerRange, piston));

            }

            ConfigurationSection onLevelUp = levelSection.getConfigurationSection("onLevelUp");
            List<String> commands = onLevelUp.getStringList("commands");
            Level.OnLevelUp.Message message = null;
            if (onLevelUp.isSet("message") && onLevelUp.getBoolean("message.enabled")) {
                Level.OnLevelUp.Title title = null;
                Level.OnLevelUp.Type type = Level.OnLevelUp.Type.valueOf(onLevelUp.getString("message.type").toUpperCase());
                if (type == Level.OnLevelUp.Type.TITLE) {
                    title = new Level.OnLevelUp.Title(onLevelUp.getString("message.title.main"), onLevelUp.getString("message.title.sub"), onLevelUp.getInt("message.title.stay"), onLevelUp.getInt("message.title.fadeIn"), onLevelUp.getInt("message.title.fadeOut"));
                }
                message = new Level.OnLevelUp.Message(onLevelUp.getBoolean("message.enabled"), type, onLevelUp.isSet("message.message") ? onLevelUp.getString("message.message") : null, title);
            }
            String broadcast = null;
            if (onLevelUp.isSet("broadcast")) {
                if (onLevelUp.isBoolean("broadcast") && !onLevelUp.getBoolean("broadcast")) {
                    broadcast = null;
                } else {
                    broadcast = onLevelUp.getString("broadcast");
                }
            }
            ConfigurationSection effects = onLevelUp.getConfigurationSection("effects");
            Level.OnLevelUp.Effects effects1 = null;
            Level.OnLevelUp.Effects.Fireworks fireworks = null;
            if (effects.isSet("fireworks.enabled") && effects.getBoolean("fireworks.enabled")) {
                Level.OnLevelUp.Effects.Fireworks.Firework firework = new Level.OnLevelUp.Effects.Fireworks.Firework(effects.getInt("fireworks.firework.power"), effects.getBoolean("fireworks.firework.flicker"), DyeColor.valueOf(effects.getString("fireworks.firework.color").toUpperCase()).getColor());
                fireworks = new Level.OnLevelUp.Effects.Fireworks(true, effects.getInt("fireworks.count"), effects.getInt("fireworks.delayBetween"), firework);

            }
            Level.OnLevelUp.Effects.Sound sound = null;
            if (effects.isSet("sound.enabled") && effects.getBoolean("sound.enabled")) {
                sound = new Level.OnLevelUp.Effects.Sound(true, effects.getString("sound.name"), ((float) (effects.getDouble("volume"))), ((float) (effects.getDouble("pitch"))));
            }
            effects1 = new Level.OnLevelUp.Effects(fireworks, sound);

            Level.OnLevelUp onLevelUp1 = new Level.OnLevelUp(commands, message, broadcast, effects1);
            Level level1 = new Level(level, neededExperience, experienceGain, nextLevel, qualityChances, prices, unlockedDrugs, onLevelUp1);
            System.out.println("Loaded level " + level + " (" + new Gson().toJson(level1) + ")");

            levelMap.put(level, level1);

        }

    }

    public LevelSettings getLevelSettings() {
        return levelSettings;
    }

    private void generateDefaultConfig() {
        final int maxLevel = 20;
        List<Drug> drugList = new ArrayList<>(DrugHandler.get().getDrugConfig().getDrugs().values());
        BukkitConfig bukkitConfig = new BukkitConfig("levels.yml", this.narcosDrugs);

        FileConfiguration configuration = bukkitConfig.getConfiguration();

        /*
         * ==========================
         *         Settings
         * ==========================
         */
        ConfigurationSection settings = configuration.createSection("settings");
        settings.set("maxLevel", maxLevel);
        settings.set("startingLevel", 0);
        settings.set("startingExperience", 0);
        configuration.set("settings", settings);
        ConfigurationSection levels = configuration.createSection("levels");

        for (int level = 1; level <= maxLevel; level++) {
            ConfigurationSection levelSection = levels.createSection(String.valueOf(level));

            levelSection.set("neededExperience", (2000 * level));
            List<String> drugs = new ArrayList<>();
            drugList.forEach(drug -> drugs.add(drug.getName()));
            levelSection.set("unlockedDrugs", drugs);
            if (level != maxLevel) {
                levelSection.set("nextLevel", (level + 1));
            }

            /*
             * Prices
             */
            ConfigurationSection prices = levelSection.createSection("prices");
            drugList.forEach(drug -> {
                prices.set(drug.getName(), 100);
            });
            levelSection.set("prices", prices);

            /*
             * Qualities
             */
            ConfigurationSection qualityChances = levelSection.createSection("qualityChances");
            drugList.forEach(drug -> {
                QualityHandler.get().getQualityMap().values().forEach(quality -> {
                    qualityChances.set(drug.getName() + "." + quality.getName(), quality.getDefaultChance());
                });
            });
            levelSection.set("qualityChances", qualityChances);


            /*
             * Experience gain
             */
            ConfigurationSection experienceGain = levelSection.createSection("experienceGain");
            drugList.forEach(drug -> {
                experienceGain.set(drug.getName() + ".amount", "10-100");
                experienceGain.set(drug.getName() + ".piston.enabled", true);
                experienceGain.set(drug.getName() + ".piston.multiplier", 0.05D);
            });
            levelSection.set("experienceGain", experienceGain);


            ConfigurationSection onLevelUp = levelSection.createSection("onLevelUp");
            List<String> commandStringList = new ArrayList<>();
            int min = 1 + (level * 10);
            int max = 1 + (level * 100);
            commandStringList.add("pay %player% %" + min + "-" + max + "%");
            onLevelUp.set("commands", commandStringList);
            onLevelUp.set("broadcast", "%prefix%Congrats to &7%player% &3for reaching level &7%level%&3.");

            ConfigurationSection message = onLevelUp.createSection("message");
            message.set("enabled", true);
            String type = "message";
            if (Math.random() * 100 <= 50.0) {
                type = "message";
            }
            double random = Math.random() * 100;
            if (random > 50 && random <= 100) {
                type = "title";

            }

            message.set("type", type.toUpperCase());
            switch (type) {
                case "message":
                    message.set("message", "%prefix%Congrats on reaching level &7&l%level%&3.");
                    break;
                case "title":
                    message.set("title.main", "&3&lLevel up");
                    message.set("title.sub", "&7Congrats on reaching level &3%level%&7!");
                    message.set("title.stay", 40);
                    message.set("title.fadeIn", 10);
                    message.set("title.fadeOut", 10);

                    break;
            }
            onLevelUp.set("message", message);

            /*
             * Effects
             */

            ConfigurationSection effects = onLevelUp.createSection("effects");
            effects.set("fireworks.enabled", true);
            effects.set("fireworks.count", 3);
            effects.set("fireworks.delayBetween", 5);
            effects.set("fireworks.firework.power", 2);
            effects.set("fireworks.firework.flicker", new Random().nextBoolean());
            effects.set("fireworks.firework.color", "RED");
            effects.set("sound.enabled", true);
            effects.set("sound.name", "LEVEL_UP");
            effects.set("sound.volume", 1.0D);
            effects.set("sound.pitch", 1.0D);
            onLevelUp.set("effects", effects);


            levelSection.set("onLevelUp", onLevelUp);


            levels.set(String.valueOf(level), levelSection);
        }
        configuration.set("levels", levels);

        bukkitConfig.setConfiguration(configuration);
        bukkitConfig.saveConfiguration();

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void reloadHandler() {

        loadLevels();
    }

    public Level getLevel(int level) {
        return levelMap.get(level);
    }

    public int getExperienceToGain(String drug, int levelNumber, boolean piston) {
        Level level;
        if (levelNumber == 0) {
            level = getLevel(1);
        } else {
            level = getLevel(levelNumber);
        }

        if (level.getExperienceGain().containsKey(drug.toLowerCase())) {
            Level.ExperienceGain experienceGain = level.getExperienceGain().get(drug.toLowerCase());
            IntegerRange amount = experienceGain.amount;
            double experience = amount.getRandomNumberInRange();
            if (experienceGain.piston != null && piston && experienceGain.piston.enabled) {
                experience = experience * experienceGain.piston.multiplier;
            }
            return (int) experience;
        }
        return -1;
    }
}
