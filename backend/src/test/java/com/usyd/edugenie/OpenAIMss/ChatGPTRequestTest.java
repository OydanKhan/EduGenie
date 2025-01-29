package com.usyd.edugenie.OpenAIMss;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPTRequestTest {

    @Test
    void testChatGPTRequestConstructorAndGetters() {
        // Create a ChatGPTRequest using the constructor
        String model = "gpt-3.5-turbo";
        String prompt = "Tell me about Java.";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        // Verify the values set by the constructor
        assertEquals(model, request.getModel());
        assertNotNull(request.getMessages());
        assertEquals(1, request.getMessages().size());

        // Verify the message content
        Message message = request.getMessages().get(0);
        assertEquals("user", message.getRole());
        assertEquals(prompt, message.getContent());
    }

    @Test
    void testSettersAndGetters() {
        // Create an empty ChatGPTRequest object
        ChatGPTRequest request = new ChatGPTRequest("gpt-4", "Explain AI.");

        // Change the model
        String newModel = "gpt-3.5";
        request.setModel(newModel);

        // Add a new message
        Message newMessage = new Message("user", "What is machine learning?");
        request.getMessages().add(newMessage);

        // Verify the updated values
        assertEquals(newModel, request.getModel());
        assertEquals(2, request.getMessages().size());
        assertEquals("What is machine learning?", request.getMessages().get(1).getContent());
    }
}