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