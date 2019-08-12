package com.apon.commandline.backend.spring.websocket;

import com.apon.commandline.backend.spring.websocket.coders.MessageDecoder;
import com.apon.commandline.backend.spring.websocket.coders.MessageEncoder;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.spring.websocket.command.CommandOutput;
import com.apon.commandline.backend.spring.websocket.command.CommandStatus;
import com.apon.commandline.backend.terminal.Terminal;
import com.apon.commandline.backend.terminal.TerminalSocketIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@SuppressWarnings("unused")
@Component
@ServerEndpoint(value = WebSocketEntryPoint.WEBSOCKET_URL, decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketEntryPoint {
    final static String WEBSOCKET_URL = "/webSocket";

    private Logger logger = LogManager.getLogger(WebSocketEntryPoint.class);
    private Session session;
    private Terminal terminal;

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Open connection '{}'.", session.getId());
        this.session = session;
        this.terminal = new Terminal(new TerminalSocketIO(session));
    }

    @OnClose
    public void onClose() {
        logger.info("Close connection '{}'.", session.getId());
    }

    @OnMessage
    public void onMessage(CommandInput commandInput) throws IOException, EncodeException {
        logger.info("Message received '{}'.", commandInput.commandArg);

        try {
            terminal.executeCommand(commandInput);
        } catch (Exception e) {
            sendFinalMessage(e.getMessage());
        }
    }

    @OnError
    public void onError(Throwable e) {
        logger.error("An error occurred", e);
    }

    private void sendFinalMessage(String message) throws IOException, EncodeException {
        CommandOutput commandOutput = new CommandOutput(message, CommandStatus.FINAL);
        session.getBasicRemote().sendObject(commandOutput);
    }

    private void sendFinalMessage() throws IOException, EncodeException {
        sendFinalMessage(null);
    }
}