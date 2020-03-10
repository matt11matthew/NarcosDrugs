package me.matthewe.atherial.narcos.narcosdrugs.level;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.IntegerRange;
import net.atherial.api.plugin.AtherialTasks;
import net.atherial.api.plugin.utilities.GenericUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;
import static net.atherial.api.plugin.utilities.MessageUtils.message;

/**
 * Created by Matthew E on 6/20/2019 at 10:55 PM for the project NarcosDrugs
 */
public class Level {
    public int level;
    public int neededExperience;
    public Map<String, ExperienceGain> experienceGain;
    public int nextLevel;
    public Map<String, Map<String, Double>> qualityChances;
    public Map<String, Double> prices;
    public List<String> unlockedDrugs;
    public OnLevelUp onLevelUp;


    public Level(int level, int neededExperience, Map<String, ExperienceGain> experienceGain, int nextLevel, Map<String, Map<String, Double>> qualityChances, Map<String, Double> prices, List<String> unlockedDrugs, OnLevelUp onLevelUp) {
        this.level = level;
        this.neededExperience = neededExperience;
        this.experienceGain = experienceGain;
        this.nextLevel = nextLevel;
        this.qualityChances = qualityChances;
        this.prices = prices;
        this.unlockedDrugs = unlockedDrugs;
        this.onLevelUp = onLevelUp;
    }

    public static Level get(int level) {
        return LevelHandler.get().getLevel(level);
    }

    public String getQuality(String drug) {
        Map<String, Double> chanceMap = qualityChances.get(drug.toLowerCase());

        List<String> foundRewards = new ArrayList<>();
        for (String reward : chanceMap.keySet()) {
            if (Math.random() * 100.0D <= chanceMap.get(reward)) {
                foundRewards.add(reward);
            }
        }
        if (foundRewards.isEmpty()) {
            return getQuality(drug);
        }
        return foundRewards.get(0);

    }


    public int getLevel() {
        return level;
    }

    public int getNeededExperience() {
        return neededExperience;
    }

    public Map<String, ExperienceGain> getExperienceGain() {
        return experienceGain;
    }

    public int getNextLevel() {
        return nextLevel;
    }

    public Map<String, Map<String, Double>> getQualityChances() {
        return qualityChances;
    }

    public Map<String, Double> getPrices() {
        return prices;
    }

    public List<String> getUnlockedDrugs() {
        return unlockedDrugs;
    }

    public OnLevelUp getOnLevelUp() {
        return onLevelUp;
    }

    public static class ExperienceGain {
        public String drug;
        public IntegerRange amount;
        public Piston piston;

        public ExperienceGain(String drug, IntegerRange amount, Piston piston) {
            this.drug = drug;
            this.amount = amount;
            this.piston = piston;
        }

        public static class Piston {
            public boolean enabled;
            public double multiplier;

            public Piston(boolean enabled, double multiplier) {
                this.enabled = enabled;
                this.multiplier = multiplier;
            }
        }
    }

    /*
        onLevelUp:
          commands:
            "pay %player% %100-200%"
          message:
            enabled: true
            type: title/message/action
            message: "%prefix%Congrats on reaching level &7&l%level%&3."
            actionBar:
              text: "&3Congrats on reaching level &7&l%level%&3."
              stay: 40
              fadeIn: 10
              fadeOut: 10
            title:
              main: "&3&lLevel up"
              sub: "&7Congrats on reaching level &3%level%&7!"
              stay: 40
              fadeIn: 10
              fadeOut: 10
          broadcast: false # Replace with "Message" to send a broadcast message
          effects:
            fireworks:
              enabled: true
              count: 3
              delayBetween: 5
              firwork:
                power: 2
                flicker: true
                color: red
            sound:
              enabled: true
              name: "LEVEL_UP"
              volume: 1.0
              pitch: 1.0
     */
    public static class OnLevelUp {
        public List<String> commands;
        public Message message;
        public String broadcast;
        public Effects effects;

        public OnLevelUp(List<String> commands, Message message, String broadcast, Effects effects) {
            this.commands = commands;
            this.message = message;
            this.broadcast = broadcast;
            this.effects = effects;
        }


        public void sendBroadcast(Player player, int level) {
            if (broadcast == null || broadcast.isEmpty() || broadcast.equalsIgnoreCase("false")) {
                return;
            }
            Bukkit.getServer().broadcastMessage(Messages.replacePrefix(this.broadcast).replaceAll("%player%", player.getName()).replaceAll("%level%", String.valueOf(level)));
        }

