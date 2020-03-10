package me.matthewe.atherial.narcos.narcosdrugs.normal;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.level.Level;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.IntegerRange;
import net.atherial.api.config.BukkitConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 8/10/2019 at 11:44 PM for the project NarcosDrugs
 */
public class NormalConfig {
    private BukkitConfig bukkitConfig;
    private NarcosDrugs narcosDrugs;
    private Map<NormalType, Normal> normalMap;

    public NormalConfig(NarcosDrugs narcosDrugs) {
        this.narcosDrugs = narcosDrugs;
        this.bukkitConfig = new BukkitConfig("normal_crops.yml", narcosDrugs);
    }

    public Normal getNormal(NormalType normalType) {
        return normalMap.getOrDefault(normalType,null);
    }

    public Map<NormalType, Normal> getNormalMap() {
        return normalMap;
    }

    public BukkitConfig getBukkitConfig() {
        return bukkitConfig;
    }

    public void load() {
        normalMap = new ConcurrentHashMap<>();

        this.bukkitConfig = new BukkitConfig("normal_crops.yml", narcosDrugs);

        FileConfiguration configuration = bukkitConfig.getConfiguration();


        for (NormalType value : NormalType.values()) {
            if (configuration.isSet(value.toString())) {
                ConfigurationSection section = configuration.getConfigurationSection(value.toString());
                Map<Integer, Double> sellMap = new ConcurrentHashMap<>();
                Map<Integer, IntegerRange> xpMap = new ConcurrentHashMap<>();
                for (String sellPrices : section.getConfigurationSection("sellPrices").getKeys(false)) {
                    int level = Integer.parseInt(sellPrices);
                    double aDouble = section.getDouble("sellPrices." + level);
                    sellMap.put(level, aDouble);
                }

                for (String xpAmounts : section.getConfigurationSection("xpAmounts").getKeys(false)) {
                    int level = Integer.parseInt(xpAmounts);
                    IntegerRange integerRange = new IntegerRange((section.getString("xpAmounts." + level)));
                    xpMap.put(level, integerRange);
                }
                normalMap.put(value, new Normal(value, sellMap, xpMap));

            } else {
                Map<Integer, Double> sellMap = new ConcurrentHashMap<>();
                Map<Integer, IntegerRange> xpMap = new ConcurrentHashMap<>();
                for (Map.Entry<Integer, Level> entry : LevelHandler.get().getLevelMap().entrySet()) {
                    sellMap.put(entry.getKey(), 50.0D);
                    xpMap.put(entry.getKey(), new IntegerRange(50, 100));
                }
                normalMap.put(value, new Normal(value, sellMap, xpMap));
                saveConfig();
            }
        }
    }

    private void saveConfig() {

        this.bukkitConfig = new BukkitConfig("normal_crops.yml", narcosDrugs);
        FileConfiguration configuration = bukkitConfig.getConfiguration();
        for (NormalType value : NormalType.values()) {
            if (normalMap.containsKey(value)) {
                Normal normal = normalMap.get(value);
                normal.getSellMap().forEach((integer, aDouble) -> {
                    configuration.set(value.toString() + ".sellPrices." + integer, aDouble);
                });
                normal.getXpMap().forEach((integer, range) -> {
                    configuration.set(value.toString() + ".xpAmounts." + integer, range.toString());
                });
            } else {
                Map<Integer, Double> sellMap = new ConcurrentHashMap<>();
                Map<Integer, IntegerRange> xpMap = new ConcurrentHashMap<>();
                for (Map.Entry<Integer, Level> entry : LevelHandler.get().getLevelMap().entrySet()) {
                    sellMap.put(entry.getKey(), 50.0D);
                    xpMap.put(entry.getKey(), new IntegerRange(50, 100));
                }
                Normal normal = new Normal(value, sellMap, xpMap);
                normal.getSellMap().forEach((integer, aDouble) -> {
                    configuration.set(value.toString() + ".sellPrices." + integer, aDouble);
                });
                normal.getXpMap().forEach((integer, range) -> {
                    configuration.set(value.toString() + ".xpAmounts." + integer, range.toString());
                });
            }
        }
        bukkitConfig.setConfiguration(configuration);
        bukkitConfig.saveConfiguration();
    }
}
