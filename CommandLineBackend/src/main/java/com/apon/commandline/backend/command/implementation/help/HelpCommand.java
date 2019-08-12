package com.apon.commandline.backend.command.implementation.help;

import com.apon.commandline.backend.command.MyUtil;
import com.apon.commandline.backend.command.framework.AbstractCommand;
import com.apon.commandline.backend.command.framework.CommandException;
import com.apon.commandline.backend.command.framework.CommandRepository;
import com.apon.commandline.backend.command.framework.ICommand;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;
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
    public void run(CommandInput commandInput) {
        terminalCommandHelper.sendMessage(run(commandInput.commandArg));
    }

    private String run(String command) {
        String commandToGetHelpFrom = determineCommandToGetHelpFrom(command);

        try {
            Class<? extends ICommand> commandClass = getClassForCommand(commandToGetHelpFrom);

            Optional<String> helpTextForCommand = getHelpTextForCommand(commandClass);
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

    private Class<? extends ICommand> getClassForCommand(String command) throws CommandException {
        return commandRepository.getCommandInstanceWithIdentifier(command).getClass();
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
