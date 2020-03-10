package me.matthewe.atherial.narcos.narcosdrugs;

import net.atherial.api.event.AtherialListener;
import net.atherial.api.plugin.handler.Handler;
import net.atherial.api.plugin.utilities.logger.AtherialLogger;
import net.atherial.api.plugin.utilities.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public abstract class NarcosHandler extends Handler<NarcosDrugs, AtherialListener> {
    public NarcosDrugs narcosDrugs;
    public Logger logger;
    private static boolean reloading;

    public NarcosHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
        this.narcosDrugs = narcosDrugs;
        this.eventListeners = new ArrayList<>();
        this.logger = new AtherialLogger(this.getClass(), narcosDrugs);
    }

    public static boolean isReloading() {
        return reloading;
    }

    public static void setReloading(boolean reloading) {
        NarcosHandler.reloading = reloading;
    }

    @Override
    public void registerListeners() {
        this.eventListeners.forEach(eventListener -> eventListener.setup(this.narcosDrugs.getEventListener()));
    }

    private List<String> listeners = new ArrayList<>();


    public void registerBukkitListener(Listener listener) {
        if (listeners.contains(listener.getClass().getName())) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(listener, this.narcosDrugs);
        listeners.add(listener.getClass().getName());
    }

    public abstract void reloadHandler();

    @Override
    public void enable() {
        super.enable();

    }

}
