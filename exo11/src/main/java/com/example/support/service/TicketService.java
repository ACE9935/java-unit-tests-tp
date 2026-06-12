package com.example.support.service;

import com.example.support.dto.TicketCreateRequest;
import com.example.support.exception.BusinessRuleException;
import com.example.support.exception.ResourceNotFoundException;
import com.example.support.model.Status;
import com.example.support.model.Ticket;
import com.example.support.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(TicketCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().length() < 3) {
            throw new BusinessRuleException("Le titre doit contenir au moins 3 caractères utiles.");
        }
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle().trim());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(Status.OPEN);
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Le ticket avec l'ID " + id + " n'existe pas."));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket updateTicketStatus(Long id, Status newStatus) {
        Ticket ticket = getTicketById(id);
        Status currentStatus = ticket.getStatus();

        if (currentStatus == Status.RESOLVED) {
            throw new BusinessRuleException("Un ticket déjà RESOLVED ne peut plus changer de statut.");
        }

        boolean isValidTransition = switch (currentStatus) {
            case OPEN -> newStatus == Status.IN_PROGRESS || newStatus == Status.RESOLVED || newStatus == Status.OPEN;
            case IN_PROGRESS -> newStatus == Status.RESOLVED || newStatus == Status.IN_PROGRESS;
            default -> false;
        };

        if (!isValidTransition) {
            throw new BusinessRuleException("Transition de statut non autorisée de " + currentStatus + " vers " + newStatus);
        }

        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket);
    }
}