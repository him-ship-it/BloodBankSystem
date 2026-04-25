# 🩸 Blood Bank & Organ Donor Coordination System

A full-stack desktop application built with **Java**, **MySQL**, **JDBC**, and **Java Swing** to manage blood donations, organ donors, recipients, blood inventory, and medical transfusion records — all through a modern dark-themed graphical dashboard.

---

## 📸 Features

- ✅ Donor registration with organ donor consent tracking
- ✅ Recipient management with urgency levels (LOW / MEDIUM / HIGH / CRITICAL)
- ✅ Real-time blood inventory tracking for all 8 blood groups
- ✅ Organ inventory management (Kidney, Liver, Heart, Lungs, Cornea, Pancreas)
- ✅ Blood group matching to find compatible donors instantly
- ✅ Blood transfusion workflow with stock validation
- ✅ Complete transaction audit log
- ✅ Hospital registry with foreign key integrity
- ✅ Live Swing Dashboard with auto-refresh every 30 seconds

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 24 |
| Database | MySQL 8.x |
| Connectivity | JDBC (MySQL Connector/J) |
| UI Framework | Java Swing |
| IDE | IntelliJ IDEA |
| DB Client | MySQL Workbench |
| Architecture | DAO + Service Layer Pattern |

---

## 📁 Project Structure


---

## ⚙️ Setup Instructions

### Prerequisites
- Java JDK 17 or above
- MySQL 8.x
- IntelliJ IDEA
- MySQL Connector/J JAR

### Step 1 — Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/BloodBankSystem.git
cd BloodBankSystem
```

### Step 2 — Setup the database
1. Open MySQL Workbench
2. Open and run `sql/schema.sql`
3. Database `blood_bank_db` will be created with all tables

### Step 3 — Add MySQL Connector
1. Download from https://dev.mysql.com/downloads/connector/j/
2. Add the `.jar` to your project libraries in IntelliJ

### Step 4 — Configure database password
Open `src/db/DBConnection.java` and update:
```java
private static final String PASS ="";
```

### Step 5 — Run the application

**Console App:**
Right-click `src/main/Main.java` → Run

**Swing Dashboard:**
Right-click `src/ui/MainDashboard.java` → Run

---

## 🗄️ Database Schema

```sql
blood_bank_db/
├── hospital          (hospital_id, name, city, contact)
├── donor             (donor_id, name, age, blood_group, contact, city, is_organ_donor)
├── recipient         (recipient_id, name, age, blood_group, hospital_id, urgency_level)
├── blood_inventory   (inventory_id, blood_group, units_available)
├── organ_inventory   (organ_id, organ_type, donor_id, blood_group, status, hospital_id)
└── transaction_log   (log_id, transaction_type, donor_id, recipient_id, blood_group, units)
```

---

## 👨‍💻 Author

**Your Name**
- GitHub: [him_ship_it](https://github.com/him_ship_it)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).