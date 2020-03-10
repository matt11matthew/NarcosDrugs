package me.matthewe.atherial.narcos.narcosdrugs.npc;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.menus.DrugBuyerMenu;
import me.matthewe.atherial.narcos.narcosdrugs.menus.DrugBuyerMenuType;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.FacePlayerNpcBehavior;
import net.atherial.api.plugin.hologram.HolographicDisplaysHologramManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.Skin;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/19/2019 at 3:08 PM for the project NarcosDrugs
 */
public class NarcosNPC {
    private final UUID uuid;
    private Location location;
    private Hologram hologram;
    private NPC npc;

    public NarcosNPC(NPC npc) {
        this.uuid = UUID.randomUUID();
        this.location = npc.getStoredLocation();
        this.npc = npc;
    }

    public NarcosNPC(Location location, UUID uuid) {
        this.location = location;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }


    public boolean despawn() {
        boolean removedNpc = false;
        boolean removedHologram = false;

        if (isSpawned()) {
            npc.destroy();
            removedNpc = true;
            this.npc = null;
        }

        if (this.hologram != null) {
            this.hologram.delete();
            this.hologram = null;
            removedHologram = true;
        }
        return removedNpc && removedHologram;
    }

    public boolean isSpawned() {
        return npc != null && npc.isSpawned();
    }

    public NPC getNpc() {
        return npc;
    }

    public Location getLocation() {
        return location;
    }


    public Hologram getHologram() {
        return hologram;
    }

    public void update() {
        List<String> lines = new ArrayList<>();
        double height = 2;
        for (String name : Messages.NPC_NAME) {
            lines.add(colorize(name));
            height += 0.32;
        }
        String finalLine = lines.get(lines.size() - 1);
        lines.remove(lines.size() - 1);

        HolographicDisplaysHologramManager<NarcosDrugs> hologramManager = NPCHandler.get().getHologramManager();

        npc.data().remove(NPC.PLAYER_SKIN_UUID_METADATA);
        npc.data().remove(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA);
        npc.data().remove(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA);
        npc.data().remove("cached-skin-uuid-name");
        npc.data().remove("cached-skin-uuid");
        npc.data().remove(NPC.PLAYER_SKIN_UUID_METADATA);


        npc.data().set(NPC.PLAYER_SKIN_USE_LATEST, false);
npc.getEntity().setCustomNameVisible(true);
npc.getEntity().setCustomName(finalLine);
        npc.despawn();
        npc.spawn(location);

        if (npc.isSpawned()) {

            SkinnableEntity skinnable = npc.getEntity() instanceof SkinnableEntity ? (SkinnableEntity) npc.getEntity() : null;
            if (skinnable != null) {
                skinnable.setSkinName(MenuConfig.get().npcSkin);
                Skin.get(skinnable).applyAndRespawn(skinnable);

            }
        }
        double finalHeight = height;
        if (npc.getEntity() != null && !npc.getEntity().hasMetadata("NARCOS_DRUGS_NPC")) {
            npc.getEntity().setMetadata("NARCOS_DRUGS_NPC", new FixedMetadataValue(NarcosDrugs.getInstance(), true));

        }
        npc.getDefaultGoalController().addBehavior(new FacePlayerNpcBehavior(npc, this), 23);
        Location newLocation = new Location(location.getWorld(), location.getX(), location.getY() + finalHeight, location.getZ(), location.getYaw(), location.getPitch());
        this.hologram = hologramManager.spawnHologram(newLocation, lines.toArray(new String[0]));


    }

    public void spawn() {
        if (location == null) {
            return;
        }
        if (isSpawned()) {
            return;
        }

        List<String> lines = new ArrayList<>();

        double height = 2;
        for (String name : Messages.NPC_NAME) {
            lines.add(colorize(name));
            height += 0.32;
        }
        String finalLine = lines.get(lines.size() - 1);
        lines.remove(lines.size() - 1);

        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, finalLine);
        HolographicDisplaysHologramManager<NarcosDrugs> hologramManager = NPCHandler.get().getHologramManager();

        npc.data().remove(NPC.PLAYER_SKIN_UUID_METADATA);
        npc.data().remove(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_METADATA);
        npc.data().remove(NPC.PLAYER_SKIN_TEXTURE_PROPERTIES_SIGN_METADATA);
        npc.data().remove("cached-skin-uuid-name");
        npc.data().remove("cached-skin-uuid");
        npc.data().remove(NPC.PLAYER_SKIN_UUID_METADATA);


        npc.data().set(NPC.PLAYER_SKIN_USE_LATEST, false);


        npc.spawn(location);
        if (npc.isSpawned()) {

            SkinnableEntity skinnable = npc.getEntity() instanceof SkinnableEntity ? (SkinnableEntity) npc.getEntity() : null;
            if (skinnable != null) {
                skinnable.setSkinName(MenuConfig.get().npcSkin);
                Skin.get(skinnable).applyAndRespawn(skinnable);

            }
        }
        double finalHeight = height;
        if (npc.getEntity() != null && !npc.getEntity().hasMetadata("NARCOS_DRUGS_NPC")) {
            npc.getEntity().setMetadata("NARCOS_DRUGS_NPC", new FixedMetadataValue(NarcosDrugs.getInstance(), true));

        }
        npc.getDefaultGoalController().addBehavior(new FacePlayerNpcBehavior(npc, this), 23);
        Location newLocation = new Location(location.getWorld(), location.getX(), location.getY() + finalHeight, location.getZ(), location.getYaw(), location.getPitch());
        this.hologram = hologramManager.spawnHologram(newLocation, lines.toArray(new String[0]));
    }

    public void openMenu(Player player) {
        new DrugBuyerMenu(player, DrugBuyerMenuType.MAIN).open();
    }
}
