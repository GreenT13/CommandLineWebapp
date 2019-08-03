package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;

public abstract class AbstractCommand implements ICommand {
    protected TerminalCommandHelper terminalCommandHelper;

    public void run(CommandInput commandInput) {
        terminalCommandHelper.sendMessage(run(commandInput.commandArg));
    }

    public abstract String run(String command);

    public abstract String getCommandIdentifier();

    @Override
    public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) {
        this.terminalCommandHelper = terminalCommandHelper;
    }
}
