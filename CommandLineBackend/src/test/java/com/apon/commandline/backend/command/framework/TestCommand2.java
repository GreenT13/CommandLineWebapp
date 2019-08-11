package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;

public class TestCommand2 implements ICommand {
    static String TEST_COMMAND_IDENTIFIER_2 = "testcommand_2";

    @Override
    public void run(CommandInput commandInput) {

    }

    @Override
    public String getCommandIdentifier() {
        return TEST_COMMAND_IDENTIFIER_2;
    }

    @Override
    public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) {

    }
}
