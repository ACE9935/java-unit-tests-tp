package com.example.support.integration;

import com.example.support.dto.StatusUpdateRequest;
import com.example.support.dto.TicketCreateRequest;
import com.example.support.model.Priority;
import com.example.support.model.Status;
import com.example.support.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicketIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        ticketRepository.clear();
    }

    @Test
    void should_Perform_Full_Ticket_Lifecycle_Integration() throws Exception {
        // 1. Créer un ticket via POST
        TicketCreateRequest create = new TicketCreateRequest();
        create.setTitle("Panne Imprimante");
        create.setPriority(Priority.LOW);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("OPEN"));

        // 2. Récupérer le ticket via GET
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Panne Imprimante"));

        // 3. Modifier son statut via PATCH
        StatusUpdateRequest update = new StatusUpdateRequest();
        update.setStatus(Status.IN_PROGRESS);

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
}