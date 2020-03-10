package me.matthewe.atherial.narcos.narcosdrugs.drug.listeners;

import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.Drug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugEffect;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drug.GrowthStage;
import me.matthewe.atherial.narcos.narcosdrugs.drug.crop.CropBlock;
import me.matthewe.atherial.narcos.narcosdrugs.drug.world.WorldDrug;
import me.matthewe.atherial.narcos.narcosdrugs.drug.world.WorldDrugLocation;
import me.matthewe.atherial.narcos.narcosdrugs.menus.SellMenu;
import me.matthewe.atherial.narcos.narcosdrugs.utilities.TimeUtils;
import net.atherial.api.event.AtherialEventListener;
import net.atherial.api.event.AtherialListener;
import net.atherial.api.plugin.AtherialTasks;
import net.atherial.api.plugin.utilities.ItemUtils;
import net.atherial.api.plugin.utilities.MessageUtils;
import net.atherial.api.plugin.utilities.message.chat.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static me.matthewe.atherial.narcos.narcosdrugs.config.Messages.*;
import static net.atherial.api.plugin.utilities.MessageUtils.colorize;

/**
 * Created by Matthew E on 6/10/2019 at 6:37 PM for the project NarcosDrugs
 */
public class PlantListener implements AtherialListener, Listener {

    private List<UUID> delayList = new ArrayList<>();


