package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;

public class TestCommand implements ICommand {
    static final String TEST_COMMAND_IDENTIFIER = "testcommand";

    @Override
    public void run(CommandInput commandInput) {

    }

    @Override
    public String getCommandIdentifier() {
        return TEST_COMMAND_IDENTIFIER;
    }

    @Override
    public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) {

    }
}
