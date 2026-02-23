## AI-Powered Event-Driven CRM Server

**crm-server** is a production-ready, **multi-tenant CRM backend** built with Spring Boot 3, PostgreSQL, Kafka, and JWT security. It combines **AI-powered lead scoring**, **event-driven workflows**, and **full auditability** to help SaaS teams ship a modern CRM or sales platform fast.

### Why this project is valuable

- **Launch a CRM SaaS faster**: Opinionated architecture for multi-tenant B2B apps (tenants, users, roles, JWT, seeding).
- **AI-enriched leads out of the box**: Uses a Generative AI model (Gemini) to summarize accounts and generate 0–100 deal scores.
- **Event-driven from day one**: Kafka-backed lead events decouple write operations from heavy AI/automation work.
- **Workflow automation built in**: JSON-based rules engine that can auto-update lead status when conditions match.
- **Auditable & enterprise-ready**: Dedicated audit logs for changes, with per-lead history.

If you are building:

- A **CRM/Sales pipeline** product  
- A **marketing automation** or **lead-intelligence** platform  
- A **multi-tenant SaaS backend** that needs auth, tenants, workflows, and AI  

…this server gives you a strong foundation you can demo to stakeholders or investors immediately.

---

### Core features

- **Multi-tenancy**
  - `Tenant` entity with plans (`FREE`, `PRO`, `ENTERPRISE`) and active flag.
  - `TenantContext` using `ThreadLocal` for request‑scoped tenant IDs.
  - All key domain entities (e.g. `Lead`, `Workflow`) are tenant-aware.

- **Authentication & Security**
  - JWT-based stateless auth (`JwtService`, `JwtAuthenticationFilter`, `SecurityConfig`).
  - `/api/auth/login` issues tokens containing both **email** and **tenantId**.
  - Protected APIs require `Authorization: Bearer <token>`.

- **Lead Management**
  - `Lead` entity with lifecycle status (`NEW`, `CONTACTED`, `QUALIFIED`, `LOST`, `WON`).
  - `LeadController` & `LeadService` for creating and paginating leads per tenant.
  - Ownership fields (`owner`) ready for assigning leads to sales reps.

- **AI Lead Scoring & Summaries**
  - `AiService` integrates with a Gemini model via HTTP (`RestClient`).
  - For each created lead, a background consumer calls AI to:
    - Produce a **1-sentence business summary**.
    - Generate a **0–100 score** estimating deal likelihood.
  - Results are stored on the lead (`aiSummary`, `aiScore`) and used by workflows.

- **Event-Driven Architecture (Kafka)**
  - `EventProducer` publishes `LeadCreatedEvent` to `crm.leads` topic.
  - `EventConsumer` listens to `crm.leads`, enriches with AI, updates DB, and triggers workflows.

- **Workflow Automation Engine**
  - `Workflow` entity stores JSON **conditions** and **actions** per tenant.
  - `WorkflowEngine` evaluates workflows for events like `LEAD_CREATED`.
  - Example rule: when `source == "Twitter"`, auto‑set lead status to `QUALIFIED`.

- **Audit Logging**
  - `AuditLog` entity captures who changed what and when.
  - `LeadService.updateLead` writes descriptive audit entries.
  - `AuditLogController` exposes read APIs per lead.

- **Seeding & Demo Mode**
  - `DataSeeding` creates a demo tenant, admin user, and sample workflow on startup if DB is empty.
  - Perfect for live demos (no manual setup beyond infra).

---

### Tech stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.2.x
- **Database**: PostgreSQL
- **Messaging**: Apache Kafka (via `spring-kafka`)
- **Security**: Spring Security + JWT (jjwt)
- **Build**: Maven (`pom.xml`)

---

### High-level architecture

- `ServerApplication` – Spring Boot entry point.
- **Config**
  - `SecurityConfig`, `JwtAuthenticationFilter`, `CustomUserDetailsService`, `TenantContext`, `DataSeeding`.
- **Domain**
  - Entities: `Lead`, `Tenant`, `User`, `Workflow`, `AuditLog`, `BaseEntity`.
  - DTOs: `LeadDTO`, `WorkflowDTO`, `AuthRequest`, `AuthResponse`, `CreateTenantRequest`, `AiResult`.
