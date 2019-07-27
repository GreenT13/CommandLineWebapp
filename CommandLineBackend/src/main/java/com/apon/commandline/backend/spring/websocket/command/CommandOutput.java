package com.apon.commandline.backend.spring.websocket.command;

public class CommandOutput {
    public String message;
    public CommandStatus commandStatus;

    public CommandOutput(String message) {
        this(message, CommandStatus.MESSAGE);
    }

    public CommandOutput(String message, CommandStatus commandStatus) {
        this.message = message;
        this.commandStatus = commandStatus;
    }
}
