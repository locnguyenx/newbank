# AGENTS.md - Guidelines for Agentic Coding Agents

This document provides instructions and guidelines for AI coding agents working in this repository.

## Project Overview

Modern digital banking system for corporate and SME banking, focusing on business accounts, cash management, and trade finance.

## Essential Commands

### Project Setup
```bash
npm install
cp .env.example .env
```

### Development & Testing
```bash
npm run dev                    # Start development server
npm run build                  # Build for production
npm test                       # Run all tests
npm test -- path/to/file.test.ts  # Run single test file
npm test -- -t "test name"     # Run single test by name
```

### Linting & Formatting
```bash
npm run lint         # Check for errors
npm run lint:fix     # Auto-fix errors
npm run format       # Format with Prettier
npm run typecheck    # TypeScript checking
```

## Code Style Guidelines

### Imports
```typescript
import path from 'path';                          // 1. Node built-ins
import express from 'express';                    // 2. External packages
import { UserService } from '@/services/user';    // 3. Internal modules
import type { User } from '@/types/user.types';   // 4. Types
import { validate } from './validators';          // 5. Relative imports
```

### Naming Conventions
- **Files**: `kebab-case.ts`
- **Classes**: `UserService`
- **Functions/Variables**: `camelCase`
- **Constants**: `MAX_AMOUNT`
- **Interfaces**: PascalCase, no `I` prefix

### TypeScript Guidelines
```typescript
// Explicit return types
function calculateInterest(principal: number, rate: number): number {
  return principal * rate;
}

// Interfaces for object shapes
interface Account {
  id: string;
  balance: number;
  currency: string;
}

// Type aliases for unions
type AccountStatus = 'active' | 'frozen' | 'closed';
```

### Error Handling
```typescript
class BankingError extends Error {
  constructor(message: string, public code: string, public statusCode = 500) {
    super(message);
  }
}

class InsufficientFundsError extends BankingError {
  constructor(available: number, requested: number) {
    super(`Insufficient funds: ${available} available, ${requested} requested`,
      'INSUFFICIENT_FUNDS', 400);
  }
}
```

### Testing Conventions
```typescript
describe('UserService', () => {
  describe('createUser', () => {
    it('should create a user with valid data', async () => {
      const userData = { email: 'test@example.com', name: 'Test User' };
      const user = await userService.createUser(userData);
      expect(user.email).toBe(userData.email);
    });
  });
});
```

## Commit Message Format

Use conventional commits:
```
type(scope): description
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Examples:
- `feat(accounts): add multi-currency support`
- `fix(transactions): correct rounding error`

## Directory Structure

```
src/
├── config/           # Configuration
├── controllers/      # Request handlers
├── middleware/        # Express middleware
├── models/          # Database models
├── repositories/    # Data access
├── routes/          # API routes
├── services/        # Business logic
├── types/           # Type definitions
└── utils/           # Utilities

tests/
├── unit/            # Unit tests
├── integration/     # Integration tests
└── e2e/             # End-to-end tests
```

## Superpowers Skills

Before coding tasks, invoke relevant skills:
- **brainstorming** - Design decisions
- **writing-plans** - Implementation plans
- **test-driven-development** - Write tests first
- **systematic-debugging** - Bug investigation

Use `skill` tool to load skills when needed.

## Banking-Specific Guidelines

### Money Handling
- Use integer cents (minor units) internally
- Never use floating point for amounts
- Store currency code with every value
- Use banker's rounding for conversions

### Transactions
- All financial operations must be atomic
- Implement idempotency keys
- Log all transactions with audit trail
- Support transaction rollback

### Security & Compliance
- Encrypt sensitive data at rest
- Use parameterized queries
- Implement rate limiting
- Maintain immutable audit logs
- Support regulatory data export