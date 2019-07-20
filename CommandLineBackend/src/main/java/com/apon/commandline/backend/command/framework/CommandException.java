package com.apon.commandline.backend.command.framework;


@SuppressWarnings("WeakerAccess")
public class CommandException extends Exception {

    public CommandException(String message) {
        super(message);
    }
}
