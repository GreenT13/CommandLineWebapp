package com.apon.commandline.backend.command.implementation.helloworld;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@SuppressWarnings("WeakerAccess")
public class HelloWorldCommandTest {
    private HelloWorldCommand helloWorldCommand = new HelloWorldCommand();
    private TerminalCommandHelper terminalCommandHelper = Mockito.mock(TerminalCommandHelper.class);

    @BeforeEach
    public void setUp() {
        helloWorldCommand.setTerminalCommandHelper(terminalCommandHelper);
    }

    @Test
    public void messageIsSent() {
        CommandInput commandInput = new CommandInput();
        commandInput.commandArg = helloWorldCommand.getCommandIdentifier();

        helloWorldCommand.run(commandInput);

        Mockito.verify(terminalCommandHelper).sendMessage(Mockito.anyString());
    }
}
