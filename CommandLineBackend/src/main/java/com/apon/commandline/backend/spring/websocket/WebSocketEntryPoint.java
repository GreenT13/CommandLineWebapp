package com.apon.commandline.backend.spring.websocket;

import com.apon.commandline.backend.spring.websocket.coders.MessageDecoder;
import com.apon.commandline.backend.spring.websocket.coders.MessageEncoder;
import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.apon.commandline.backend.spring.websocket.command.CommandOutput;
import com.apon.commandline.backend.spring.websocket.command.CommandStatus;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint(value = "/webSocket", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketEntryPoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Open Connection ...");
        this.session = session;
    }

    @OnClose
    public void onClose() {
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public void onMessage(CommandInput commandInput) throws IOException, EncodeException {
        sendMessage("Message received");
        System.out.println(commandInput.commandArg);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendFinalMessage("Test");
    }

    @OnError
    public void onError(Throwable e) {
        e.printStackTrace();
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