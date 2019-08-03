package com.apon.commandline.backend.command.implementation.help;

import com.apon.commandline.backend.command.MyUtil;
import com.apon.commandline.backend.command.framework.AbstractCommand;
import com.apon.commandline.backend.command.framework.CommandRepository;
import com.apon.commandline.backend.command.framework.ICommand;
import com.apon.commandline.backend.terminal.TerminalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * This command is for explaining the website for new people.
 */
public class HelpCommand extends AbstractCommand {
    private Logger logger = LogManager.getLogger(HelpCommand.class);
    private CommandRepository commandRepository = CommandRepository.INSTANCE;

    @Override
    public String run(String command) {
        String commandToGetHelpFrom = determineCommandToGetHelpFrom(command);

        try {
            Optional<Class<? extends ICommand>> classOptional = getClassForCommand(commandToGetHelpFrom);
            if (classOptional.isEmpty()) {
                return "Could not find the command " + commandToGetHelpFrom + ".";
            }

            Optional<String> helpTextForCommand = getHelpTextForCommand(classOptional.get());
            if (helpTextForCommand.isEmpty()) {
                return "The command does not have a help text.";
            }

            return helpTextForCommand.get();
        } catch (Exception e) {
            logger.error("An error occurred when executing '{}'.", command, e);
            return "Could not find help. Contact the administrator.";
        }
    }

    /**
     * Determine for which command to get help from, based on the executed command.
     *
     * For example, if you typed 'help x123 y456", this returns "x123".
     * @param command Executed command.
     */
    private String determineCommandToGetHelpFrom(String command) {
        // If no command was given, find the help for this command.
        if (!command.contains(" ")) {
            return this.getCommandIdentifier();
        }

        // Find the full string between the first and second space.
        // Add a string to the end so there always is a second space.
        int startIndex = command.indexOf(" ") + 1;
        int endIndex = (command.substring(command.indexOf(" ") + 1) + " ").indexOf(" ") + startIndex;

        return command.substring(startIndex, endIndex);
    }

    private Optional<Class<? extends ICommand>> getClassForCommand(String command) {
        Optional<ICommand> optionalICommand = commandRepository.getCommandInstanceWithIdentifier(command);
        if (optionalICommand.isEmpty()) {
            return Optional.empty();
        }
        ICommand iCommand = optionalICommand.get();

        return Optional.of(iCommand.getClass());
    }

    /**
     * Retrieve the help text from the given command class.
     *
     * Returns empty if the command help file could not be found.
     * @param commandClass The command.
     */
    private Optional<String> getHelpTextForCommand(Class<? extends ICommand> commandClass) throws IOException {
        try {
            InputStream inputStream = MyUtil.getCommandFile(commandClass, ".txt");
            return Optional.of(MyUtil.getContentOfFile(inputStream));
        } catch (TerminalException e) {
            return Optional.empty();
        }
    }

    @Override
    public String getCommandIdentifier() {
        return "help";
    }
}
