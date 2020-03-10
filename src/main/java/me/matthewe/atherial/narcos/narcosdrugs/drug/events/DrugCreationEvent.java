package me.matthewe.atherial.narcos.narcosdrugs.drug.events;

import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Matthew E on 6/20/2019 at 11:47 PM for the project NarcosDrugs
 */
public class DrugCreationEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Drug drug;

    public DrugCreationEvent(Drug drug) {
        this.drug = drug;
    }

    public Drug getDrug() {
        return drug;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
