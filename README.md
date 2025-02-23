# 🚀 Project Name

## 📌 Project Overview
This is a **Spring Boot Monolithic Application** that includes authentication (JWT), visit management, notifications, transport management, and document storage.

## 🛠 Tech Stack
- **Backend:** Java, Spring Boot
- **Database:** MySQL
- **Security:** JWT Authentication, Spring Security
- **Version Control:** Git & GitHub
- **IDE:** IntelliJ IDEA Community Edition

---

## 🔥 Getting Started

### ✅ 1. Clone the Repository
Before starting, ensure you have **Git** installed. Then, open a terminal and run:

```sh
git clone https://github.com/Iheb-Zenkri/VisitEntreprise---SpringBoot.git
cd Visit
```

### ✅ 2. Open in IntelliJ IDEA
1. Open **IntelliJ IDEA Community Edition**
2. Click **File** > **Open**
3. Select the cloned `Visit` folder
4. Wait for dependencies to load (Maven/Gradle)

### ✅ 3. Configure Database (MySQL)
1. Create a **new MySQL database** (e.g., `Visit_db`)
2. Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Visit_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

3. Save the file and restart IntelliJ if needed.

### ✅ 4. Run the Project
Click **Run ▶️** in IntelliJ or execute:

```sh
mvn spring-boot:run
```

If the application starts successfully, it should be running at:
```
http://localhost:8080
```

---

## 🔄 Git Workflow for Collaboration

### 1️⃣ Pull the Latest Code
Before working on a new feature, always pull the latest code from `main`:

```sh
git checkout main
git pull origin main
```

### 2️⃣ Make Changes & Commit
After making code changes, add and commit them:

```sh
git add .
git commit -m "Your commit message here"
```

### 3️⃣ Push to GitHub
Push your changes directly to `main`:

```sh
git push origin main
```

> 🚨 **Warning:** Since everyone is working on the `main` branch, be cautious to avoid conflicts. Always pull before making changes.

### 4️⃣ Merge Conflict Resolution
If you face merge conflicts:
1. Pull the latest changes:

```sh
git pull origin main
```

2. Manually resolve conflicts in IntelliJ
3. Add resolved files:

```sh
git add .
```

4. Commit and push:

```sh
git commit -m "Resolved merge conflicts"
git push origin main
```

---

## 🔧 Common Issues & Fixes

**1️⃣ Maven Dependencies Not Loading?**
- Go to **File** > **Invalidate Caches & Restart** in IntelliJ

**2️⃣ Database Connection Issues?**
- Ensure MySQL is running: `sudo systemctl start mysql`
- Check credentials in `application.properties`

**3️⃣ Merge Conflicts?**
- If conflicts occur when merging, resolve them manually in IntelliJ and commit the changes.

---
