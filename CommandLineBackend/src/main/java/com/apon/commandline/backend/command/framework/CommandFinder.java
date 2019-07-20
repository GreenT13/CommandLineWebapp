package com.apon.commandline.backend.command.framework;

import org.reflections.Reflections;

import java.util.Set;

/**
 * This class is responsible for finding all the ICommand's that should be installed.
 */
@SuppressWarnings("WeakerAccess")
public class CommandFinder {

    public Set<Class<? extends ICommand>> findAllCommands() {
        // For some reason I need to add "" to the prefix, otherwise it wont detect my classes.
        Reflections reflections = new Reflections("com.apon");
        return reflections.getSubTypesOf(ICommand.class);
    }
}
