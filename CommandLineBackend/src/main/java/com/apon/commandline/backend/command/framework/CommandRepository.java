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
 */
public enum CommandRepository {
    INSTANCE();

    @SuppressWarnings("FieldCanBeLocal")
    private Logger logger = LogManager.getLogger(CommandRepository.class);
    private CommandRepositoryFunctions commandRepositoryFunctions;

    CommandRepository() {
        commandRepositoryFunctions = new CommandRepositoryFunctions(logger);
        commandRepositoryFunctions.initialize("com.apon");
    }

    /**
     * Get an instance of the command with the identifier.
     * @param commandIdentifier The identifier.
     */
    @Nonnull
    public ICommand getCommandInstanceWithIdentifier(String commandIdentifier) throws CommandException {
        return commandRepositoryFunctions.getCommandInstanceWithIdentifier(commandIdentifier);
    }
}
