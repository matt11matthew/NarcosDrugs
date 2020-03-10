package me.matthewe.atherial.narcos.narcosdrugs.config;

import me.matthewe.atherial.narcos.narcosdrugs.NarcosDrugs;
import net.atherial.api.plugin.CommandMessages;

public class NarcosDrugsCommandMessages implements CommandMessages {
    private CommandConfig commandConfig;

    public NarcosDrugsCommandMessages(NarcosDrugs narcosDrugs) {
        this.commandConfig = new CommandConfig(narcosDrugs).load();
    }

    @Override
    public String getNoPermissionMessage() {
        return commandConfig.noPermission;
    }

    @Override
    public String getHelpArgumentsColor() {
        return commandConfig.helpArgumentsColor;
    }

    @Override
    public String getHelpLine() {
        return commandConfig.helpLine;
    }

    @Override
    public String getHelpHeader() {
        return commandConfig.helpHeader;
    }

    @Override
    public String getHelpFooter() {
        return commandConfig.helpFooter;
    }

    @Override
    public String getPlayerOnlyCommandMessage() {
        return commandConfig.playerOnlyCommand;
    }

    @Override
    public String getCorrectCommandArgumentUsage() {
        return commandConfig.correctCommandArgumentUsage;
    }

    @Override
    public String getCorrectCommandUsage() {
        return commandConfig.correctCommandUsage;
    }
}
