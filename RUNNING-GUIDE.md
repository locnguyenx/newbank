# RUN SYSTEM

## SETTING DB
Simplest way to run the system:

**Option 1:** If you have PostgreSQL installed locally

```bash
cd .worktrees/foundation
createdb customer_db          # create database
./gradlew bootRun             # start backend on :8080
# In another terminal
cd .worktrees/foundation/frontend
npm install && npm run dev    # start frontend on :5173
```

**Option 2:** If you have Docker
```bash
docker run -d --name customer-db \
  -e POSTGRES_DB=customer_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:16
cd .worktrees/foundation
./gradlew bootRun
# In another terminal
cd .worktrees/foundation/frontend
npm install && npm run dev
```

**Option 3:** Simplest — use H2 (no PostgreSQL needed)
Edit src/main/resources/application.yml to use H2 instead of PostgreSQL:
spring:
  datasource:
    url: jdbc:h2:mem:customerdb
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
  flyway:
    enabled: false
  h2:
    console:
      enabled: true
      path: /h2-console
server:
  port: 8080

Then:
```bash
./gradlew bootRun
# H2 console at http://localhost:8080/h2-console
```

Option 3 is the simplest — no external dependencies.

## System works. Here's how to run it:

### Run Backend
```bash
cd .worktrees/foundation
./gradlew bootRun
```

Wait for: Tomcat started on port 8080

### Run Frontend (optional)
In another terminal:

```bash
cd .worktrees/foundation/frontend
npm install
npm run dev
```

### Test the API

```bash
# Create corporate customer
curl -X POST http://localhost:8080/api/customers/corporate \
  -H "Content-Type: application/json" \
  -d '{"name":"Acme Corp","taxId":"12-3456789"}'
# Search customers
curl "http://localhost:8080/api/customers/search?name=Acme"
```

### H2 Console
open http://localhost:8080/h2-console
H2 Console settings:
- JDBC URL: jdbc:h2:mem:customerdb
- Username: sa
- Password: (empty)

## RUNNING SERVER WITH PostgreSQL

### 1. Start All Services (PostgreSQL + Kafka)
```bash
docker-compose up -d
# Or explicitly:
docker-compose up -d postgres zookeeper kafka
```
This starts:
- ✅ PostgreSQL on localhost:5432 (db: banking, user: banking, pass: banking)
- ✅ Zookeeper on localhost:2181
- ✅ Kafka on localhost:9092

**Start PostgreSQL Only (if you don't need Kafka)**
```bash
docker-compose up -d postgres
```
**Verify Services Running**
```bash
# Check all services
docker-compose ps
# Check logs
docker-compose logs -f postgres
docker-compose logs -f kafka
# Test PostgreSQL connection
docker-compose exec postgres psql -U banking -d banking -c "\dt"
# Test Kafka
docker-compose exec kafka kafka-topics --bootstrap-server localhost:9092 --list
```

**Stop Services**
```bash
# Stop but keep data
docker-compose stop
# Stop and remove containers (data preserved in volume)
docker-compose down
# Stop, remove containers AND delete data
docker-compose down -v
```

### 2. Run Application with PostgreSQL Profile
```bash
./gradlew bootRun --args='--spring.profiles.active=postgres'
# after changing PostgreSQL as main db for dev, we can use this
./gradlew bootRun
```
Or set the profile as default in IDE/Run configuration.

---

**Quick Start Script (optional, create start-postgres.sh):**
```bash
#!/bin/bash
docker-compose up -d postgres
echo "PostgreSQL started on localhost:5432"
echo "Database: banking, User: banking, Password: banking"
./gradlew bootRun --args='--spring.profiles.active=postgres'
```
Everything is ready - just run docker-compose up -d postgres and start the app with the postgres profile!

### Guide to Run Server with PostgreSQL (latest)

#### Option 1: Use a persistent PostgreSQL with seed data

This approach use persistent PostgreSQL with seed data. Newly created data wont be lost

1. Start PostgreSQL
```bash
# stop the running container (ephemeral)
docker stop test-postgres
# start persistent one
docker-compose up -d postgres
```
This creates:
- DB: testdb (from your existing container)
- User: testuser
- Password: testpass

2. Run Server
```bash
# run server and load seed data
./gradlew bootRun
```
Server runs on: http://localhost:8080

config file: src/main/resources/application.yml

3. Verify Data
The database already has seed data (customers, products, accounts).
Test it:
curl http://localhost:8080/api/customers
---

#### Option 2: Run tests first to populate data, then run server

The test profile uses testcontainers (ephemeral database). For manual testing with same data, here's the easiest approach:
```bash
# start PosgreSQL testcontainers
docker run testcontainers
# or rerun using container name
docker start test-postgres
# This populates the testcontainers database with seed data
./gradlew test
# Then run server with test profile
./gradlew bootRun --args='--spring.profiles.active=test'
```

**Note:** Your existing test-postgres container has the same data as when tests ran. That's why you see the same data. But testcontainers are typically ephemeral - data may not persist.

## System Security

**Problem Explanation (Simple)**
What Happened
The server had two security systems fighting each other when running in development mode:
1. Security System A said: "You need a password to access APIs"
2. Security System B said: "No password needed in development"
When both were active, sometimes one would block requests (403 error) before the other could allow them.
Why Some APIs Worked
Products happened to go through the system that allowed access. Other APIs like Master Data and Charges went through the system that blocked them.
**The Fix**
We told the server:
- Development mode (test & dev profile): Use "No password needed" security only
- Production mode: Use "Password required" security

Now in development, all APIs are accessible without a password.

---

**Admin user:**
Email:	admin@bank.com
Password:	admin123
JWT Token:	Received ✅ (always have)

The server is running with dev profile (no password required). You can now test with:
Quick test:
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@bank.com","password":"admin123"}'
This returns an access token you can use for authenticated requests.
Note: There's a warning about Jackson not supporting Instant in the logs - it's not critical but could be fixed later.

**Running Server with Different Profiles**
Current Setup
- Default profile: dev (no password required)
- Server running on port 8080

**Commands**
Profile	Command
dev:	./gradlew bootRun
test:	./gradlew bootRun --args='--spring.profiles.active=test'
prod:	./gradlew bootRun --args='--spring.profiles.active=prod'
---

**To Switch Profiles**
1. Stop current server:
```bash
lsof -ti:8080 | xargs kill -9
```
2. Start with desired profile:
```bash
# Dev (current - no password)
./gradlew bootRun
# Test 
./gradlew bootRun --args='--spring.profiles.active=test'
# Prod (JWT required)
./gradlew bootRun --args='--spring.profiles.active=prod'
```
---

**Profile Behavior**
Profile	APIs	Auth Required
dev	All accessible	❌ No
test	All accessible	❌ No
prod	All accessible	✅ Yes

---

