// TicketCreateRequest.java
package com.example.support.dto;

import com.example.support.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketCreateRequest {
    @NotBlank(message = "Le titre est obligatoire.")
    @Size(min = 3, message = "Le titre doit contenir au moins 3 caractères.")
    private String title;

    @NotNull(message = "La priorité est obligatoire.")
    private Priority priority;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
}
