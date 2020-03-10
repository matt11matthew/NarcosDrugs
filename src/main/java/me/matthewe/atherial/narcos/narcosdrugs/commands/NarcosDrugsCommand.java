package me.matthewe.atherial.narcos.narcosdrugs.commands;

import me.matthewe.atherial.narcos.narcosdrugs.commands.drugpack.DrugPackCommand;
import me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands.*;
import me.matthewe.atherial.narcos.narcosdrugs.commands.subcommands.seed.SeedSubCommand;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.npc.commands.NarcosDrugsNPCCommand;
import net.atherial.api.plugin.command.spigot.HelpSubCommand;
import net.atherial.api.plugin.command.spigot.SpigotCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NarcosDrugsCommand extends SpigotCommand {
    public String label;

    public NarcosDrugsCommand(DrugHandler drugHandler) {
        super("narcosdrugs", "nd", "drugs");

        addSubCommand(new ReloadSubCommand(this));
        addSubCommand(new NarcosDrugsHelpSubCommand(this));
        addSubCommand(new SeedSubCommand(this));
        addSubCommand(new DrugPackCommand(this));
        addSubCommand(new NarcosDrugsNPCCommand(this));
        addSubCommand(new NarcosDrugsPricesSubCommand(this));
        addSubCommand(new NarcosDrugsLevelsSubCommand(this));
        addSubCommand(new NarcosDrugsGiveWandSubCommand(this));


    }
 
    @Override
    public void run(CommandSender sender, String[] args) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.label = label;
        return super.onCommand(sender, command, label, args);
    }

    public String getLabel() {
        return label;
    }

    public List<HelpSubCommand> getHelp(String[] args, Map<String, SpigotCommand> commandMap) {
        List<HelpSubCommand> helpList = new ArrayList<>();
        commandMap.values().forEach(spigotCommand -> helpList.addAll(spigotCommand.getHelp(args)));
        return helpList;
    }
    @Override
    public List<HelpSubCommand> getHelp(String[] args) {
        List<HelpSubCommand> helpList = new ArrayList<>();
        this.getSubCommandMap().values().forEach(spigotCommand -> helpList.addAll(spigotCommand.getHelp(args)));
        return helpList;
    }
}