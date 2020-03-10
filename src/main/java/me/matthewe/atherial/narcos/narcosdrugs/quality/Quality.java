package me.matthewe.atherial.narcos.narcosdrugs.quality;

/**
 * Created by Matthew E on 6/20/2019 at 11:28 PM for the project NarcosDrugs
 */
public class Quality {
    private String name;
    private String lore;
    private double defaultChance;
    private double sellMultiplier;

    public Quality(String name, String lore, double defaultChance, double sellMultiplier) {
        this.name = name;
        this.lore = lore;
        this.defaultChance = defaultChance;
        this.sellMultiplier = sellMultiplier;
    }

    public double getSellMultiplier() {
        return sellMultiplier;
    }

    public String getName() {
        return name;
    }

    public double getDefaultChance() {
        return defaultChance;
    }

    public String getLore() {
        return lore;
    }

}
