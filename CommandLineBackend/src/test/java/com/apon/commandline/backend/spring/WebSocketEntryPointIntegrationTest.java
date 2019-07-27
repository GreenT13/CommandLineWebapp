package com.apon.commandline.backend.spring;

import com.apon.commandline.backend.spring.websocket.WebSocketEntryPoint;
import com.apon.commandline.backend.spring.websocket.command.CommandOutput;
import com.apon.commandline.backend.spring.websocket.command.CommandStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Integration test created for testing WebSocket communication.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class WebSocketEntryPointIntegrationTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    private WebSocketClientEndpoint webSocketClientEndpoint;
    private List<CommandOutput> commandOutputs;

    @Before
    public void initialize() {
        commandOutputs = new ArrayList<>();

        URI uri = URI.create("ws://localhost:" + port + "/" + WebSocketEntryPoint.WEBSOCKET_URL);
        webSocketClientEndpoint = new WebSocketClientEndpoint(uri);

        // Add a listener that will add to the list.
        webSocketClientEndpoint.addMessageHandler(message -> {
            try {
                commandOutputs.add(objectMapper.readValue(message, CommandOutput.class));
            } catch (IOException e) {
                fail("Response JSON could not be converted to CommandOutput object");
            }
        });
    }

    @Test
    public void testThatCommandSendsFinalMessage() {
        webSocketClientEndpoint.sendMessage("{\"commandArg\":\"help\"}");

        long startTime = System.currentTimeMillis();
        while (true) {
            // Create a clone to prevent ConcurrentModificationException
            List<CommandOutput> commandOutputsClone = new ArrayList<>(commandOutputs);
            for (CommandOutput commandOutput : commandOutputsClone) {
                if (CommandStatus.FINAL.equals(commandOutput.commandStatus)) {
                    return;
                }
            }

            // If we wait more than 5 seconds, something is wrong.
            if (System.currentTimeMillis() - startTime > 5000) {
                fail("Final message was not received.");
                return;
            }
        }
    }

}
