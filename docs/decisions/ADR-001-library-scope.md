Decision:
Implement the project as a plain Java Maven library without Spring Boot, REST API, database access or application runtime configuration.

Reason:
The exercise asks for a simple Java package/library. No persistence, HTTP API or standalone runtime is required.

Alternatives considered:
- Spring Boot REST service
- Library with persistence layer
- Fully hexagonal application skeleton

Trade-offs:
The library is simpler, framework-independent and easier to test. Consumers must instantiate or wire it themselves.
