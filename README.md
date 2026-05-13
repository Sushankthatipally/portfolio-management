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
