package com.apon.commandline.backend.command.implementation.help.nohelp;

import com.apon.commandline.backend.command.framework.AbstractCommand;

/**
 * Class for testing the help command.
 */
public class NoHelp extends AbstractCommand {
    @Override
    public String run(String command) {
        return "There is no help for this!";
    }

    @Override
    public String getCommandIdentifier() {
        return "nohelp";
    }
}
