package me.matthewe.atherial.narcos.narcosdrugs.drug;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Matthew E on 6/27/2019 at 10:50 PM for the project NarcosDrugs
 */
public class DrugEffect {
    private String name;
    private int duration;
    private int level;

    public DrugEffect(String name, int duration, int level) {
        this.name = name;
        this.duration = duration;
        this.level = level;
    }

    public void apply(Player player) {
        PotionEffectType potionEffectType = null;
        try {
            potionEffectType = PotionEffectType.getByName(name.toUpperCase());
        } catch (Exception e) {
            return;
        }

        player.addPotionEffect(new PotionEffect(potionEffectType, duration, level));
    }

    public int getDuration() {
        return duration;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
