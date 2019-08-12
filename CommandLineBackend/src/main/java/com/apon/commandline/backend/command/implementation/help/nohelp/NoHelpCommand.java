package com.apon.commandline.backend.command.implementation.help.nohelp;

import com.apon.commandline.backend.command.framework.AbstractCommand;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;

/**
 * Class for testing the help command.
 */
public class NoHelpCommand extends AbstractCommand {

    @Override
    public void run(CommandInput commandInput) {
        terminalCommandHelper.sendMessage("There is no help for this!");
    }

    @Override
    public String getCommandIdentifier() {
        return "nohelp";
    }
}
