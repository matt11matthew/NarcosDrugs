package me.matthewe.atherial.narcos.narcosdrugs.npc.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.npc.NPCHandler;
import me.matthewe.atherial.narcos.narcosdrugs.npc.NarcosNPC;
import net.atherial.api.event.AtherialEventListener;
import net.atherial.api.event.AtherialListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Created by Matthew E on 6/19/2019 at 5:31 PM for the project NarcosDrugs
 */
public class NpcRightClickListener implements AtherialListener {
    @Override
    public void setup(AtherialEventListener eventListener) {
        eventListener.listen((PlayerInteractEntityEvent event) -> {
            Entity rightClicked = event.getRightClicked();
            if ((rightClicked != null) && rightClicked.hasMetadata("NPC") && rightClicked.hasMetadata("NARCOS_DRUGS_NPC") && NPCHandler.get().isNPC(rightClicked)) {
                event.setCancelled(true);
                NarcosNPC npc = NPCHandler.get().getNPC(rightClicked);
                npc.openMenu(event.getPlayer());
            }
        });
    }
}
