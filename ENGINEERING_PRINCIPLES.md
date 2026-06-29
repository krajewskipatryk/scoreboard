# ENGINEERING_PRINCIPLES.md

# Purpose

This document defines the engineering principles that guide the implementation of this project.

It is intentionally independent of the project requirements and should not prescribe architectural decisions or implementation details.

Instead, it defines **how engineering decisions should be made**.

The project requirements remain the source of truth for functional behavior.

---

# Core Philosophy

Prefer software that is:

* simple,
* explicit,
* maintainable,
* easy to reason about,
* easy to test,
* easy to evolve.

Every abstraction should justify its existence.

Patterns are tools, not goals.

---

# Decision Making Process

For every non-trivial engineering decision follow this process:

1. Understand the requirement.
2. Identify ambiguities.
3. Make assumptions explicit.
4. Explore realistic alternatives.
5. Compare trade-offs.
6. Select the simplest solution that satisfies the current requirements.
7. Validate the decision through tests.
8. Document the decision.

Do not silently make assumptions.

---

# Requirements Before Architecture

Requirements always have higher priority than architectural purity.

When there is a conflict between:

* implementing a design pattern,
* following a preferred architecture,
* satisfying the actual requirements,

prefer satisfying the requirements.

Architecture should support the problem, not become the problem.

---

# Simplicity First

Prefer the simplest solution that:

* satisfies the current requirements,
* keeps the code understandable,
* keeps future changes reasonably inexpensive.

Do not introduce complexity only because it might become useful later.

Future extensibility is valuable only when the additional complexity is proportional to its expected benefit.

---

# Explicit Assumptions

Whenever the requirements are ambiguous:

1. Do not silently choose one interpretation.
2. Document the assumption.
3. Cover it with tests.
4. Record it in the project documentation.

Assumptions are engineering decisions.

Treat them accordingly.

---

# Evaluate Alternatives

Before introducing a new abstraction, pattern or component, answer:

* What problem does it solve today?
* What is the simplest alternative?
* What is the cost of introducing it?
* What is gained?
* What is lost?

If the abstraction only solves hypothetical future problems, do not introduce it.

---

# Public API

Treat the public API as more stable than the implementation.

Internal implementation may evolve.

Public contracts should change only when justified.

Design the public API from the perspective of library consumers, not implementation convenience.

---

# Encapsulation

Keep business rules close to the data they protect.

Avoid leaking internal implementation details through the public API.

Do not expose mutable internal state unless there is a strong reason.

---

# Testing Philosophy

Tests describe behaviour.

Tests should:

* express requirements,
* protect assumptions,
* document edge cases,
* enable safe refactoring.

Avoid testing implementation details.

---

# Test-Driven Development

Follow short TDD cycles.

```
Red
↓

Green
↓

Refactor
```

Rules:

* One behaviour at a time.
* Small iterations.
* Minimal implementation.
* Refactor only when tests pass.
* Never commit failing tests.

---

# Engineering Decisions

For every important decision create a short rationale using the following format:

```
Decision

Reason

Alternatives considered

Trade-offs
```

Focus on engineering reasoning rather than implementation details.

---

# AI Collaboration

AI acts as a senior engineering partner.

AI should:

* challenge assumptions,
* propose alternatives,
* explain trade-offs,
* identify unnecessary complexity,
* review code,
* help refine documentation.

AI should not:

* automatically agree,
* silently introduce architecture,
* optimize for hypothetical future requirements,
* generate unnecessary abstractions.

---

# AI Review Process

When discussing a design decision, AI should follow this process:

1. Restate the problem.
2. Identify missing information.
3. Present at least two realistic alternatives.
4. Compare trade-offs.
5. Recommend one solution.
6. Explain why the alternatives were rejected.
7. State confidence in the recommendation.

If there is not enough information to make a good recommendation, AI should explicitly say so instead of guessing.

---

# Confidence Levels

When appropriate, classify recommendations as:

* High confidence
* Medium confidence
* Low confidence

Explain what information would increase confidence.

---

# Stop Exploring

Continue exploring alternatives only while they provide meaningful value.

Stop when:

* additional complexity outweighs the benefit,
* alternatives solve only hypothetical future problems,
* requirements no longer justify additional analysis.

Avoid analysis paralysis.

---

# Continuous Review

Treat every architectural decision as revisitable.

If new requirements appear, previous decisions may become invalid.

Changing a decision is acceptable when supported by new information.

---

# Final Principle

Good engineering is not about choosing the most sophisticated solution.

It is about making well-reasoned decisions that solve today's problem while keeping tomorrow's changes reasonably inexpensive.
