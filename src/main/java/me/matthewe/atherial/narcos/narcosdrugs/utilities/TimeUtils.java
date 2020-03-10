package me.matthewe.atherial.narcos.narcosdrugs.utilities;

import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/19/2019 at 2:54 PM for the project NarcosDrugs
 */
public class TimeUtils {
    public static String formatTime(long secondsInput) {
        long seconds = secondsInput;
        long minutes = 0;
        long hours = 0;
        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }

        StringBuilder stringBuilder = new StringBuilder();

        if (hours > 0) {
            stringBuilder.append(colorize(Messages.TIME_FORMAT_HOURS)
                    .replaceAll("%time%", String.valueOf(hours))).append(' ');
        }
        if (minutes > 0) {
            stringBuilder.append(colorize(Messages.TIME_FORMAT_MINUTES)
                    .replaceAll("%time%", String.valueOf(minutes))).append(' ');
        }
        if (seconds > 0) {
            stringBuilder.append(colorize(Messages.TIME_FORMAT_SECONDS)
                    .replaceAll("%time%", String.valueOf(seconds)));
        }
        if (stringBuilder.toString().trim().isEmpty()) {
            return "now";
        }
        return stringBuilder.toString().trim();
    }
}
