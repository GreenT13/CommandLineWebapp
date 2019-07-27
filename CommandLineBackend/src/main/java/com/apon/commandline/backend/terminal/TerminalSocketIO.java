package com.apon.commandline.backend.terminal;

import com.apon.commandline.backend.spring.websocket.command.CommandOutput;
import com.apon.commandline.backend.spring.websocket.command.CommandStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class TerminalSocketIO {
    private Logger logger = LogManager.getLogger(Terminal.class);
    private Session session;

    public TerminalSocketIO(Session session) {
        this.session = session;
    }

    public void sendMessage(String message) {
        try {
            CommandOutput commandOutput = new CommandOutput(message, CommandStatus.MESSAGE);
            session.getBasicRemote().sendObject(commandOutput);
        } catch (IOException | EncodeException e) {
            logger.error("Failed to send message.", e);
        }
    }
}
