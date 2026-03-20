
# Draft BRD + BDD Prompt
Paste this into Claude Code as initial instruction or skill trigger for step-by-step execution:

```
Using business-analysis skill (or adapted brainstorming): Follow BRD -> BDD -> writing-plans -> TDD workflow for [PROJECT, e.g., "user authentication system"].

**PHASE 1: BRD**
1. Explore context: Review codebase/docs/commits.
2. Clarify business: Ask one question at a time (e.g., "Who are primary users? Business goals/KPIs? Non-functional reqs like security/scalability?").
3. Propose approaches: 2-3 options with tradeoffs (e.g., "Simple email login vs. OAuth").
4. Draft BRD sections:
   - Goal: [One-sentence business outcome].
   - Stakeholders: [List].
   - User Stories: As a [role], I want [feature] so [benefit].
   - Functional/Non-Functional Reqs.
   - Assumptions/Risks.
5. Get my approval per section. Save to docs/superpowers/brds/YYYY-MM-DD-login-brd.md. Dispatch reviewer subagent.

**PHASE 2: BDD from BRD**
1. Derive Gherkin scenarios (prioritize MVP):
   Feature: User Authentication
   Scenario: Successful login
     Given [state]
     When [action]
     Then [outcome]
   Cover happy path, errors, edge cases.
2. Present for approval. Save to docs/superpowers/specs/YYYY-MM-DD-login-bdd.md. Review loop.

**PHASE 3: Technical Design**
Invoke writing-plans: Break into TDD tasks (Red: write failing test; Green: minimal code; Refactor; Commit).

**PHASE 4: Implement**
Use subagent-driven-development with test-driven-development skill.
```
