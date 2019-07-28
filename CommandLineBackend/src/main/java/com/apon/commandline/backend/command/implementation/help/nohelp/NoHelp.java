package com.apon.commandline.backend.command.implementation.help.nohelp;

import com.apon.commandline.backend.command.framework.ICommand;

public class NoHelp implements ICommand {
    @Override
    public String run(String command) {
        return "There is no help for this!";
    }

    @Override
    public String getCommandIdentifier() {
        return "nohelp";
    }
}
