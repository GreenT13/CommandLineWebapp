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
     * Find all classes which satisfy ALL of the following:
     * <ul>
     *     <li>The class is instantiable with a constructor without parameters.</li>
     *     <li>The class is not abstract, static or an inner class.</li>
     *     <li>The class is contained in the package "com.apon".</li>
     * </ul>
     *
     * The package "com.apon" is used because this drastically reduces reflection time.
     */
    public Set<Class<? extends ICommand>> findAllValidCommandsInComApon() {
        Set<Class<? extends ICommand>> commands = findAllCommands("com.apon");

        // Filter out all the abstract, static, inner classes and check that the constructor has no parameters.
        return commands.stream()
                .filter(commandClass -> !Modifier.isAbstract(commandClass.getModifiers()))
                .filter(commandClass -> !Modifier.isStatic(commandClass.getModifiers()))
                .filter(commandClass -> commandClass.getEnclosingClass() == null)
                .filter(this::classHasNoParamConstructor)
                .collect(Collectors.toSet());
    }

    /**
     * Determine whether the class has a constructor that can be called without parameters.
     * @param clazz The class.
     */
    private boolean classHasNoParamConstructor(Class<?> clazz) {
        try {
            clazz.getConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
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
