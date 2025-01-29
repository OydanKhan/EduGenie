package com.usyd.edugenie.OpenAIMss;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatGPTResponseTest {

    @Test
    void testChatGPTResponseConstructorAndGetters() {
        // Create a Message object
        Message message1 = new Message("user", "Tell me about Java.");
        Message message2 = new Message("assistant", "Java is a programming language.");

        // Create Choice objects
        ChatGPTResponse.Choice choice1 = new ChatGPTResponse.Choice(0, message1);
        ChatGPTResponse.Choice choice2 = new ChatGPTResponse.Choice(1, message2);

        // Create a list of choices
        List<ChatGPTResponse.Choice> choices = Arrays.asList(choice1, choice2);

        // Create ChatGPTResponse object using the constructor
        ChatGPTResponse response = new ChatGPTResponse(choices);

        // Verify the values set by the constructor
        assertNotNull(response.getChoices());
        assertEquals(2, response.getChoices().size());
        assertEquals(0, response.getChoices().get(0).getIndex());
        assertEquals("Tell me about Java.", response.getChoices().get(0).getMessage().getContent());
        assertEquals(1, response.getChoices().get(1).getIndex());
        assertEquals("Java is a programming language.", response.getChoices().get(1).getMessage().getContent());
    }

    @Test
    void testSettersAndGetters() {
        // Create Message objects
        Message message1 = new Message("user", "What is machine learning?");
        Message message2 = new Message("assistant", "Machine learning is a subset of AI.");

        // Create Choice objects
        ChatGPTResponse.Choice choice1 = new ChatGPTResponse.Choice(0, message1);
        ChatGPTResponse.Choice choice2 = new ChatGPTResponse.Choice(1, message2);

        // Create ChatGPTResponse object and set choices
        ChatGPTResponse response = new ChatGPTResponse();
        response.setChoices(Arrays.asList(choice1, choice2));

        // Verify the values set using the setters
        assertNotNull(response.getChoices());
        assertEquals(2, response.getChoices().size());
        assertEquals(0, response.getChoices().get(0).getIndex());
        assertEquals("What is machine learning?", response.getChoices().get(0).getMessage().getContent());
        assertEquals(1, response.getChoices().get(1).getIndex());
        assertEquals("Machine learning is a subset of AI.", response.getChoices().get(1).getMessage().getContent());
    }
}