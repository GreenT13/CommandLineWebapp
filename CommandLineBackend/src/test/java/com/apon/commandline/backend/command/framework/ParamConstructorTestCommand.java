package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;

@SuppressWarnings("unused")
public class ParamConstructorTestCommand implements ICommand {

    public ParamConstructorTestCommand(String test) { }

    @Override
    public void run(CommandInput commandInput) {

    }

    @Override
    public String getCommandIdentifier() {
        return null;
    }

    @Override
    public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) {

    }
}
