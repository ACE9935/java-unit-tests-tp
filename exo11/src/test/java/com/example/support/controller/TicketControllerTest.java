package com.example.support.controller;

import com.example.support.dto.TicketCreateRequest;
import com.example.support.exception.BusinessRuleException;
import com.example.support.exception.ResourceNotFoundException;
import com.example.support.model.Priority;
import com.example.support.model.Status;
import com.example.support.model.Ticket;
import com.example.support.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_Return201_When_TicketIsCreated() throws Exception {
        TicketCreateRequest request = new TicketCreateRequest();
        request.setTitle("Écran noir");
        request.setPriority(Priority.HIGH);

        Ticket mockCreated = new Ticket(1L, "Écran noir", Priority.HIGH, Status.OPEN);
        when(ticketService.createTicket(any(TicketCreateRequest.class))).thenReturn(mockCreated);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void should_Return400_When_TitleIsTooShort() throws Exception {
        TicketCreateRequest request = new TicketCreateRequest();
        request.setTitle("PC");
        request.setPriority(Priority.LOW);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void should_Return404_When_ResourceNotFound() throws Exception {
        when(ticketService.getTicketById(99L)).thenThrow(new ResourceNotFoundException("Inexistant"));

        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Inexistant"));
    }

    @Test
    void should_Return409_When_BusinessRuleViolated() throws Exception {
        when(ticketService.updateTicketStatus(eq(1L), any(Status.class)))
                .thenThrow(new BusinessRuleException("Transition interdite"));

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }
}