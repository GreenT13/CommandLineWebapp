package com.apon.commandline.backend.terminal;

import com.apon.commandline.backend.command.framework.CommandException;
import com.apon.commandline.backend.command.framework.CommandRepository;
import com.apon.commandline.backend.command.framework.ICommand;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * The terminal object is responsible for running the correct command in a separate thread.
 */
public class Terminal {
    private Logger logger = LogManager.getLogger(Terminal.class);
    private CommandRepository commandRepository = CommandRepository.INSTANCE;
    private TerminalSocketIO terminalSocketIO;
    private TerminalCommandHelper terminalCommandHelper;

    public Terminal(TerminalSocketIO terminalSocketIO) {
        this.terminalSocketIO = terminalSocketIO;
        terminalCommandHelper = new TerminalCommandHelper(terminalSocketIO);
    }

    public void executeCommand(CommandInput commandInput) throws CommandException {
        logger.debug("Executing first command '{}'.", commandInput.commandArg);

        // Get an instance of the correct command.
        String commandIdentifier = getCommandIdentifier(commandInput.commandArg);
        ICommand iCommand = commandRepository.getCommandInstanceWithIdentifier(commandIdentifier);

        // Run the command.
        iCommand.setTerminalCommandHelper(terminalCommandHelper);
        iCommand.run(commandInput);

        // Send final message because we are done.
        terminalSocketIO.sendFinalMessage();
    }

    private String getCommandIdentifier(String commandArgs) {
        if (!commandArgs.contains(" ")) {
            return commandArgs;
        }

        return commandArgs.substring(0, commandArgs.indexOf(" "));
    }

}
