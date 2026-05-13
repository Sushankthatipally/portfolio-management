# Portfolio Management System (PMS)

A full-stack Java EE web application built to demonstrate the complete syllabus of
Web Programming: HTML/CSS, JavaScript/XML/AJAX, Servlets/JDBC, and JSP/MVC.

Users can register, log in, browse a live stock market, place BUY/SELL orders against
a virtual cash balance, and review their holdings, P&L, and full transaction history.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | HTML5, CSS3 (Box model, Flex/Grid, Navigation, Dropdowns) |
| Client logic | JavaScript ES6 — DOM, Events, Regex, AJAX (XMLHttpRequest) |
| Data | XML with namespaces, DTD |
| Backend | Java Servlets 4.0 (Tomcat 9+) |
| Views | JSP — Directives, Scriptlets, Expressions, JavaBeans, implicit objects |
| Database | MySQL 8 via JDBC |
| Architecture | Model-View-Controller (MVC) |

---

## Syllabus Coverage

### Unit I — HTML & CSS
- `index.html` — semantic landing page with hero, feature grid
- Forms (`login.jsp`, `register.jsp`, `trade.jsp`) — text, password, email, checkbox, number, hidden
- Tables — Dashboard, Market, Transactions
- Images / icon containers
- CSS — selectors, box model, margins/padding/borders, backgrounds, links, lists, tables
- `position: sticky`, `z-index`, `overflow`, `inline-block`, dropdown navigation, multi-column layout

### Unit II — JavaScript, XML, AJAX
- `js/validation.js` — variables (let/const), operators, conditionals, **regular expressions** for form validation
- `js/script.js` — arrays, array functions (`forEach`, `Array.from`), String methods, **Date object**
- **Event handlers** — keyboard (`/` focus shortcut), mouse (row hover), form (submit confirm), window (load animation)
- **DOM** manipulation — `getElementById`, `querySelectorAll`, `innerHTML`, style mutation
- **AJAX** — `XMLHttpRequest` calls `api/stocks` and parses **XML response** to render the market table
- **XML** — `WebContent/xml/stocks.xml` with namespaces; `stocks.dtd` schema
- Servlet `/api/stocks` emits valid XML (and JSON when `?format=json`)

### Unit III — Servlets
- **Servlet lifecycle** — `init()` (reads init-params from `web.xml`), `service()`, `destroy()`
- **Servlet API** — `HttpServletRequest`, `HttpServletResponse`, `ServletContext`, `HttpSession`
- **Reading servlet parameters** — `request.getParameter(...)`
- **Reading initialization parameters** — `getInitParameter("appName")` in `LoginServlet`
- **Handling HTTP Request & Response** — GET/POST in all controllers
- **Session management**
  - Hidden fields — `<input type="hidden" name="stockId">` in `trade.jsp`
  - Cookies — "Remember me" persistent cookie in `LoginServlet`
  - HttpSession — login state, flash messages
  - URL rewriting — `response.encodeRedirectURL(...)`
- **RequestDispatcher** — `request.getRequestDispatcher(...).forward(...)`
- **JDBC** — `DBConnection.java` + DAO classes use `PreparedStatement`, transactions, connection close

### Unit IV — JSP & MVC
- **Anatomy of JSP** — directives, declarations, expressions, scriptlets, comments
  - `<%@ page %>` / `<%@ page import %>` directives at the top of each view
  - `<%= ... %>` expressions for output
  - `<% ... %>` scriptlets for control flow
- **Implicit objects** — `request`, `response`, `session`, `application`, `out`
- **JavaBeans** — `<jsp:useBean>` and `<jsp:getProperty>` used in `dashboard.jsp`
- **Session management** — cookies + sessions (same as Unit III)
- **JDBC from JSP** — via DAOs (loose coupling, MVC compliant)
- **MVC Architecture**
  - **Model** — `com.pms.model.*` (User, Stock, Holding, Transaction)
  - **View** — `WebContent/WEB-INF/views/*.jsp`
  - **Controller** — `com.pms.controller.*` Servlets
  - DAO layer separates persistence from controllers

---

## Project Structure

```
murali-wt/
├── WebContent/
│   ├── WEB-INF/
│   │   ├── web.xml                  ← deployment descriptor
│   │   ├── lib/                     ← drop mysql-connector-j-*.jar here
│   │   └── views/
│   │       ├── header.jsp / footer.jsp
│   │       ├── login.jsp / register.jsp
│   │       ├── dashboard.jsp
│   │       ├── market.jsp / trade.jsp
│   │       ├── transactions.jsp
│   │       └── error.jsp
│   ├── css/style.css
│   ├── js/script.js / validation.js
│   ├── xml/stocks.xml / stocks.dtd
│   └── index.html
├── src/com/pms/
│   ├── model/        User, Stock, Holding, Transaction
│   ├── dao/          DBConnection, UserDAO, StockDAO, PortfolioDAO
│   └── controller/   Login, Register, Logout, Dashboard, Market,
│                     Trade, Transaction, StockApi (Servlets)
└── database/schema.sql
```

---

## Setup & Run (Eclipse IDE for Enterprise Java)

### 1. Database
```bash
mysql -u root -p < database/schema.sql
```
Set these environment variables before starting the app:
```
PMS_DB_URL=jdbc:mysql://localhost:3306/portfolio_db?useSSL=false&serverTimezone=UTC
PMS_DB_USER=root
PMS_DB_PASSWORD=your_password
```
If you prefer hardcoding local credentials, update `src/com/pms/dao/DBConnection.java`.

### 2. JDBC Driver
Download `mysql-connector-j-8.x.x.jar` from
https://dev.mysql.com/downloads/connector/j/ and place it in
`WebContent/WEB-INF/lib/`.

### 3. Server
1. In Eclipse: **File → New → Dynamic Web Project** named `pms`
2. Set the source folder to `src/` and content directory to `WebContent/`
3. Add **Apache Tomcat 9** as the target runtime
4. Add the MySQL JDBC jar to the project's build path
5. Right-click project → **Run As → Run on Server**

### 4. Open in browser
```
http://localhost:8080/pms/
```

### 5. Demo credentials
| Username | Password |
|----------|----------|
| admin    | admin123 |
| john     | john123  |

---

## Key URLs

| URL | Purpose |
|-----|---------|
| `/`              | Landing page (index.html) |
| `/login`         | Login form |
| `/register`      | Register form |
| `/dashboard`     | User dashboard with holdings & P&L |
| `/market`        | Browse all stocks (with search) |
| `/trade?stockId=N&action=BUY` | Place a trade |
| `/transactions`  | Transaction history |
| `/api/stocks`    | Returns XML feed of stocks |
| `/api/stocks?format=json` | Returns JSON feed |
| `/logout`        | Destroys session |

---

## Notes

- The Servlet API is `javax.servlet.*` (Tomcat 9). For Tomcat 10+ use `jakarta.servlet.*`.
- Prices are static demo data; you can plug in a real feed by replacing
  `StockDAO.getAllStocks()` with an API call.
- Trades run inside a JDBC transaction (`autoCommit = false`, commit/rollback).
