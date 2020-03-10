package me.matthewe.atherial.narcos.narcosdrugs.player.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.player.PlayerHandler;
import net.atherial.api.event.AtherialEventListener;

/**
 * Created by Matthew E on 6/10/2019 at 8:13 PM for the project NarcosDrugs
 */
public class DrugPlayerListener implements net.atherial.api.event.AtherialListener {
    private final PlayerHandler playerHandler;

    public DrugPlayerListener() {
        this.playerHandler = PlayerHandler.get();
    }

    @Override
    public void setup(AtherialEventListener eventListener) {
        eventListener.onJoin(event -> playerHandler.getPlayerManager().loadPlayer(event.getPlayer()));
        eventListener.onQuit(event -> playerHandler.getPlayerManager().savePlayer(event.getPlayer().getUniqueId()));
    }
}
