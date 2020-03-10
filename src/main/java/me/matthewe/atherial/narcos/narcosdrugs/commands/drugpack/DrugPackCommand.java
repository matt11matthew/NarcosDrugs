package me.matthewe.atherial.narcos.narcosdrugs.commands.drugpack;

import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.NarcosDrugsSubCommand;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 6/19/2019 at 1:57 PM for the project NarcosDrugs
 */
public class DrugPackCommand extends NarcosDrugsSubCommand {


    public DrugPackCommand(NarcosDrugsCommand narcosDrugsCommand) {
        super("drugpack", narcosDrugsCommand);

        addSubCommand(new DrugPackGiveCommand(narcosDrugsCommand, this));
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
