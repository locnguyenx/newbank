## List of reworked skill

1. **brainstorming** - apply BRD & BDD before Tech Design

2. **using-git-branches** - use my new skill to use git branch insteach of **using-git-worktrees** which creating worktree

3. **test-driven-development** - to be revised to enforce Test Strategy

## **Updated by superpowers:brainstorming skill:**

### **Persist the plan progress**
subagent-driven-development:
- Added "Progress Persistence" section with status markers [DONE]/[IN_PROGRESS]/[BLOCKED]/[PENDING]
- Plan file read now skips [DONE] tasks on resume
- Each task: mark [IN_PROGRESS] before dispatch, mark [DONE] after reviews pass
- Added to Red Flags: never skip plan file updates
executing-plans:
- Added same "Progress Persistence" section
- Step 1: skip [DONE] tasks, create TodoWrite from remaining
- Step 2: mark [IN_PROGRESS] before starting, [DONE] after verification
- Added to Remember: update plan file markers for every task
After a session interruption, you just open the plan file and see exactly where things stand.

**Instruction set:**
In a new session, tell the agent:
Resume the core infrastructure imp
The agent will:
1. Read the plan file
2. Skip all [DONE] tasks
3. See Task 1a.3 is [IN_PROGRESS]
4. Create TodoWrite with remaining tasks (24 tasks left)
5. Dispatch implementer for Task 1a.3
The plan file is the single source of truth — [DONE] markers survive sessions.

### Enforce superpowers skills to follow AGENTS.md rules

**Here's what changed:**
implementer-prompt.md:
- Added "Architecture Rules (REQUIRED)" section telling subagent to read AGENTS.md before coding
- Subagent must follow module boundary rules — if plan conflicts, report BLOCKED
code-quality-reviewer-prompt.md:
- Added architecture compliance check: reads AGENTS.md and verifies imports, JPA relationships, package structure, audit fields, error handling
subagent-driven-development/SKILL.md:
- Added "Architecture Enforcement" section documenting single-source-of-truth pattern
Now when you change architecture rules, you only edit AGENTS.md. Subagents read it at runtime. Want me to commit these changes?