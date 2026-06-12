Feature: Management of Support Tickets

  Scenario: Successful creation of a valid support ticket
    When the user creates a ticket with title "Database Connection Timeout" and priority "HIGH"
    Then the response status code should be 201
    And the ticket status should be "OPEN"

  Scenario: Resolving an active support ticket
    Given an existing ticket with status "OPEN"
    When the user updates the ticket status to "RESOLVED"
    Then the response status code should be 200
    And the ticket status should be "RESOLVED"

  Scenario: Refusing modifications on an already resolved ticket
    Given an existing ticket with status "RESOLVED"
    When the user tries to update the ticket status to "IN_PROGRESS"
    Then the response status code should be 409

  Scenario: Requesting a non-existent ticket
    When the user requests a ticket with ID 999
    Then the response status code should be 404