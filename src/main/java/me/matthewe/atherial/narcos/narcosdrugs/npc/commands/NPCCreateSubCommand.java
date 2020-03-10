package me.matthewe.atherial.narcos.narcosdrugs.npc.commands;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.npc.NPCHandler;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 6/19/2019 at 3:18 PM for the project NarcosDrugs
 */
public class NPCCreateSubCommand extends NarcosDrugsSubCommand {
    private NarcosDrugsNPCCommand narcosDrugsNPCCommand;


    public NPCCreateSubCommand(NarcosDrugsCommand narcosDrugsCommand, NarcosDrugsNPCCommand narcosDrugsNPCCommand) {
        super("create", narcosDrugsCommand);
        this.narcosDrugsNPCCommand = narcosDrugsNPCCommand;
        this.permission = Messages.PERMISSION_ADMIN;
        this.playerOnly = true;


    }


    @Override
    public void run(CommandSender sender, String[] strings) {
        Player player = (Player) sender;

        NPCHandler npcHandler = NPCHandler.get();
        if (npcHandler.isLoading()) {
            message(sender, Messages.replacePrefix(Messages.ERRORS_NPCS_LOADING));
            return;
        }

        if (npcHandler.isNPC(player.getLocation())) {
            message(sender, Messages.replacePrefix(Messages.ERRORS_NPC_ALREADY_EXISTS));
            return;
        }
        if (npcHandler.createNPC(player.getLocation())) {
            Location location = player.getLocation();
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            this.message(sender, Messages.replacePrefix(Messages.MESSAGES_NPC_CREATE)
                    .replaceAll("%yaw%", decimalFormat.format(location.getYaw()))
                    .replaceAll("%z%", decimalFormat.format(location.getZ()))
                    .replaceAll("%y%", decimalFormat.format(location.getY()))
                    .replaceAll("%pitch%", decimalFormat.format(location.getPitch()))
                    .replaceAll("%x%", decimalFormat.format(location.getX())));
        }

    }

    @Override
    public List<HelpSubCommand> getHelp(String[] strings) {
        return Arrays.asList(HelpSubCommand.builder()

                .permission(this.permission)
                .command(this.narcosDrugsCommand.label + " npc create")
                .description(Messages.HELP_NPC_CREATE)
                .build());
    }
}
