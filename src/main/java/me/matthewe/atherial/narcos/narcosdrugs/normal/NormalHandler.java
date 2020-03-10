package me.matthewe.atherial.narcos.narcosdrugs.normal;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.player.DrugPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Matthew E on 8/10/2019 at 11:42 PM for the project NarcosDrugs
 */
public class NormalHandler extends NarcosHandler implements Listener {
    private NormalConfig normalConfig;

    public NormalHandler(NarcosDrugs narcosDrugs) {
        super(narcosDrugs);
    }

    public NormalConfig getNormalConfig() {
        return normalConfig;
    }

    @Override
    public void onEnable() {
        normalConfig = new NormalConfig(this.narcosDrugs);
        this.normalConfig.load();
        registerBukkitListener(this);

    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        DrugPlayer drugPlayer = DrugPlayer.get(player);
        if (drugPlayer == null) {
            return;
        }
        if (DrugHandler.get().isDrug(event.getBlock().getLocation())){
            return;
        }
        for (NormalType value : NormalType.values()) {
            if (value.isBlock(event.getBlock())) {
                Normal normal = normalConfig.getNormal(value);
                drugPlayer.addExperience(player,normal.getXPToGive(drugPlayer.getLevel()).getRandomNumberInRange());
                return;

            }
        }
    }

    @Override
    public void onDisable() {

    }

    public static NormalHandler get() {
        return NarcosDrugs.getInstance().getHandler(NormalHandler.class);
    }

    @Override
    public void reloadHandler() {

    }
}
