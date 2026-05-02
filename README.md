# 🌍 Like Hero To Zero

Eine Webanwendung zur Darstellung weltweiter CO2-Emissionen, entwickelt im Rahmen der Fallstudie für den Kurs **IPWA02-01 – Programmierung von industriellen Informationssystemen mit Java EE** an der IU Internationalen Hochschule.

## 📋 Beschreibung

Like Hero To Zero bündelt weltweite CO2-Emissionsdaten in einer übersichtlichen Webanwendung. Die Daten werden automatisch beim Start der Anwendung von der **Our World in Data API** (Global Carbon Project) geladen und umfassen die Jahre 2004 bis 2024.

## ✨ Features

- 🔍 **CO2-Suche** – Suche nach CO2-Emissionen eines Landes per ISO-Ländercode (z.B. DE, CN, US)
- 🌐 **Länderübersicht** – Vollständige Liste aller Länder mit aktuellsten CO2-Daten
- 📈 **Trendanalyse** – Visualisierung der CO2-Entwicklung der letzten 20 Jahre mit Chart.js
- 🔄 **Ländervergleich** – Vergleich von zwei Ländern in einem Diagramm mit zwei Y-Achsen
- 🔐 **Login-System** – Zwei Benutzerrollen: Wissenschaftler und Herausgeber
- ✅ **Freigabe-Workflow** – Neue Einträge müssen von einem Herausgeber freigegeben werden
- 🔄 **Automatischer Datenimport** – CO2-Daten werden beim Start automatisch von Our World in Data geladen

## 🛠️ Technologiestack

| Technologie | Version | Beschreibung |
|-------------|---------|--------------|
| Java | 21 | Programmiersprache |
| Jakarta EE | 9.1 | Enterprise-Framework |
| JSF | 3.0 | JavaServer Faces (Benutzeroberfläche) |
| CDI | 3.0 | Contexts and Dependency Injection |
| JPA / OpenJPA | 3.0 | Jakarta Persistence API |
| Apache TomEE Plume | 10.1.4 | Application Server |
| MySQL | 9.x | Relationale Datenbank |
| Maven | 3.x | Build-Tool |
| Chart.js | 4.4.0 | Diagramm-Bibliothek |

## 🚀 Installation und Start

### Voraussetzungen

- Java 21 (OpenJDK)
- Apache TomEE Plume 10.1.4
- MySQL 8.x oder höher
- Apache NetBeans (empfohlen) oder eine andere Java-IDE
- Maven 3.x

### Schritt 1: Datenbank einrichten

```sql
CREATE DATABASE likeherozero CHARACTER SET utf8mb4;

USE likeherozero;

CREATE TABLE countries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country_code VARCHAR(10) NOT NULL
);

CREATE TABLE co2_emissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year INT NOT NULL,
    emission_kt DOUBLE NOT NULL,
    country_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'APPROVED',
    FOREIGN KEY (country_id) REFERENCES countries(id)
);

CREATE TABLE scientists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'SCIENTIST'
);

-- Admin-Benutzer anlegen
INSERT INTO scientists (username, password, email, role)
VALUES ('admin', 'admin123', 'admin@iubh.de', 'EDITOR');

-- Wissenschaftler anlegen
INSERT INTO scientists (username, password, email, role)
VALUES ('scientist1', 'pass123', 'scientist@iubh.de', 'SCIENTIST');
```

### Schritt 2: TomEE konfigurieren

Füge folgendes in die Datei `conf/tomee.xml` deiner TomEE-Installation ein:

```xml
<Resource id="jdbc/likeherozero" type="DataSource">
    JdbcDriver = com.mysql.cj.jdbc.Driver
    JdbcUrl = jdbc:mysql://localhost:3306/likeherozero?serverTimezone=UTC
    UserName = root
    Password = DEIN_MYSQL_PASSWORT
    JtaManaged = true
</Resource>
```

### Schritt 3: MySQL-Treiber kopieren

Kopiere den MySQL-Connector in das `lib`-Verzeichnis von TomEE:

```bash
cp ~/.m2/repository/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar /pfad/zu/tomee/lib/
```

### Schritt 4: Projekt bauen und starten

```bash
mvn clean install
```

Oder in NetBeans: Rechtsklick auf Projekt → **Clean and Build** → **Run**

### Schritt 5: Anwendung aufrufen

```
http://localhost:8080/like-hero-to-zero/index.xhtml
```

## 👤 Benutzer

| Benutzername | Passwort | Rolle |
|---|---|---|
| admin | admin123 | Herausgeber (Editor) |
| scientist1 | pass123 | Wissenschaftler |

## 📊 Datenbankstruktur

```
countries
├── id (PK)
├── name
└── country_code

co2_emissions
├── id (PK)
├── year
├── emission_kt
├── country_id (FK → countries)
└── status (PENDING / APPROVED)

scientists
├── id (PK)
├── username
├── password
├── email
└── role (SCIENTIST / EDITOR)
```

## 📁 Projektstruktur

```
src/main/
├── java/
│   ├── de/iubh/controller/
│   │   ├── Co2Controller.java       # JSF Backing Bean für CO2-Daten
│   │   └── LoginController.java     # JSF Backing Bean für Login
│   ├── de/iubh/model/
│   │   ├── Co2Emission.java         # JPA Entity
│   │   ├── Country.java             # JPA Entity
│   │   └── Scientist.java           # JPA Entity
│   └── de/iubh/service/
│       ├── Co2DataImportService.java # Automatischer Datenimport
│       ├── Co2EmissionService.java   # EJB Service
│       ├── CountryService.java       # EJB Service
│       └── ScientistService.java     # EJB Service
├── resources/META-INF/
│   └── persistence.xml              # JPA Konfiguration
└── webapp/
    ├── WEB-INF/
    │   ├── beans.xml
    │   ├── resources.xml            # DataSource Konfiguration
    │   └── web.xml
    ├── dashboard.xhtml              # Dashboard (Wissenschaftler/Herausgeber)
    ├── index.xhtml                  # Startseite
    ├── login.xhtml                  # Login-Seite
    └── trend.xhtml                  # Trendanalyse
```

## 📈 Datenquelle

Die CO2-Daten stammen vom **Global Carbon Project** via **Our World in Data**:

> Global Carbon Project. (2024). *Annual CO2 emissions by country*. Our World in Data. https://ourworldindata.org/co2-emissions

## 🎓 Projekt-Kontext

Diese Anwendung wurde als Fallstudie im Rahmen des Kurses **IPWA02-01** an der **IU Internationalen Hochschule** entwickelt.

- **Studiengang:** Bachelor of Science Medieninformatik
- **Autorin:** Silvia H.
- **Matrikelnummer:** 32********
