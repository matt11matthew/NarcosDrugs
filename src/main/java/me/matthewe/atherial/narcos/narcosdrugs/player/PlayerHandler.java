package me.matthewe.atherial.narcos.narcosdrugs.player;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.player.listeners.DrugPlayerListener;

/**
 * Created by Matthew E on 6/10/2019 at 6:47 PM for the project NarcosDrugs
 */
public class PlayerHandler extends NarcosHandler {
    private DrugPlayerManager playerManager;

    public PlayerHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    @Override
    public void onEnable() {
        this.playerManager = new DrugPlayerManager(this.narcosDrugs);
        this.playerManager.loadAllOnline();

        this.registerListener(new DrugPlayerListener());
    }

    @Override
    public void onDisable() {
        this.playerManager.saveAllOnline();
    }

    @Override
    public void reloadHandler() {
        this.playerManager.saveAllOnline();
        this.playerManager.loadAllOnline();

    }

    public DrugPlayerManager getPlayerManager() {
        return playerManager;
    }

    public static PlayerHandler get() {
        return NarcosDrugs.getInstance().getHandler(PlayerHandler.class);
    }
}
