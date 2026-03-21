# DATA PERSISTENCE

What would actually prevent recurrence:
- CI/CD gate: Run bootRun with ddl-auto: none in CI before allowing merges — this catches it automatically
- Code review checklist: Require @Table and @Column annotations to be explicitly checked against Flyway migrations
- Lint rule: Warn when a new @Entity class lacks @Table, or when @Embedded AuditFields is added without a corresponding migration check
Want me to set up a CI check for this? It would be a simple script that starts the app with ddl-auto: none and verifies it stays up for a few seconds — catching exactly this class of mismatch.
