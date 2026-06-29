Decision:
Use JUnit 5 with AssertJ and develop the implementation incrementally using TDD.

Reason:
JUnit 5 keeps the project fully Java-based and AssertJ improves assertion readability. TDD helps keep the implementation focused on the exercise requirements.

Alternatives considered:
- Spock
- Writing implementation first and tests afterwards

Trade-offs:
JUnit 5 is less expressive than Spock for BDD-style tests, but avoids introducing Groovy and extra build complexity.
