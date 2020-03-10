package me.matthewe.atherial.narcos.narcosdrugs.utilities;

import net.atherial.api.plugin.utilities.GenericUtils;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by Matthew E on 6/20/2019 at 11:34 PM for the project NarcosDrugs
 */
public class ConfigUtils {
    public static double getChance(String key, ConfigurationSection section) {
        double chance = -1.0D;
        if (section.isString(key)) {
            String chanceString = section.getString(key).replaceAll("%", "");

            if (GenericUtils.isInteger(chanceString)) {
                chance = GenericUtils.getInteger(chanceString);
            } else if (GenericUtils.isDouble(chanceString)) {
                chance = GenericUtils.getDouble(chanceString);
            }
        } else if (section.isDouble(key)) {
            chance = section.getDouble(key);
        } else if (section.isInt(key)) {
            chance = section.getInt(key);
        }
        return chance;
    }
}
