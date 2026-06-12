package com.example.support.bdd;

import com.example.support.dto.StatusUpdateRequest;
import com.example.support.dto.TicketCreateRequest;
import com.example.support.model.Priority;
import com.example.support.model.Status;
import com.example.support.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class TicketStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions latestResponse;
    private Long targetTicketId;

    @Before
    public void setup() {
        ticketRepository.clear();
    }

    @When("the user creates a ticket with title {string} and priority {string}")
    public void the_user_creates_a_ticket_with_title_and_priority(String title, String priority) throws Exception {
        TicketCreateRequest request = new TicketCreateRequest();
        request.setTitle(title);
        request.setPriority(Priority.valueOf(priority));

        latestResponse = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) throws Exception {
        latestResponse.andExpect(status().is(statusCode));
    }

    @Then("the ticket status should be {string}")
    public void the_ticket_status_should_be(String expectedStatus) throws Exception {
        latestResponse.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @Given("an existing ticket with status {string}")
    public void an_existing_ticket_with_status(String initialStatus) throws Exception {
        TicketCreateRequest create = new TicketCreateRequest();
        create.setTitle("Initial Default Title");
        create.setPriority(Priority.MEDIUM);

        String rawResponse = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andReturn().getResponse().getContentAsString();

        com.example.support.model.Ticket created = objectMapper.readValue(rawResponse, com.example.support.model.Ticket.class);
        targetTicketId = created.getId();

        if (!initialStatus.equals("OPEN")) {
            StatusUpdateRequest update = new StatusUpdateRequest();
            update.setStatus(Status.valueOf(initialStatus));
            mockMvc.perform(patch("/api/tickets/" + targetTicketId + "/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(update)));
        }
    }

    @When("the user updates the ticket status to {string}")
    @When("the user tries to update the ticket status to {string}")
    public void the_user_updates_the_ticket_status_to(String nextStatus) throws Exception {
        StatusUpdateRequest update = new StatusUpdateRequest();
        update.setStatus(Status.valueOf(nextStatus));

        latestResponse = mockMvc.perform(patch("/api/tickets/" + targetTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));
    }

    @When("the user requests a ticket with ID {int}")
    public void the_user_requests_a_ticket_with_id(int id) throws Exception {
        latestResponse = mockMvc.perform(get("/api/tickets/" + id));
    }
}