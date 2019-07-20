package com.apon.commandline.backend.command.implementation;

import com.apon.commandline.backend.command.framework.ICommand;

public class HelpCommand implements ICommand {
    @Override
    public String run() {
        return "You found the help!";
    }

    @Override
    public String getCommandIdentifier() {
        return "help";
    }
}
