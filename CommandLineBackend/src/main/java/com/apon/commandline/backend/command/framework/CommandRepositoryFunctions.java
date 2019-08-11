package com.apon.commandline.backend.command.framework;

import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The CommandRepositoryFunctions is responsible for all the utility needed in {@link com.apon.commandline.backend.command.framework.CommandRepository}.
 * This class is created for testing and only used by CommandRepository (therefore package-private).
 */
class CommandRepositoryFunctions {
    private Logger logger;

    /**
     * Map where each key is the command identifier of the value.
     */
    private Map<String, Class<? extends ICommand>> commandClassMap;

    /**
     * See {@link com.apon.commandline.backend.command.framework.CommandRepositoryFunctions}.
     */
    CommandRepositoryFunctions(Logger logger) {
        this.logger = logger;
        this.commandClassMap = new HashMap<>();
    }

    /**
     * Get an instance of the command with the identifier.
     * @param commandIdentifier The identifier.
     */
    @Nonnull
    ICommand getCommandInstanceWithIdentifier(String commandIdentifier) throws CommandException {
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
     * Initialize the class for use, meaning we need to initialize the {@link CommandRepositoryFunctions#commandClassMap}.
     * @param packageName The name of the package containing al the commands.
     */
    @SuppressWarnings("SameParameterValue")
    void initialize(String packageName) {
        initializeCommandClassMap(packageName);
    }

    /**
     * Return an instance of the command class.
     *
     * @param iCommandClass The class.
     */
    private ICommand getInstanceFromClass(Class<? extends ICommand> iCommandClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends ICommand> constructor = iCommandClass.getConstructor();
        return constructor.newInstance();
    }

    /**
     * Initialize all the valid commands into the CommandClassMap.
     * @param packageName The package containing the commands.
     */
    private void initializeCommandClassMap(String packageName) {
        // Find all commands we need to initialize.
        Set<Class<? extends ICommand>> commands = findAllValidCommands(packageName);

        // Initialize the commands.
        for (Class<? extends ICommand> iCommandClass : commands) {
            try {
                initializeCommand(iCommandClass, commandClassMap);
            } catch (CommandException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                logger.error("Could not initialize command {}. Stacktrace: {}", iCommandClass.getName(), e);
            }
        }
    }

    /**
     * Store the command in the commandClassMap with the correct command identifier.
     *
     * @param iCommandClass The class.
     */
    private void initializeCommand(Class<? extends ICommand> iCommandClass, Map<String, Class<? extends ICommand>> commandClassMap)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CommandException {
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
     * Find all classes which satisfy ALL of the following:
     * <ul>
     *     <li>The class is a subclass of {@link com.apon.commandline.backend.command.framework.ICommand}.</li>
     *     <li>The class is instantiable with a constructor without parameters.</li>
     *     <li>The class is not abstract, static or an inner class.</li>
     *     <li>The class is contained in the given package.</li>
     * </ul>
     * @param packageName The package to scan.
     */
    private Set<Class<? extends ICommand>> findAllValidCommands(String packageName) {
        Set<Class<? extends ICommand>> commands = findAllCommands(packageName);

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
    private Set<Class<? extends ICommand>> findAllCommands(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(ICommand.class);
    }

    /**
     * Getter so we can test it.
     */
    Map<String, Class<? extends ICommand>> getCommandClassMap() {
        return commandClassMap;
    }
}
