package me.matthewe.atherial.narcos.narcosdrugs.level;

/**
 * Created by Matthew E on 6/20/2019 at 10:56 PM for the project NarcosDrugs
 */
public class LevelSettings {
    private final int maxLevel;
    private final  int startingLevel;
    private final int startingExperience;

    public LevelSettings(int maxLevel, int startingLevel, int startingExperience) {
        this.maxLevel = maxLevel;
        this.startingLevel = startingLevel;
        this.startingExperience = startingExperience;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getStartingLevel() {
        return startingLevel;
    }

    public int getStartingExperience() {
        return startingExperience;
    }
}

