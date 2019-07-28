package com.apon.commandline.backend.command.implementation.helloworld;

import com.apon.commandline.backend.command.framework.ICommand;

public class HelloWorld implements ICommand {
    @Override
    public String run(String command) {
        return "Hello world!";
    }

    @Override
    public String getCommandIdentifier() {
        return "helloworld";
    }
}
