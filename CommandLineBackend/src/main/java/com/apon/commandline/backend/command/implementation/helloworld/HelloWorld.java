package com.apon.commandline.backend.command.implementation.helloworld;

import com.apon.commandline.backend.command.framework.AbstractCommand;

/**
 * Basic command to test functionality.
 */
public class HelloWorld extends AbstractCommand {
    @Override
    public String run(String command) {
        return "Hello world!";
    }

    @Override
    public String getCommandIdentifier() {
        return "helloworld";
    }
}