- **Bounded contexts**
  - **Auth**: `AuthController`, `AuthService`, `JwtService`, `UserRepository`.
  - **Tenants**: `TenantController`, `TenantService`, `TenantRepository`.
  - **Leads & AI**: `LeadController`, `LeadService`, `AiService`, `LeadRepository`, `EventProducer`, `EventConsumer`.
  - **Workflows**: `WorkflowController`, `WorkflowService`, `WorkflowEngine`, `WorkflowRepository`.
  - **Audit**: `AuditLogController`, `AuditLogRepository`, `AuditLog`.

---

### API overview (REST)

All non-auth endpoints are protected and require a valid JWT in the `Authorization` header.

- **Auth**
  - `POST /api/auth/login` – Returns `AuthResponse { token }` for the provided email/password.

- **Tenant onboarding**
  - `POST /api/tenants` – Public endpoint to create a tenant and its initial admin user.

- **Leads**
  - `POST /api/leads` – Create a new lead for the current tenant using `LeadDTO`.
  - `GET /api/leads?page=0&size=10` – Paginated list of leads for the tenant.

- **Workflows**
  - `POST /api/workflows` – Create a workflow rule for the current tenant.

- **Audit Logs**
  - `GET /api/audit-logs/{leadId}` – Fetch audit log entries for a specific lead.

> For a full, up-to-date picture of request/response shapes, inspect the controller and DTO classes in `src/main/java/com/crm/server`.

---

### Getting started

#### 1. Prerequisites

- Java 17+
- Maven
- PostgreSQL running and accessible
- Kafka broker (for example via Docker)

#### 2. Configure environment

Edit `src/main/resources/application.yml` or externalize these properties:

- **Database**
  - `spring.datasource.url` – e.g. `jdbc:postgresql://localhost:5433/crm_db`
  - `spring.datasource.username` / `spring.datasource.password`
- **Kafka**
  - `spring.kafka.bootstrap-servers` – e.g. `localhost:19092`
- **AI integration**
  - `crm.ai.api-key` – your Gemini API key (do **not** commit real keys).
  - `crm.ai.model-url` – default Gemini model endpoint.

In production, prefer environment variables or an external config server for secrets.

#### 3. Run the application

From the `server` directory (where `pom.xml` lives):

```bash
mvn spring-boot:run
```

On first startup with an empty DB:

- A **Demo tenant** (`Demo Corp`) is created.
- An **admin user** (`admin@demo.com` / password `secret`) is seeded.
- A sample **workflow rule** (`Auto-Qualify Twitter Leads`) is inserted.

#### 4. Try it out

1. **Authenticate**
   - `POST /api/auth/login` with JSON:
     ```json
     { "email": "admin@demo.com", "password": "secret" }
     ```
   - Copy the `token` from the response.
2. **Create a lead**
   - `POST /api/leads` with header `Authorization: Bearer <token>` and a body like:
     ```json
     {
       "firstName": "Jane",
       "lastName": "Doe",
       "email": "jane@fintech.io",
       "company": "Fintech.io",
       "source": "Twitter"
     }
     ```
3. **Watch the pipeline**
   - `LeadService` saves the lead and publishes `LeadCreatedEvent` to Kafka.
   - `EventConsumer` picks it up, calls `AiService`, updates `aiSummary` / `aiScore`, and triggers `WorkflowEngine`.
   - The seeded workflow auto‑updates the lead status if rules match (e.g. `source == "Twitter"`).

4. **Inspect audit logs**
   - When leads are updated via `LeadService.updateLead`, audit entries are written.
   - Fetch them with `GET /api/audit-logs/{leadId}`.

---

### Customizing for your product

- **Branding & domain language**: Rename entities (`Lead`, `Workflow`, etc.) to match your business domain.
- **AI prompts**: Adjust the prompt in `AiService` to optimize summaries and scoring for your vertical.
- **Rules engine**: Extend `WorkflowEngine` to support more fields, operators, and actions.
- **Security**:
  - Replace the `NoOpPasswordEncoder` with `BCryptPasswordEncoder`.
  - Harden CORS, rate limiting, and error messages for production.

---

### Selling this project internally or to investors

- **Clear story**: Modern SaaS CRM backend with AI, workflows, and multi-tenancy already wired.
- **Demo-ready**: Seeded data lets you run an end-to-end narrative in minutes (onboarding → login → create lead → AI enrichment → auto-qualification → audit trail).
- **Extensible**: Clean separation of concerns (auth, tenants, leads, workflows, AI, audit) makes it easy to evolve into a full product.

Use this repository as the **backend foundation** for your next CRM or customer data product, and focus your time on UI, analytics, and go-to-market instead of reinventing infrastructure.

