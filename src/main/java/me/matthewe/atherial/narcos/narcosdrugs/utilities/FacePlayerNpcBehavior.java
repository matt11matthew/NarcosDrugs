package me.matthewe.atherial.narcos.narcosdrugs.utilities;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.npc.NarcosNPC;
import net.citizensnpcs.api.ai.tree.Behavior;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

/**
 * Created by Matthew E on 6/19/2019 at 4:48 PM for the project NarcosDrugs
 */
public class FacePlayerNpcBehavior implements Behavior {
    private NPC npc;
    private final NarcosNPC narcosNPC;

    public FacePlayerNpcBehavior(NPC npc, NarcosNPC narcosNPC) {
        this.npc = npc;
        this.narcosNPC = narcosNPC;
    }

    @Override
    public void reset() {
        npc.teleport(narcosNPC.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private Player getClosestPlayer() {
        List<Player> players = NarcosDrugs.getInstance().getPlayers(player -> !player.hasMetadata("NPC") && player.getWorld().getName().equalsIgnoreCase(npc.getEntity().getWorld().getName()) && player.getLocation().distanceSquared(npc.getEntity().getLocation()) <= Messages.SETTINGS_MAX_FACE_DISTANCE);
        players.sort((o1, o2) -> Double.compare(o1.getLocation().distanceSquared(npc.getEntity().getLocation()), o2.getLocation().distanceSquared(npc.getEntity().getLocation())));
        if (players.isEmpty()) {
            return null;
        }

        return players.get(0);
    }

    @Override
    public BehaviorStatus run() {
        Player closestPlayer = getClosestPlayer();
        if (closestPlayer != null) {
            npc.faceLocation(closestPlayer.getLocation());
        } else {
            npc.teleport(narcosNPC.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
        return BehaviorStatus.RUNNING;
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }
}
