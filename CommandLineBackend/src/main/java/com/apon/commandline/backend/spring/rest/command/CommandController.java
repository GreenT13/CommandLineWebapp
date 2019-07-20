package com.apon.commandline.backend.spring.rest.command;

import com.apon.commandline.backend.command.framework.CommandRepository;
import com.apon.commandline.backend.command.framework.CommandRunner;
import com.apon.commandline.backend.command.framework.ICommand;
import com.apon.commandline.backend.spring.database.PluginRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * The rest controller that makes the processing of commands available.
 */
@RestController
public class CommandController {

    private Logger logger = LogManager.getLogger(CommandController.class);
    private CommandRepository commandRepository = CommandRepository.INSTANCE;

    @PostMapping("/command")
    public CommandResponseValueObject executeCommand(@RequestBody CommandInputValueObject commandInputValueObject) {
        logger.debug("Executing command '{}'.", commandInputValueObject.commandArgs);

        String commandIdentifier = getCommandIdentifier(commandInputValueObject.commandArgs);
        Optional<ICommand> iCommand = commandRepository.getCommandInstanceWithIdentifier(commandIdentifier);
        if (iCommand.isEmpty()) {
            return new CommandResponseValueObject("Could not find command for identifier '" + commandIdentifier + "'.");
        }

        CommandRunner commandRunner = new CommandRunner();
        String output = commandRunner.runCommand(iCommand.get());

        return new CommandResponseValueObject(output);
    }

    private String getCommandIdentifier(String commandArgs) {
        if (!commandArgs.contains(" ")) {
            return commandArgs;
        }

        return commandArgs.substring(0, commandArgs.indexOf(" "));
    }

}
