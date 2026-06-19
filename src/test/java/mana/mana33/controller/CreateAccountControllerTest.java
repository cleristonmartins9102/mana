package mana.mana33.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mana.mana33.domain.CreateAccount;
import mana.mana33.domain.models.AccountModel;
import mana.mana33.domain.models.CreateAccountDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CreateAccountController.class)
class CreateAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateAccount createAccountUseCase;

    @Test
    void shouldReturnOkWhenCreatingAccountWithValidData() throws Exception {
        CreateAccountDTO dto = new CreateAccountDTO(
            "John",
            "Doe",
            "john.doe@example.com",
            "password123",
            "1234567890"
        );

        AccountModel mockAccount = new AccountModel(
            "123",
            "John",
            "Doe",
            "john.doe@example.com",
            "1234567890",
            "token",
            "refreshToken"
        );

        when(createAccountUseCase.create(any(CreateAccountDTO.class))).thenReturn(mockAccount);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("123"));
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

        AccountModel mockAccount = new AccountModel(
            "456",
            "Jane",
            "Smith",
            "jane.smith@example.com",
            "9876543210",
            "token",
            "refreshToken"
        );

        when(createAccountUseCase.create(any(CreateAccountDTO.class))).thenReturn(mockAccount);

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

        AccountModel mockAccount = new AccountModel(
            "789",
            "Bob",
            "Johnson",
            "bob.johnson@example.com",
            "5555555555",
            "token",
            "refreshToken"
        );

        when(createAccountUseCase.create(any(CreateAccountDTO.class))).thenReturn(mockAccount);

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

        AccountModel mockAccount = new AccountModel(
            "999",
            "Alice",
            "Williams",
            "alice.williams@example.com",
            "1111111111",
            "token",
            "refreshToken"
        );

        when(createAccountUseCase.create(any(CreateAccountDTO.class))).thenReturn(mockAccount);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAccountIdInResponseBody() throws Exception {
        CreateAccountDTO dto = new CreateAccountDTO(
            "Test",
            "User",
            "test@example.com",
            "password",
            "1234567890"
        );

        AccountModel mockAccount = new AccountModel(
            "account-id-123",
            "Test",
            "User",
            "test@example.com",
            "1234567890",
            "token",
            "refreshToken"
        );

        when(createAccountUseCase.create(any(CreateAccountDTO.class))).thenReturn(mockAccount);

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("account-id-123"));
    }
}
