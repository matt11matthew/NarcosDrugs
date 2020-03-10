package me.matthewe.atherial.narcos.narcosdrugs.npc.commands;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 6/19/2019 at 3:17 PM for the project NarcosDrugs
 */
public class NarcosDrugsNPCCommand extends NarcosDrugsSubCommand {

    public NarcosDrugsNPCCommand(NarcosDrugsCommand narcosDrugsCommand) {
        super("npc", narcosDrugsCommand);
        addSubCommand(new NPCCreateSubCommand(narcosDrugsCommand, this));
    }

    @Override
    public void run(CommandSender sender, String[] args) {

    }

    @Override
    public List<HelpSubCommand> getHelp(String[] args) {
        List<HelpSubCommand> helpList = new ArrayList<>();
        this.getSubCommandMap().values().forEach(spigotCommand -> helpList.addAll(spigotCommand.getHelp(args)));
        return helpList;
    }
}
