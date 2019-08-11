package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Test class for {@link com.apon.commandline.backend.command.framework.CommandFinder}.
 */
@SuppressWarnings("WeakerAccess")
public class CommandFinderTest {

    private Set<Class<? extends ICommand>> foundCommands;

    @BeforeEach
    public void initialize() {
        CommandFinder commandFinder = new CommandFinder();
        foundCommands = commandFinder.findAllValidCommandsInComApon();
    }

    /**
     * Happy flow!
     */
    @Test
    public void testCommandClassIsFound() {
        assertTrue(foundCommands.contains(TestCommand.class), "The class 'TestCommand' should be found.");
    }

    @Test
    public void testCommandWithoutNoParamConstructorIsNotFound() {
        assertFalse(foundCommands.contains(ParamConstructorTestCommand.class),
                "Every found class should have a constructor that takes no parameters.");
    }

    @Test
    public void testAbstractCommandClassesAreNotFound() {
        assertFalse(foundCommands.contains(AbstractTestCommand.class), "Abstract classes should not be found.");
        // This test case is double (also in testInnerCommandClassesAreNotFound), but it belongs in both.
        assertFalse(foundCommands.contains(InnerAbstractTestCommand.class), "Inner abstract classes should not be found.");
    }

    @Test
    public void testInnerCommandClassesAreNotFound() {
        assertFalse(foundCommands.contains(InnerAbstractTestCommand.class), "Inner classes should not be found.");
        assertFalse(foundCommands.contains(InnerAbstractTestCommand.class), "Inner abstract classes should not be found.");
        assertFalse(foundCommands.contains(InnerAbstractTestCommand.class), "Inner static classes should not be found.");
    }

    class InnerTestCommand implements ICommand {
        @Override
        public void run(CommandInput commandInput) {

        }

        @Override
        public String getCommandIdentifier() {
            return null;
        }

        @Override
        public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) {

        }
    }

    abstract class InnerAbstractTestCommand implements ICommand { }

    static class InnerStaticTestCommand implements ICommand {
        @Override
        public void run(CommandInput commandInput) { }

        @Override
        public String getCommandIdentifier() { return null; }

        @Override
        public void setTerminalCommandHelper(TerminalCommandHelper terminalCommandHelper) { }
    }

}
