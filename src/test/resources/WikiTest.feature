Feature: Wiki test

  Scenario: Wiki test
    Given I am on Selenium wiki page
    Then I verify all external links work
    When I click on Oxygen in periodic table
    Then I verify navigation to Oxygen wiki page.
    And I verify it is a feature article
    And I take screenshot of properties of Oxygen
    And I count number of pdf links in references
    When I search for pluto
    Then I verify second suggestion is plutonium