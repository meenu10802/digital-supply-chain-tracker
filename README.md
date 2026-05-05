# Digital Supply Chain Tracker 🚚📦

## 📌 Overview

The **Digital Supply Chain Tracker** is a microservices-based system designed to track and manage supply chain operations such as inventory, orders, and shipments.

This project follows a **Spring Boot Microservices Architecture** with centralized configuration, service discovery, and API gateway routing.

---

## 🏗️ Architecture

Client → API Gateway → Eureka Server → Microservices
↘ Config Server

---

## ⚙️ Tech Stack

* Java 17
* Spring Boot
* Spring Cloud
* Eureka Server (Service Discovery)
* Config Server (Centralized Configuration)
* API Gateway (Routing)
* Maven (Build Tool)
* Git & GitHub (Version Control)

---

## 🚀 Infrastructure Services (Completed)

### ✅ Config Server

* Centralized configuration management
* Runs on **port 8888**

### ✅ Eureka Server

* Service discovery and registration
* Runs on **port 8761**

### ✅ API Gateway

* Entry point for all client requests
* Routes requests to services
* Runs on **port 8080**

---

## 📂 Project Structure

```
digital-supply-chain/
│── config-server/
│── eureka-server/
│── api-gateway/
```

---

## 🔄 Git Workflow

* `main` → Stable code
* `dev` → Integration branch
* `feature/infra-services` → Infrastructure setup

### ✔ Completed Flow

* Created infra services in feature branch
* Raised Pull Request
* Resolved conflicts
* Merged into `dev`

---

## 🌐 Service Ports

| Service       | Port |
| ------------- | ---- |
| Config Server | 8888 |
| Eureka Server | 8761 |
| API Gateway   | 8080 |

---

## ▶️ How to Run

### 1. Start Config Server

```
cd config-server
mvn spring-boot:run
```

### 2. Start Eureka Server

```
cd eureka-server
mvn spring-boot:run
```

### 3. Start API Gateway

```
cd api-gateway
mvn spring-boot:run
```

---

## 🔍 Verification

* Eureka Dashboard → http://localhost:8761
* Config Server → http://localhost:8888
* API Gateway → http://localhost:8080

---

## 🧠 Future Scope

* Inventory Service
* Order Service
* Shipment Service
* User Authentication Service
* Database Integration
* Security (JWT / OAuth)

---

## 👨‍💻 Team

* Team Lead: Kavish Dhiman
* Members: Srihari V , Kaviya S P , Jayanthi M , Meenakshi Kalimuthu 

---

## 📌 Status

✔ Infrastructure Setup Completed
🚧 Business Services In Progress

---
