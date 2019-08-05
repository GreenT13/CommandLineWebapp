package com.apon.commandline.backend.command.framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The CommandRepository responsible for creating instances of a valid subclass of ICommand based on a command identifier.
 * The list of all valid subclasses is created by {@link com.apon.commandline.backend.command.framework.CommandFinder}.
 */
public enum CommandRepository {
    INSTANCE();

    private Logger logger = LogManager.getLogger(CommandRepository.class);
    private Map<String, Class<? extends ICommand>> commandClassMap;

    CommandRepository() {
        this.commandClassMap = new HashMap<>();
        initialize();
    }

    /**
     * Get an instance of the command with the identifier.
     * @param commandIdentifier The identifier.
     */
    public Optional<ICommand> getCommandInstanceWithIdentifier(String commandIdentifier) {
        if (!commandClassMap.containsKey(commandIdentifier)) {
            return Optional.empty();
        }

        // If any errors occur when creating instance, we just log this instead of throwing the exception.
        // We already instantiated it earlier (when initializing), so I expect this happens almost never.
        try {
            ICommand iCommand = getInstanceFromClass(commandClassMap.get(commandIdentifier));
            return Optional.of(iCommand);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Could not initialize command {}. Stacktrace: {}", commandClassMap.get(commandIdentifier).getName(), e);
            return Optional.empty();
        }
    }

    /**
     * Initialize all commands and store them in memory.
     */
    private void initialize() {
        CommandFinder commandFinder = new CommandFinder();
        Set<Class<? extends ICommand>> commands = commandFinder.findAllValidCommandsInComApon();

        for (Class<? extends ICommand> iCommandClass : commands) {
            try {
                initializeCommand(iCommandClass);
            } catch (CommandException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                logger.error("Could not initialize command {}. Stacktrace: {}", iCommandClass.getName(), e);
            }
        }
    }

    /**
     * Store the command in the commandClassMap with the correct
     *
     * @param iCommandClass The class.
     */
    private void initializeCommand(Class<? extends ICommand> iCommandClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CommandException {
        String commandIdentifier = getInstanceFromClass(iCommandClass).getCommandIdentifier();

        if (commandClassMap.containsKey(commandIdentifier)) {
            throw new CommandException("There already exists a command with command identifier '" + commandIdentifier +
                    "', namely " + commandClassMap.get(commandIdentifier).getName());
        }

        commandClassMap.put(commandIdentifier, iCommandClass);
        logger.debug("Initialized command '{}' for class '{}'.", commandIdentifier, iCommandClass.getName());
    }

    /**
     * Return an instance of the class.
     *
     * @param iCommandClass The class.
     */
    private ICommand getInstanceFromClass(Class<? extends ICommand> iCommandClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends ICommand> constructor = iCommandClass.getConstructor();
        return constructor.newInstance();
    }
}
