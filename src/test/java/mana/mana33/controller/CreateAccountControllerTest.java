package mana.mana33.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mana.mana33.domain.models.CreateAccountDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateAccountController.class)
class CreateAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnOkWhenCreatingAccountWithValidData() throws Exception {
        CreateAccountDTO dto = new CreateAccountDTO(
            "John",
            "Doe",
            "john.doe@example.com",
            "password123",
            "1234567890"
        );

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptPostRequest() throws Exception {
        CreateAccountDTO dto = new CreateAccountDTO(
            "Jane",
            "Smith",
            "jane.smith@example.com",
            "password456",
            "9876543210"
        );

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptJsonContentType() throws Exception {
        CreateAccountDTO dto = new CreateAccountDTO(
            "Bob",
            "Johnson",
            "bob.johnson@example.com",
            "password789",
            "5555555555"
        );

        String jsonContent = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    void shouldMapToApiAccountsEndpoint() throws Exception {
        CreateAccountDTO dto = new CreateAccountDTO(
            "Alice",
            "Williams",
            "alice.williams@example.com",
            "password",
            "1111111111"
        );

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
