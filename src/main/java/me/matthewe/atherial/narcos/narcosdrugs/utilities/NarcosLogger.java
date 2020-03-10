package me.matthewe.atherial.narcos.narcosdrugs.utilities;

import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import net.atherial.api.plugin.AtherialPlugin;
import net.atherial.api.plugin.utilities.logger.AtherialLogger;

/**
 * Created by Matthew E on 6/10/2019 at 7:15 PM for the project NarcosDrugs
 */
public class NarcosLogger extends AtherialLogger {
    public NarcosLogger(Class clazz, AtherialPlugin atherialPlugin) {
        super(clazz, atherialPlugin);
    }

    @Override
    public void debug(String message, Object... args) {
        if (!Messages.SETTINGS_DEBUG) {
            return;
        }
        super.debug(message, args);
    }
}