package com.apon.commandline.backend.terminal;

import com.apon.commandline.backend.command.framework.CommandRepository;
import com.apon.commandline.backend.command.framework.CommandRunner;
import com.apon.commandline.backend.command.framework.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Terminal {
    private Logger logger = LogManager.getLogger(Terminal.class);
    private CommandRepository commandRepository = CommandRepository.INSTANCE;

    private TerminalSocketIO terminalSocketIO;

    public Terminal(TerminalSocketIO terminalSocketIO) {
        this.terminalSocketIO = terminalSocketIO;
    }

    public void executeCommand(String command) throws TerminalException {
        logger.debug("Executing command '{}'.", command);

        String commandIdentifier = getCommandIdentifier(command);
        Optional<ICommand> iCommand = commandRepository.getCommandInstanceWithIdentifier(commandIdentifier);
        if (iCommand.isEmpty()) {
            throw new TerminalException("Could not find command for identifier '" + commandIdentifier + "'.");
        }

        CommandRunner commandRunner = new CommandRunner();
        terminalSocketIO.sendMessage(commandRunner.runCommand(iCommand.get()));
    }

    private String getCommandIdentifier(String commandArgs) {
        if (!commandArgs.contains(" ")) {
            return commandArgs;
        }

        return commandArgs.substring(0, commandArgs.indexOf(" "));
    }

}
