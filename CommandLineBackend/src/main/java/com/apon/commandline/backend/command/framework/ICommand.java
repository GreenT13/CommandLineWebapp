package com.apon.commandline.backend.command.framework;

public interface ICommand {

    String run(String command);

    String getCommandIdentifier();

}
