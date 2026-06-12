package com.example.support.service;

import com.example.support.dto.TicketCreateRequest;
import com.example.support.exception.BusinessRuleException;
import com.example.support.exception.ResourceNotFoundException;
import com.example.support.model.Priority;
import com.example.support.model.Status;
import com.example.support.model.Ticket;
import com.example.support.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new TicketService(ticketRepository);
    }

    @Test
    void should_CreateTicketSuccessfully_WithDefaultOpenStatus() {
        // Arrange
        TicketCreateRequest request = new TicketCreateRequest();
        request.setTitle("Problème de connexion VPN");
        request.setPriority(Priority.HIGH);

        Ticket savedTicket = new Ticket(1L, "Problème de connexion VPN", Priority.HIGH, Status.OPEN);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        // Act
        Ticket result = ticketService.createTicket(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Status.OPEN, result.getStatus());
        assertEquals("Problème de connexion VPN", result.getTitle());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void should_ThrowException_When_TicketDoesNotExist() {
        // Arrange
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> ticketService.getTicketById(99L));
    }

    @Test
    void should_AllowValidTransitions_FromOpenToInProgress() {
        // Arrange
        Ticket baseTicket = new Ticket(1L, "Erreur 500", Priority.MEDIUM, Status.OPEN);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(baseTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket updated = ticketService.updateTicketStatus(1L, Status.IN_PROGRESS);

        // Assert
        assertEquals(Status.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void should_RefuseTransition_When_TicketIsAlreadyResolved() {
        // Arrange
        Ticket resolvedTicket = new Ticket(1L, "Demande d'accès", Priority.LOW, Status.RESOLVED);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(resolvedTicket));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> ticketService.updateTicketStatus(1L, Status.IN_PROGRESS));
    }
}