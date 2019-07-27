package com.apon.commandline.backend.spring.websocket.command;

public class CommandOutput {
    public String message;
    public CommandStatus commandStatus;

    public CommandOutput() {
        // This constructor needs to exist for the ObjectMapper to work.
    }

    public CommandOutput(String message) {
        this(message, CommandStatus.MESSAGE);
    }

    public CommandOutput(String message, CommandStatus commandStatus) {
        this.message = message;
        this.commandStatus = commandStatus;
    }
}