    @Override
    public void setup(AtherialEventListener eventListener) {
        eventListener.listen((BlockPhysicsEvent event) -> {
            if (DrugHandler.get().isDrug(event.getBlock().getLocation())) {

                WorldDrug drug = DrugHandler.get().getDrug(event.getBlock().getLocation());
                if (!drug.isPlanting()) {
                    event.setCancelled(true);
                }
            }
        });
        eventListener.listen((BlockFormEvent event) -> {
//            if (event.getBlock().getType().toString().contains("WATER")&&event.getToBlock().getType()==Material.AIR){
//                return;
//            }
            if (DrugHandler.get().isDrug(event.getBlock().getLocation())) {
                event.setCancelled(true);

            }
        });

        eventListener.listen((BlockFromToEvent event) -> {

            if (DrugHandler.get().isDrug(event.getBlock().getLocation())) {

                WorldDrug drug = DrugHandler.get().getDrug(event.getBlock().getLocation());
                if (!drug.isPlanting()) {
                    event.setCancelled(true);
                }
            }
        });

        eventListener.listen((BlockPistonRetractEvent event) -> {
            for (Block block : event.getBlocks()) {
                if (DrugHandler.get().isDrug(block.getLocation())) {
                    WorldDrug drug = DrugHandler.get().getDrug(block.getLocation());
                    if (drug.isInBounds(block.getLocation()) && Bukkit.getPlayer(drug.getPlanter()) != null && Bukkit.getPlayer(drug.getPlanter()).isOnline()) {
                        drug.harvest(Bukkit.getPlayer(drug.getPlanter()), true);
                        event.setCancelled(true);
                        break;
                    }
                }
            }
        });
        eventListener.listen((BlockPistonExtendEvent event) -> {
            for (Block block : event.getBlocks()) {
                if (DrugHandler.get().isDrug(block.getLocation())) {
                    WorldDrug drug = DrugHandler.get().getDrug(block.getLocation());
                    if (drug.isInBounds(block.getLocation()) && Bukkit.getPlayer(drug.getPlanter()) != null && Bukkit.getPlayer(drug.getPlanter()).isOnline()) {
                        event.setCancelled(true);
                        drug.harvest(Bukkit.getPlayer(drug.getPlanter()), true);
                        break;
                    }
                }
            }
        });
//        eventListener.listen((BlockSpreadEvent event) -> {
//            if (DrugHandler.get().isDrug(event.getBlock().getLocation())) {
//                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "BlockSpreadEvent()");
//            }
//        });
//        eventListener.listen((BlockFromToEvent event) -> {
//            if (DrugHandler.get().isDrug(event.getBlock().getLocation())) {
//                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "BlockFromToEvent()");
//            }
//        });
//        eventListener.listen((BlockFadeEvent event) -> {
//            if (DrugHandler.get().isDrug(event.getBlock().getLocation())) {
//                Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "BlockFadeEvent()");
//            }
//        });
//        eventListener.listen((BlockFromToEvent event)->{ //Cancels soil from losing water effect.
//            if (event.getBlock().getType()==Material.SOIL&&event.getBlock().getData()==1&&(event.getToBlock().getType()==Material.SOIL&&event.getToBlock().getData()!=1)&&DrugHandler.get().isDrug(event.getBlock().getLocation())){
//                event.setCancelled(true);
//            }
//        });
        eventListener.listen((BlockBreakEvent event) -> {
            WorldDrugLocation worldDrugLocation = WorldDrugLocation.fromLocation(event.getBlock().getLocation());
//            worldDrugLocation.setY(worldDrugLocation.getY() + 1);

            Block soil = worldDrugLocation.getLocation().getBlock();
            if (DrugHandler.get().isDrug(worldDrugLocation.getLocation())) {
                WorldDrug worldDrug = DrugHandler.get().getDrug(worldDrugLocation.getLocation());

                WorldDrugLocation worldDrugLocation1 = WorldDrugLocation.fromLocation(worldDrug.getLocation().getLocation());
                worldDrugLocation1.setY(worldDrugLocation1.getY() - 1);
                if (worldDrugLocation.getY() == worldDrugLocation1.getY()) {
                    Drug drug = worldDrug.getDrugObject();
                    if (drug.getRequiredSoil().getData() == soil.getData() && drug.getRequiredSoil().getType() == soil.getType()) {
                        event.setCancelled(true);
                        return;
                    }
                    for (GrowthStage value : drug.getGrowthStages().values()) {
                        if (value.getSoil().getData() == soil.getData() && value.getSoil().getType() == soil.getType()) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            if (DrugHandler.get().isDrug(event.getBlock().getLocation())) {
                WorldDrug worldDrug = DrugHandler.get().getDrug(event.getBlock().getLocation());
//                if (!worldDrug.getPlanter().equals(event.getPlayer().getUniqueId())) {
//                    event.setCancelled(true);
//                    return;
//                }
                event.setCancelled(true);
                if (worldDrug.isInBounds(event.getBlock().getLocation())) {
                    worldDrug.harvest(event.getPlayer(), false);
                } else {
                    event.setCancelled(false);
                }

            }
        });
        eventListener.listen((BlockPlaceEvent event) -> {
            if (isDrugBlocking(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        });
//        if (event.getAction() == Action.RIGHT_CLICK_AIR && SellMenu.isDrug(event.getItem())) {
//            eventListener.cancelInteractEvent(event);
//            String drugName = SellMenu.getDrug(event.getItem());
//            if (drugName == null) {
//                return;
//            }
//            Drug drug = Drug.get(drugName);
//            if (drug != null) {
//                for (DrugEffect effect : drug.getEffects()) {
//                    effect.apply(event.getPlayer());
//                }
//                playSound(CONSUME_SOUND, event.getPlayer());
//                ItemUtils.removeItemInHand(event.getPlayer(), 1);
//            }
//            return;
//        }
        eventListener.listen(EventPriority.LOWEST, false, (PlayerInteractEvent event) -> {
            if (delayList.contains(event.getPlayer().getUniqueId())) {
                eventListener.cancelInteractEvent(event);
                return;
            }
            if (((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) && !SellMenu.isDrugSeed(event.getItem()) && SellMenu.isDrug(event.getItem(), false)) {

                eventListener.cancelInteractEvent(event);
                runDelay(event.getPlayer());
                String drugName = SellMenu.getDrug(event.getItem());
                if (drugName == null) {
                    return;
                }
                Drug drug = Drug.get(drugName);
                if (drug != null) {
                    for (DrugEffect effect : drug.getEffects()) {
                        effect.apply(event.getPlayer());
                    }
                    playSound(CONSUME_SOUND, event.getPlayer());
                    ItemUtils.removeItemInHand(event.getPlayer(), 1);
                }
            }
        });
        eventListener.listen((PlayerInteractEvent event) -> {
            Block block = event.getClickedBlock();
//            if (event.getAction()==Action.PHYSICAL && DrugHandler.get().isDrug(block.getLocation())){
//                eventListener.cancelInteractEvent(event);
//                event.getPlayer().sendMessage("Cancelled");
//                return;
//            }
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                return;
            }
            if (delayList.contains(event.getPlayer().getUniqueId())) {
                eventListener.cancelInteractEvent(event);
                return;
            }
//            if (event.getAction() == Action.RIGHT_CLICK_AIR && SellMenu.isDrug(event.getItem())) {
//                eventListener.cancelInteractEvent(event);
//                String drugName = SellMenu.getDrug(event.getItem());
//                if (drugName == null) {
//                    return;
//                }
//                Drug drug = Drug.get(drugName);
//                if (drug != null) {
//                    for (DrugEffect effect : drug.getEffects()) {
//                        effect.apply(event.getPlayer());
//                    }
//                    playSound(CONSUME_SOUND, event.getPlayer());
//                    ItemUtils.removeItemInHand(event.getPlayer(), 1);
//                }
//                return;
//            }


            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().isSneaking() && DrugHandler.get().isDrug(block.getLocation()) && Messages.SETTINGS_DRUG_CROP_INFO) {
                eventListener.cancelInteractEvent(event);
                runDelay(event.getPlayer());
                WorldDrug drug = DrugHandler.get().getDrug(block.getLocation());
//                if (drug.getPlanter().equals(event.getPlayer().getUniqueId())) {

                int px = MessageUtils.getPX(colorize(MESSAGES_CROP_STATUS_HEADER));
                ChatMessage.Builder builder = ChatMessage.builder()
                        .header(colorize(MESSAGES_CROP_STATUS_HEADER))
                        .append(ChatMessage.builder()
                                .message((Messages.MESSAGES_CROP_STATUS_CENTER) ? MessageUtils.centered(colorize(Messages.MESSAGES_CROP_STATUS_TEXT), px) : colorize(Messages.MESSAGES_CROP_STATUS_TEXT))
                                .newLine()
                                .build());

                int size = Messages.MESSAGES_CROP_STATUS_LINES.size();

                for (String statusLine : Messages.MESSAGES_CROP_STATUS_LINES) {
                    size--;

                    String message = colorize(statusLine)
                            .replaceAll("%planter%", event.getPlayer().getName())
                            .replaceAll("%name%", drug.getDrug())
                            .replaceAll("%growTime%", TimeUtils.formatTime(drug.getGrowTime()))
                            .replaceAll("%stage%", String.valueOf(drug.getStage()));
                    ChatMessage.Builder append = ChatMessage.builder()
                            .message((Messages.MESSAGES_CROP_STATUS_CENTER) ? MessageUtils.centered(message, px) : message);
                    if (size > 0) {
                        append.newLine();
                    }
                    builder.append(append.build());
                }
                builder = builder.footer(colorize(MESSAGES_CROP_STATUS_FOOTER));

                builder.build().send(event.getPlayer());
//                }
                return;


            }

            if (DrugHandler.get().isDrug(block.getLocation())) {
                eventListener.cancelInteractEvent(event);

                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem()) {
                ItemStack item = event.getItem();
                runDelay(event.getPlayer());
                if (DrugHandler.get().getDrugBySeed(item) != null) {
                    WorldDrugLocation worldDrugLocation1 = WorldDrugLocation.fromLocation(block.getLocation());

                    worldDrugLocation1.setY(worldDrugLocation1.getY() + 1);
                    if (DrugHandler.get().isDrug(worldDrugLocation1.getLocation())) {
                        return;
                    }
                    if (isDrugBlocking(block.getLocation())) {
                        return;
                    }

                    eventListener.cancelInteractEvent(event);
                    Drug drug = DrugHandler.get().getDrugBySeed(item);
                    if (drug.getRequiredSoil().getType() != event.getClickedBlock().getType() || drug.getRequiredSoil().getData() != event.getClickedBlock().getData()) {
                        return;
                    }
                    if (hasEnoughSpace(drug, event.getClickedBlock().getLocation())) {

                        WorldDrugLocation worldDrugLocation = WorldDrugLocation.fromLocation(block.getLocation());

                        worldDrugLocation.setY(worldDrugLocation.getY() + 1);
                        if (DrugHandler.get().isDrug(worldDrugLocation.getLocation())) {
                            return;
                        }
                        WorldDrug drug1 = DrugHandler.get().getWorldDrugConfig().createDrug(worldDrugLocation.getLocation(), drug, event.getPlayer());
                        drug1.plant();
                        ItemUtils.removeItemInHand(event.getPlayer(), 1);
//                        AtherialTasks.runIn(() -> {
//                            AtherialTasks.runIn(() -> {
//                                WorldDrugLocation worldDrugLocation = WorldDrugLocation.fromLocation(block.getLocation());
//
//                                worldDrugLocation.setY(worldDrugLocation.getY() + 1);
//                                if (DrugHandler.get().isDrug(worldDrugLocation.getLocation())) {
//                                    return;
//                                }
//                                WorldDrug drug1 = DrugHandler.get().getWorldDrugConfig().createDrug(worldDrugLocation.getLocation(), drug, event.getPlayer());
//                                drug1.plant();
//                                ItemUtils.removeItemInHand(event.getPlayer(), 1);
//                            }, 2);
//                        }, 2);
                    }
                }

            }

        });
    }

    private void runDelay(Player player) {
        final UUID uuid = player.getUniqueId();
        if (!delayList.contains(uuid)) {
            delayList.add(uuid);
        }
        AtherialTasks.runAsyncIn(() -> {
            if (delayList.contains(uuid)) {
                delayList.remove(uuid);
            }

        }, 3L);
    }

    private void playSound(String soundName, Player player) {
        Sound sound = null;
        try {
            sound = Sound.valueOf(soundName.toUpperCase());
        } catch (Exception e) {
            return;
        }
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }

    private boolean hasEnoughSpace(Drug drug, Location soil) {

        int maxY = -1;
        for (GrowthStage growthStage : drug.getGrowthStages().values()) {
            for (CropBlock value : growthStage.getBlocks().values()) {
                if (value.getOffset().getUp() >= maxY) {
                    maxY = value.getOffset().getUp();

                }
            }
        }
        maxY++;
        if (maxY != -1) {
            int i = 1;
            while (maxY > 0) {
                Block relative = soil.getBlock().getRelative(BlockFace.UP, i);
                if (relative.getType() != Material.AIR) {
                    return false;
                }
                maxY--;
                i++;
            }
        }
        return true;
    }

    private boolean isDrugBlocking(Location location) {
        List<BlockFace> blockFaces = Arrays.asList(BlockFace.UP, BlockFace.DOWN);

        for (int i = 1; i <= 5; i++) {
            for (BlockFace blockFace : blockFaces) {
                Block relative = location.getBlock().getRelative(blockFace, i);
                if (DrugHandler.get().isDrug(relative.getLocation()) && DrugHandler.get().getDrug(relative.getLocation()).isInBounds(location)) {
                    return true;
                }
            }
        }
        return false;
    }


}
