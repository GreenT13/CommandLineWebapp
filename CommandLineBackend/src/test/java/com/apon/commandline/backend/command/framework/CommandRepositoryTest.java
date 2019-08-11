package com.apon.commandline.backend.command.framework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Testing CommandRepository is not really possible. It would take a lot of reflection, which is terrible.
 * Any error in initializing the instance throws errors, so I hope you just see the stacktrace.
 *
 * Things I could not (easily) test.
 * <ul>
 *     <li>Having the same command leads to an error.</li>
 *     <li>Having an empty command leads to an error.</li>
 * </ul>
 */
@SuppressWarnings("WeakerAccess")
public class CommandRepositoryTest {

    /**
     * Happy flow!
     */
    @Test
    public void testCommandIsInstantiatedGivenIdentifier() throws CommandException {
        ICommand iCommand = CommandRepository.INSTANCE.getCommandInstanceWithIdentifier(TestCommand.TEST_COMMAND_IDENTIFIER);

        assertTrue("The instantiated command is not the correct command.", iCommand instanceof TestCommand);
    }

    @Test
    public void throwErrorWhenCommandIsNotFound() {
        assertThrows(CommandException.class, () ->
                CommandRepository.INSTANCE.getCommandInstanceWithIdentifier("thiscommanddoesnotexist"));
    }
}
