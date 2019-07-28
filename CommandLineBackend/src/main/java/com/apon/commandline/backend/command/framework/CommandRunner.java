package com.apon.commandline.backend.command.framework;

public class CommandRunner {
    public String runCommand(ICommand command, String typedCommand) {
        return command.run(typedCommand);
    }
}