        public void runCommands(Player player, int level) {
            for (String command : commands) {
                command = command.replaceAll("%player%", player.getName()).replaceAll("%level%", String.valueOf(level)).trim();

                Map<String, String> toReplaceMap = new ConcurrentHashMap<>();
                if (command.contains("%")) {
                    for (String string : command.split("%")) {
                        string = string.trim();
                        if (string.isEmpty()) {
                            continue;
                        }
                        if (string.contains("-")) {
                            String[] split = string.split("-");
                            if (GenericUtils.isInteger(split[0].trim()) && GenericUtils.isInteger(split[1].trim())) {
                                int min = GenericUtils.getInteger(split[0].trim());
                                int max = GenericUtils.getInteger(split[1].trim());
                                if (min > max) {
                                    min = max;
                                }

                                IntegerRange integerRange = new IntegerRange(min, max);
                                toReplaceMap.put("%" + min + "-" + max + "%", String.valueOf(integerRange.getRandomNumberInRange()));
                            }
                        } else if (string.contains(",")) {
                            List<String> stringList = new ArrayList<>();
                            for (String s : string.split(",")) {
                                stringList.add(s.trim());
                            }
                            if (!stringList.isEmpty()) {
                                if (stringList.size() == 1) {
                                    toReplaceMap.put("%" + string + "%", stringList.get(0));
                                } else {
                                    int i = new Random().nextInt(stringList.size() - 1);
                                    String s = stringList.get(i);
                                    toReplaceMap.put("%" + string + "%", s == null ? stringList.get(0) : stringList.get(i));
                                }
                            }
                        }
                    }
                    for (Map.Entry<String, String> stringStringEntry : toReplaceMap.entrySet()) {
                        command = command.replaceAll(stringStringEntry.getKey(), stringStringEntry.getValue());
                    }
                }
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceFirst("/", "").trim());
            }

        }

        public void sendMessage(Player player, int level) {
            if (message != null && message.enabled) {
                message.send(player, level);
            }
        }

        public static class Effects {
            public Fireworks fireworks;
            public Sound sound;

            public Effects(Fireworks fireworks, Sound sound) {
                this.fireworks = fireworks;
                this.sound = sound;
            }

            public static class Sound {

                public boolean enabled;
                public String name;
                public float volume;
                public float pitch;

                public Sound(boolean enabled, String name, float volume, float pitch) {
                    this.enabled = enabled;
                    this.name = name;
                    this.volume = volume;
                    this.pitch = pitch;
                }

                public void play(Player player) {
                    org.bukkit.Sound sound = null;
                    try {
                        sound = org.bukkit.Sound.valueOf(this.name.toUpperCase());
                    } catch (Exception ignored) {
                        return;
                    }
                    player.playSound(player.getLocation(), sound, this.volume, this.pitch);
                }
            }

            public static class Fireworks {

                public boolean enabled;
                public int count;
                public int delayBetween;
                public Firework firework;

                public Fireworks(boolean enabled, int count, int delayBetween, Firework firework) {
                    this.enabled = enabled;
                    this.count = count;
                    this.delayBetween = delayBetween;
                    this.firework = firework;
                }

                public void detonate(Location location) {
                    if (enabled && firework != null) {
                        int delay = new Integer(delayBetween).intValue();
                        for (int i = 0; i < count; i++) {
                            AtherialTasks.runAsyncIn(() -> {
                                AtherialTasks.run(() -> {
                                    firework.detonate(location);
                                });
                            }, delay);
                            delay += delayBetween;
                        }
                    }
                }

                public static class Firework {
                    public int power;
                    public boolean flicker;
                    public Color color;

                    public Firework(int power, boolean flicker, Color color) {
                        this.power = power;
                        this.flicker = flicker;
                        this.color = color;
                    }

                    public void detonate(Location location) {

                        org.bukkit.entity.Firework firework = (org.bukkit.entity.Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();

                        fireworkMeta.setPower(this.power);
                        fireworkMeta.addEffect(FireworkEffect.builder().flicker(this.flicker).withColor(this.color).build());

                        firework.setFireworkMeta(fireworkMeta);
                        firework.detonate();
                    }

                }
            }
        }


        public static class Message {
            public boolean enabled;

            public Type type;


            public String message;
            public Title title;

            public Message(boolean enabled, Type type, String message, Title title) {
                this.enabled = enabled;
                this.type = type;

                this.message = message;
                this.title = title;
            }

            public void send(Player player, int level) {

                switch (type) {

                    case TITLE:
                        if (title != null) {
                            title.send(player, level);
                        }
                        break;
                    case MESSAGE:
                        message(player, Messages.replacePrefix(this.message).replaceAll("%player%", player.getName()).replaceAll("%level%", String.valueOf(level)));
                        break;
                }
            }
        }

        public static class Title {
            public String main;
            public String sub;
            public int stay;
            public int fadeIn;
            public int fadeOut;

            public Title(String main, String sub, int stay, int fadeIn, int fadeOut) {
                this.main = main;
                this.sub = sub;
                this.stay = stay;
                this.fadeIn = fadeIn;
                this.fadeOut = fadeOut;
            }

            public void send(Player player, int level) {
                NarcosDrugs.getInstance().getVersionProvider().sendTitle(player, colorize(main)
                        .replaceAll("%player%", player.getName())
                        .replaceAll("%level%", String.valueOf(level)), sub != null && !sub.isEmpty() ? colorize(sub)
                        .replaceAll("%player%", player.getName())
                        .replaceAll("%level%", String.valueOf(level)) : null, stay, fadeIn, fadeOut);
            }
        }

        public static enum Type {
            TITLE, MESSAGE;
        }
    }

}
