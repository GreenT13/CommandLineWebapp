package com.apon.commandline.backend.spring.websocket.coders;

import com.apon.commandline.backend.spring.websocket.command.CommandOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<CommandOutput> {

    private ObjectMapper objectMapper;

    @Override
    public String encode(CommandOutput commandOutput) throws EncodeException {
        try {
            return objectMapper.writeValueAsString(commandOutput);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new EncodeException(commandOutput.message, "Could not encode value.");
        }
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