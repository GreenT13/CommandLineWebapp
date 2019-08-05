package com.apon.commandline.backend.command.implementation.helloworld;

import com.apon.commandline.backend.command.framework.AbstractCommand;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;

/**
 * Basic command to test functionality.
 */
public class HelloWorld extends AbstractCommand {

    @Override
    public void run(CommandInput commandInput) {
        terminalCommandHelper.sendMessage(run(commandInput.commandArg));
    }

    public String run(String command) {
        return "Hello world!";
    }

    @Override
    public String getCommandIdentifier() {
        return "helloworld";
    }
}
