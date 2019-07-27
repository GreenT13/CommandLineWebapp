package com.apon.commandline.backend.spring.websocket.coders;

import com.apon.commandline.backend.spring.websocket.command.CommandInput;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<CommandInput> {
    private ObjectMapper objectMapper;

    @Override
    public CommandInput decode(String s) throws DecodeException {
        CommandInput commandInput;
        try {
            commandInput = objectMapper.readValue(s, CommandInput.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DecodeException(s, "Could not decode value.");
        }
        return commandInput;
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void destroy() {
        // Close resources (if any used)
    }
}