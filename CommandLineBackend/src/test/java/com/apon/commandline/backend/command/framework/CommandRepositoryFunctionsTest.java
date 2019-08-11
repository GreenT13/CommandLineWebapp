package com.apon.commandline.backend.command.framework;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.terminal.TerminalCommandHelper;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for {@link CommandRepositoryFunctions}.
 */
@SuppressWarnings("WeakerAccess")
public class CommandRepositoryFunctionsTest {

    private Map<String, Class<? extends ICommand>> commandClassMap;
    private CommandRepositoryFunctions commandRepositoryFunctions;

    @BeforeEach
    public void initialize() {
        commandRepositoryFunctions = new CommandRepositoryFunctions(LogManager.getLogger(CommandRepositoryFunctionsTest.class));
        commandRepositoryFunctions.initialize("com.apon.commandline.backend.command.framework");
        commandClassMap = commandRepositoryFunctions.getCommandClassMap();
    }

    /**
     * Happy flow!
     */
    @Test
    public void testCommandClassIsFound() {
        assertTrue(commandClassMap.values().contains(TestCommand.class), "The class 'TestCommand' is not found.");
        assertEquals(commandClassMap.get(TestCommand.TEST_COMMAND_IDENTIFIER), TestCommand.class,
                "The commandClassMap does not correctly map the test command to the command class.");
    }

    @Test
    public void testCommandWithoutNoParamConstructorIsNotFound() {
        assertFalse(commandClassMap.values().contains(ParamConstructorTestCommand.class),
                "Every found class should have a constructor that takes no parameters.");
    }

    @Test
    public void testAbstractCommandClassesAreNotFound() {
        assertFalse(commandClassMap.values().contains(AbstractTestCommand.class), "Abstract classes should not be found.");
        // This test case is double (also in testInnerCommandClassesAreNotFound), but it belongs in both.
        assertFalse(commandClassMap.values().contains(InnerAbstractTestCommand.class), "Inner abstract classes should not be found.");
    }

    @Test
    public void testInnerCommandClassesAreNotFound() {
        assertFalse(commandClassMap.values().contains(InnerAbstractTestCommand.class), "Inner classes should not be found.");
        assertFalse(commandClassMap.values().contains(InnerAbstractTestCommand.class), "Inner abstract classes should not be found.");
        assertFalse(commandClassMap.values().contains(InnerAbstractTestCommand.class), "Inner static classes should not be found.");
    }

    @Test
    public void throwErrorWhenCommandIsNotFound() {
        assertThrows(CommandException.class, () ->
                commandRepositoryFunctions.getCommandInstanceWithIdentifier("thiscommanddoesnotexist"));
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
