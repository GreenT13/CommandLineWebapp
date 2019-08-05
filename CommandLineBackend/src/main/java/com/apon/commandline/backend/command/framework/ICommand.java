package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;

/**
 * Interface to implement for adding commands to the program.
 *
 * <br /><br /><strong>Initializing</strong><br />
 * All commands are stored inside the {@link CommandRepository}. Note that the value of {@link ICommand#getCommandIdentifier()}
 * must be unique and not empty! If it is, errors will occur when starting up the application.
 *
 * <br /><br /><strong>Executing</strong><br />
 * When executing a command, the steps are:
 * <ul>
 *     <li>The command is instantiated from the class (constructor without parameters).</li>
 *     <li>The function {@link ICommand#setTerminalCommandHelper(TerminalCommandHelper)}</li> is called.</li>
 *     <li>The function {@link ICommand#run(CommandInput)}</li> is called with the input of the terminal.</li>
 * </ul>
 *
 * <strong>Abstract class</strong><br />
 * Instead of an interface, you can also choose to extend the {@link AbstractCommand}.
 */
public interface ICommand {

    /**
     * This function is called with the input of the terminal.
     *
     * See {@link ICommand} for more info.
     */
    void run(CommandInput commandInput);

    /**
     * This returns a unique identifier for the command.
     *
     * See {@link ICommand} for more info.
     */
    String getCommandIdentifier();

    /**
     * See {@link ICommand} for more info.
     */
    void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper);

}
