package com.apon.commandline.backend.spring.websocket;

import javax.websocket.*;
import java.net.URI;

/**
 * Class that is needed for testing WebSockets.
 *
 * Based on https://stackoverflow.com/questions/26452903/javax-websocket-client-simple-example.
 */
@SuppressWarnings("WeakerAccess")
@ClientEndpoint
public class WebSocketClientEndpoint {

    private Session session = null;
    private MessageHandler messageHandler;

    public WebSocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the session which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        this.session = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the session which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.session = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * Register message handler.
     *
     * @param msgHandler The handler.
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message The message.
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     */
    public interface MessageHandler {
        void handleMessage(String message);
    }
}
