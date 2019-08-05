package com.apon.commandline.backend.command.framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
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

    /**
     * Map where each key is the command identifier of the value.
     */
    private Map<String, Class<? extends ICommand>> commandClassMap;

    CommandRepository() {
        initializeCommandClassMap(findAllInstantiableCommands());
    }

    /**
     * Get an instance of the command with the identifier.
     * @param commandIdentifier The identifier.
     */
    @Nonnull
    public ICommand getCommandInstanceWithIdentifier(String commandIdentifier) throws CommandException {
        if (!commandClassMap.containsKey(commandIdentifier)) {
            throw new CommandException("Could not find command with command identifier '" + commandIdentifier + "'.");
        }

        // If any errors occur when creating instance, we just log this instead of throwing the exception.
        // We already instantiated it earlier (when initializing), so I expect this happens almost never.
        try {
            return getInstanceFromClass(commandClassMap.get(commandIdentifier));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error("Could not initialize command {}. Stacktrace: {}", commandClassMap.get(commandIdentifier).getName(), e);
            throw new CommandException("Could not instantiate command with command identifier '" + commandIdentifier + "'.");
        }
    }

    /**
     * Initialize all commands and store them in {@link CommandRepository#commandClassMap}.
     */
    private void initializeCommandClassMap(Set<Class<? extends ICommand>> commands) {
        this.commandClassMap = new HashMap<>();

        for (Class<? extends ICommand> iCommandClass : commands) {
            try {
                initializeCommand(iCommandClass);
            } catch (CommandException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                logger.error("Could not initialize command {}. Stacktrace: {}", iCommandClass.getName(), e);
            }
        }
    }

    /**
     * Find all instantiable commands. For more detail, see {@link CommandFinder#findAllValidCommandsInComApon()}.
     */
    private Set<Class<? extends ICommand>> findAllInstantiableCommands() {
        CommandFinder commandFinder = new CommandFinder();
        return commandFinder.findAllValidCommandsInComApon();
    }

    /**
     * Store the command in the commandClassMap with the correct command identifier.
     *
     * @param iCommandClass The class.
     */
    private void initializeCommand(Class<? extends ICommand> iCommandClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CommandException {
        // Retrieve the command identifier.
        String commandIdentifier = getInstanceFromClass(iCommandClass).getCommandIdentifier();

        if (StringUtils.isEmpty(commandIdentifier)) {
            throw new CommandException("The command identifier cannot be empty, see class " + commandClassMap.get(commandIdentifier).getName() + ".");
        }

        // If there is already a command with the same command identifier, we throw an error.
        if (commandClassMap.containsKey(commandIdentifier)) {
            throw new CommandException("There already exists a command with command identifier '" + commandIdentifier +
                    "', namely " + commandClassMap.get(commandIdentifier).getName() + ".");
        }

        // Add the command to the map.
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
