package com.apon.commandline.backend.spring.websocket;

import com.apon.commandline.backend.spring.websocket.coders.MessageDecoder;
import com.apon.commandline.backend.spring.websocket.coders.MessageEncoder;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.spring.websocket.command.CommandOutput;
import com.apon.commandline.backend.spring.websocket.command.CommandStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@SuppressWarnings("unused")
@Component
@ServerEndpoint(value = "/webSocket", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketEntryPoint {
    private Logger logger = LogManager.getLogger(WebSocketEntryPoint.class);
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Open connection '{}'.", session.getId());
        this.session = session;
    }

    @OnClose
    public void onClose() {
        logger.info("Close connection '{}'.", session.getId());
    }

    @OnMessage
    public void onMessage(CommandInput commandInput) throws IOException, EncodeException {
        logger.info("Message received '{}'.", commandInput.commandArg);

        sendMessage("Message received. Going to wait for a second.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendFinalMessage("Finished.");
    }

    @OnError
    public void onError(Throwable e) {
        logger.error("An error occurred", e);
    }

    private void sendMessage(String message) throws IOException, EncodeException {
        CommandOutput commandOutput = new CommandOutput(message, CommandStatus.MESSAGE);
        session.getBasicRemote().sendObject(commandOutput);
    }

    private void sendFinalMessage(String message) throws IOException, EncodeException {
        CommandOutput commandOutput = new CommandOutput(message, CommandStatus.FINAL);
        session.getBasicRemote().sendObject(commandOutput);
    }
}