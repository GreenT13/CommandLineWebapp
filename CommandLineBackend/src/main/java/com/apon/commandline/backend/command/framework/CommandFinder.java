package com.apon.commandline.backend.command.framework;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The CommandFinder is responsible for finding all the ICommand classes (using reflection) that should be registered.
 * The list of subclasses is processed by {@link com.apon.commandline.backend.command.framework.CommandRepository}.
 */
@SuppressWarnings("WeakerAccess")
public class CommandFinder {

    /**
     * Find all classes that are a subtype of ICommand and are contained in the package "com.apon".
     */
    public Set<Class<? extends ICommand>> findAllValidCommandsInComApon() {
        Set<Class<? extends ICommand>> commands = findAllCommands("com.apon");

        // Filter out all the abstract, static and inner classes.
        return commands.stream()
                .filter(commandClass -> !Modifier.isAbstract(commandClass.getModifiers()))
                .filter(commandClass -> !Modifier.isStatic(commandClass.getModifiers()))
                .filter(commandClass -> commandClass.getEnclosingClass() == null)
                .collect(Collectors.toSet());
    }

    /**
     * Find all subclasses of ICommand in a given package.
     * @param packageName The full package name.
     */
    @SuppressWarnings("SameParameterValue")
    private Set<Class<? extends ICommand>> findAllCommands(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(ICommand.class);
    }
}
