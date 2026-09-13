# 🛠️ TechAssist AI

**An intelligent IT troubleshooting platform where employees report technical problems and IT technicians resolve them with AI assistance.**

![Status](https://img.shields.io/badge/status-active-brightgreen)
![Backend](https://img.shields.io/badge/backend-Spring%20Boot%203.3-blue)
![Frontend](https://img.shields.io/badge/frontend-React%20%2B%20TypeScript-61dafb)
![Database](https://img.shields.io/badge/database-PostgreSQL-336791)
![AI](https://img.shields.io/badge/AI-Google%20Gemini-4285f4)
![License](https://img.shields.io/badge/license-MIT-green)

---

## 🎯 What Is TechAssist AI?

TechAssist AI is a full-stack IT helpdesk platform that combines traditional ticket management with modern AI capabilities.

**Core workflow:**
Employee reports an IT problem
↓
AI categorizes it (category + priority)
↓
Technician investigates with AI troubleshooting guidance
↓
Technician resolves the issue
↓
Solution is documented in the knowledge base

text

The AI **assists** the technician — it doesn't replace their decision-making.

---

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based stateless authentication
- Three roles: **EMPLOYEE**, **TECHNICIAN**, **ADMIN**
- BCrypt password hashing
- Role-based endpoint protection (`@PreAuthorize`)

### 🎫 Ticket Management
- Create, view, update, and resolve tickets
- Full ticket lifecycle: `NEW → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED`
- Priority levels: `CRITICAL`, `HIGH`, `MEDIUM`, `LOW`
- Category system (Hardware, Software, Network, Account & Access, Security)
- Technician assignment
- Comments between employee and technician
- Automatic timestamps (`@PrePersist`, `@PreUpdate`)

### 🤖 AI Features (Powered by Google Gemini)
- **Ticket Categorization** — AI reads the ticket and suggests category, subcategory, and priority
- **Troubleshooting Assistant** — AI generates a numbered list of diagnostic steps
- Model fallback chain (if one model is unavailable, tries another)
- API key stored in environment variable (never committed)

### 📚 Knowledge Base
- Reusable IT documentation articles
- Full-text search across title, problem, and tags
- Category filtering
- 6 pre-seeded troubleshooting guides:
  - VPN Troubleshooting Guide
  - DNS Troubleshooting Guide
  - Active Directory Account Lockout
  - Windows Network Troubleshooting
  - Shared Folder Permissions
  - Printer Troubleshooting

### 📊 Dashboards
- **Employee dashboard** — my tickets, open, resolved, by status
- **Technician dashboard** — assigned tickets, high priority, by category
- **Admin dashboard** — system-wide statistics and breakdowns

---

## 🏗️ Architecture

### Layered Backend
┌─────────────────────────────────────────────┐
│ React Frontend (SPA) │
└─────────────────────────────────────────────┘
↓ HTTP + JWT
┌─────────────────────────────────────────────┐
│ CONTROLLER LAYER │
│ AuthController, TicketController, │
│ AiController, DashboardController, │
│ KnowledgeController │
└─────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────┐
│ SERVICE LAYER │
│ AuthService, TicketService, AiService, │
│ DashboardService, KnowledgeService │
└─────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────┐
│ REPOSITORY LAYER │
│ Spring Data JPA repositories │
└─────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────┐
│ PostgreSQL │
└─────────────────────────────────────────────┘

text

### Authentication Flow
User POST /api/auth/login {email, password}

Backend verifies via AuthenticationManager + BCrypt

Backend generates signed JWT (HMAC-SHA512)

Frontend stores JWT in localStorage

Every request sends: Authorization: Bearer <JWT>

JwtAuthenticationFilter validates token on each request

SecurityContextHolder identifies the user

text

### AI Integration Flow
Frontend
↓ POST /api/ai/categorize {title, description}
Backend
↓ AiService builds OpenAI-compatible JSON
Google Gemini (via OpenAI-compatible endpoint)
↓ Returns natural-language response
AiService parses the response into a DTO
↓
Frontend receives structured JSON

text

The API key **stays on the backend** — never exposed to the frontend.

---

## 🛠️ Tech Stack

### Backend
| Technology | Purpose |
|-----------|---------|
| **Java 17** | Language |
| **Spring Boot 3.3.4** | Framework |
| **Spring Security** | Authentication & authorization |
| **Spring Data JPA / Hibernate** | ORM |
| **PostgreSQL 18** | Database |
| **JJWT 0.12.6** | JWT tokens |
| **Lombok** | Boilerplate reduction |
| **Maven** | Build tool |

### Frontend *(in progress)*
| Technology | Purpose |
|-----------|---------|
| **React 18** | UI framework |
| **TypeScript** | Type safety |
| **Vite** | Build tool |
| **Tailwind CSS** | Styling |
| **Axios** | HTTP client |
| **React Router** | Routing |

### AI
| Provider | Model |
|----------|-------|
| **Google AI Studio** | gemini-2.5-flash, gemini-3.8-flash |

---

## 📁 Project Structure
TechAssist-AI/
├── backend/
│ ├── src/main/java/com/techassist/backend/
│ │ ├── BackendApplication.java
│ │ ├── config/
│ │ │ ├── SecurityConfig.java
│ │ │ └── DataSeeder.java
│ │ ├── controller/
│ │ │ ├── AuthController.java
│ │ │ ├── TicketController.java
│ │ │ ├── AiController.java
│ │ │ ├── DashboardController.java
│ │ │ └── KnowledgeController.java
│ │ ├── dto/ (Request/Response objects)
│ │ ├── exception/ (GlobalExceptionHandler)
│ │ ├── model/ (Entities)
│ │ │ ├── User.java
│ │ │ ├── Role.java
│ │ │ ├── Ticket.java
│ │ │ ├── TicketStatus.java
│ │ │ ├── TicketPriority.java
│ │ │ ├── Category.java
│ │ │ ├── TicketComment.java
│ │ │ └── KnowledgeArticle.java
│ │ ├── repository/ (Spring Data JPA)
│ │ ├── security/ (JWT filter, UserDetailsService)
│ │ └── service/ (Business logic)
│ ├── src/main/resources/
│ │ └── application.properties
│ └── pom.xml
├── frontend/ (React + TypeScript, coming soon)
├── docs/
├── .gitignore
└── README.md

text

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.9+
- PostgreSQL 16+
- Node.js 20+ (for frontend)
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/mohmdmudawi-a11y/TechAssist-AI.git
cd TechAssist-AI
2. Set Up PostgreSQL
sql
CREATE DATABASE techassist_db;
3. Configure Environment Variables
The app requires these environment variables:

Variable	Purpose	Example
GOOGLE_AI_KEY	Google AI Studio API key	AIza...
Windows:

cmd
setx GOOGLE_AI_KEY "your_key_here"
Mac/Linux:

bash
export GOOGLE_AI_KEY="your_key_here"
4. Configure application.properties
Edit backend/src/main/resources/application.properties:

properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/techassist_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_DB_PASSWORD

# JWT
app.jwt.secret=YOUR_BASE64_SECRET
app.jwt.expiration=86400000

# AI
openrouter.api-key=${GOOGLE_AI_KEY}
openrouter.url=https://generativelanguage.googleapis.com/v1beta/openai/chat/completions
openrouter.models=gemini-2.5-flash,gemini-3.8-flash
5. Run the Backend
bash
cd backend
./mvnw spring-boot:run    # Mac/Linux
mvnw.cmd spring-boot:run  # Windows
Backend starts at http://localhost:8080

On first startup, DataSeeder inserts 6 knowledge base articles automatically.

6. Run the Frontend (coming soon)
bash
cd frontend
npm install
npm run dev
Frontend starts at http://localhost:5173

📡 API Endpoints
Authentication (Public)
Method	Endpoint	Description
POST	/api/auth/register	Register a new user
POST	/api/auth/login	Login and receive JWT
Tickets (Authenticated)
Method	Endpoint	Role	Description
POST	/api/tickets	EMPLOYEE+	Create a ticket
GET	/api/tickets/mine	Any	My tickets
GET	/api/tickets/assigned	TECHNICIAN+	Tickets assigned to me
GET	/api/tickets/{id}	Any	Get one ticket
GET	/api/tickets	ADMIN	All tickets
POST	/api/tickets/{id}/assign	ADMIN	Assign a technician
PUT	/api/tickets/{id}/status	TECHNICIAN+	Change status
PUT	/api/tickets/{id}/priority	TECHNICIAN+	Change priority
POST	/api/tickets/{id}/resolve	TECHNICIAN+	Resolve with note
AI (Authenticated)
Method	Endpoint	Role	Description
POST	/api/ai/categorize	Any	AI suggests category + priority
POST	/api/ai/troubleshoot	TECHNICIAN+	AI generates troubleshooting steps
Dashboards (Authenticated)
Method	Endpoint	Role
GET	/api/dashboard/employee	Any
GET	/api/dashboard/technician	TECHNICIAN+
GET	/api/dashboard/admin	ADMIN
Knowledge Base (Authenticated)
Method	Endpoint	Role
GET	/api/knowledge	Any
GET	/api/knowledge/{id}	Any
GET	/api/knowledge/search?q=vpn	Any
GET	/api/knowledge/category/{category}	Any
POST	/api/knowledge	TECHNICIAN+
PUT	/api/knowledge/{id}	TECHNICIAN+
📝 Example API Calls
Register a user
bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@test.com",
    "password": "Password123",
    "role": "EMPLOYEE"
  }'
Login
bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "john@test.com", "password": "Password123"}'
Response:

json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "email": "john@test.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "EMPLOYEE"
}
Create a ticket
bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Cannot connect to VPN",
    "description": "Every time I try to connect I get error 809.",
    "categoryId": 3,
    "priority": "HIGH"
  }'
AI categorization
bash
curl -X POST http://localhost:8080/api/ai/categorize \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"title": "Cannot connect to VPN", "description": "Error 809."}'
Response:

json
{
  "category": "Network",
  "subcategory": "VPN",
  "priority": "HIGH",
  "reasoning": "Error 809 indicates a VPN tunnel configuration issue."
}
🔒 Security Highlights
Concern	Implementation
Password storage	BCrypt hashing (never plain text)
Session management	Stateless JWT (no server-side sessions)
Token validation	Signature verification with HMAC-SHA512
Role enforcement	@PreAuthorize + SecurityFilterChain rules
API keys	Environment variables only — never committed
CORS	Restricted to localhost:5173 in dev
Input validation	Bean Validation (@Valid, @NotBlank, @Email)
Global error handling	Centralized @RestControllerAdvice


🗺️ Roadmap
☑ Phase 1 — Project foundation
☑ Phase 2 — JWT authentication + roles
☑ Phase 3 — Ticket management + comments
☑ Phase 4 — Dashboards
☑ Phase 5 — AI categorization + troubleshooting
☑ Phase 6 — Knowledge base
□ Phase 7 — React frontend (in progress)
□ Phase 8 — Polish, tests, deployment
Future Enhancements
□ File attachments on tickets
□ Email notifications
□ Audit log UI
□ Advanced analytics dashboard
□ Docker containerization
□ CI/CD pipeline (GitHub Actions)
□ Deployment to cloud (Railway / Render / AWS)
🧪 Testing
(Coming soon) — JUnit tests + Testcontainers for integration testing.

👨‍💻 Author
Mohammed Mudawi

GitHub: @mohmdmudawi-a11y

Project: Full Stack Software with AI-Enabled For Ticket System

📄 License
This project is licensed under the MIT License.

🙏 Acknowledgments

Google AI Studio — for free access to Gemini models

Spring Boot community — for excellent documentation

Open source maintainers — for the tools that made this possible

📞 Contact
For questions, feedback, or collaboration:

Open an issue on GitHub

Reach out via the GitHub profile above

⭐ If you found this project helpful, consider giving it a star!