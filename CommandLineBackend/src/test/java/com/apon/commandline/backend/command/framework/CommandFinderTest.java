package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Test class for {@link com.apon.commandline.backend.command.framework.CommandFinder}.
 */
public class CommandFinderTest {

    private Set<Class<? extends ICommand>> foundCommands;

    @Before
    public void initialize() {
        CommandFinder commandFinder = new CommandFinder();
        foundCommands = commandFinder.findAllValidCommandsInComApon();
    }

    @Test
    public void testCommandClassIsFound() {
        assertTrue("The class 'TestCommand' should be found." , foundCommands.contains(TestCommand.class));
    }

    @Test
    public void testAbstractCommandClassesAreNotFound() {
        assertFalse("Abstract classes should not be found." , foundCommands.contains(AbstractTestCommand.class));
        // This test case is double (also in testInnerCommandClassesAreNotFound), but it belongs in both.
        assertFalse("Inner abstract classes should not be found." , foundCommands.contains(InnerAbstractTestCommand.class));
    }

    @Test
    public void testInnerCommandClassesAreNotFound() {
        assertFalse("Inner classes should not be found." , foundCommands.contains(InnerAbstractTestCommand.class));
        assertFalse("Inner abstract classes should not be found." , foundCommands.contains(InnerAbstractTestCommand.class));
        assertFalse("Inner static classes should not be found." , foundCommands.contains(InnerAbstractTestCommand.class));
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
