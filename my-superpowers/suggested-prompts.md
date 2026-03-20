
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
# Improve the skills
## brainstorming

1. The user wants traceability between BRD requirements (both functional requirements and user stories) and BDD scenarios

   AGENT: Let me think about the best approach. I should:
   1. First update the BRD with requirement IDs
   2. Then update the BDD to tag each scenario with the requirement ID(s) it covers
   3. Maybe add a traceability matrix at the end

2. BDD scenarios must be atomic - one scenario should map to one requirement ID, not multiple

   AGENT: 
   I'm realizing that user stories and functional requirements might serve different purposes in the traceability matrix. User stories capture the business need, while FRs define the system specification. 
   A single BDD scenario could reasonably trace back to one user story and one or more FRs if those FRs are all part of the same requirement cluster. But the user wants atomicity—one behavior per scenario—so I should restructure both the BRD with US IDs and then rebuild the BDD with truly single-responsibility scenarios. I should organize BDD scenarios by user stories to keep things clean and maintainable. Each scenario gets tagged with its corresponding user story, and I'll add a traceability matrix that maps stories to their functional requirements and related scenarios.

3. Classify BDD scenarios to: Happy path (optimal user flow) , Edge case (boundary, invalid, or extreme scenarios)