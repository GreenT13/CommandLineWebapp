package com.apon.commandline.backend.command.framework;

public class CommandRunner {
    public String runCommand(ICommand command) {
        return command.run();
    }
}
