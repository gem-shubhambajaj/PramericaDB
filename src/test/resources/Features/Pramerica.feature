@run
Feature: Data Testing

  @test
  Scenario Outline: Validating Information from database and UI
    When Login in the application with "<username>" and "<password>"
    And Click on Side Menu and go to profile
    Then Validate details from "<agentCode>"
    Examples:
      | agentCode | username | password |
      | 70028763  | 70028763 | 420420   |
      | 70028420  | 70028763 | 420420   |

  @test2
  Scenario Outline: Fetching Information from Database
    Then Get information from database with "<limit>"
    Examples:
      | limit |
      | 3     |

  @test3
  Scenario: Run generic functions
    Then run generic