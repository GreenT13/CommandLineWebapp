package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;

public interface ICommand {

    void run(CommandInput commandInput);

    String getCommandIdentifier();

    void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper);

}
