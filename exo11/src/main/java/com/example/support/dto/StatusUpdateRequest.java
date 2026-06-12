// StatusUpdateRequest.java
package com.example.support.dto;

import com.example.support.model.Status;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    @NotNull(message = "Le statut est obligatoire.")
    private Status status;

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}