package com.budgetly.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void register_validRequest_returnsToken() throws Exception {
        String body =
                """
                {
                    "email": "test@example.com",
                    "password": "password123",
                    "firstName": "Test",
                    "lastName": "User"
                }
                """;

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_afterRegister_returnsToken() throws Exception {
        String registerBody =
                """
                {
                    "email": "login-test@example.com",
                    "password": "password123",
                    "firstName": "Login",
                    "lastName": "Test"
                }
                """;

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody));

        String loginBody =
                """
                {
                    "email": "login-test@example.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void accessProtectedEndpoint_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/accounts")).andExpect(status().isUnauthorized());
    }

    @Test
    void accessProtectedEndpoint_withValidToken_returns200() throws Exception {
        String registerBody =
                """
                {
                    "email": "protected-test@example.com",
                    "password": "password123",
                    "firstName": "Protected",
                    "lastName": "Test"
                }
                """;

        MvcResult registerResult =
                mockMvc.perform(
                                post("/auth/register")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(registerBody))
                        .andReturn();

        JsonNode json = objectMapper.readTree(registerResult.getResponse().getContentAsString());
        String token = json.get("token").asText();

        mockMvc.perform(get("/accounts").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
