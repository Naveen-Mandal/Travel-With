package com.naveenmandal.TravelWith;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TravelWithApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Verifies that the application context boots up cleanly without database defects
    }

    @Test
    void security_ShouldRejectUnauthorizedAccessToProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/stations/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void auth_ShouldReturnBadRequest_WhenLoginPayloadIsMalformed() throws Exception {
        String invalidJsonPayload = "{ \"username\": \"\", \"password\": \"\" }";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJsonPayload))
                .andExpect(status().isBadRequest());
    }
}