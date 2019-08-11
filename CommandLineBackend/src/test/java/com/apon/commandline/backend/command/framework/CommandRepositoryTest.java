package com.apon.commandline.backend.command.framework;

import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Test class for {@link CommandRepository}.
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
}
