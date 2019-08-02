package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;

public class CommandRunner {
    public String runCommand(ICommand command, CommandInput commandInput) {
        if (commandInput.fileBase64 != null) {
            command.setFile(commandInput.fileBase64);
        }

        return command.run(commandInput.commandArg);
    }
}
