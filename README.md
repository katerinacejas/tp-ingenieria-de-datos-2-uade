# tp-ingenieria-de-datos-2-uade
Trabajo práctico de Ingeniería de Datos 2 de UADE

---

## 🧩 **Backend Políglota - Sistema de Facturación, Mensajería y Monitoreo**

Proyecto académico desarrollado en **Java Spring Boot**, con persistencia distribuida en **MySQL**, **MongoDB** y **Cassandra**.  
Implementa un entorno poliglota para gestionar usuarios, cuentas corrientes, facturación, pagos, mensajería, sensores y auditoría.

---

## 🚀 **Tecnologías utilizadas**

| Tipo | Tecnología | Uso principal |
|------|-------------|----------------|
| Backend | **Java 17 + Spring Boot 3.3** | Framework principal para servicios REST y lógica de negocio |
| Base relacional | **MySQL** | Facturación, usuarios, cuentas y pagos (consistencia ACID) |
| Base documental | **MongoDB** | Mensajería, grupos, sensores, mantenimiento y alertas (flexibilidad y volumen) |
| Base columnar | **Cassandra** | Logs de procesos y auditoría de mensajes (alta tasa de escritura, series temporales) |
| Seguridad | **JWT (JSON Web Token)** | Control de autenticación y roles (USER / ADMIN) |
| ORM / Persistencia | **Spring Data JPA, Spring Data MongoDB, Spring Data Cassandra** | Integración automática con las 3 bases de datos |
| Build | **Maven** | Gestión de dependencias y empaquetado |
| Librerías adicionales | **Lombok**, **Jakarta Persistence**, **JJWT** | Simplificación de código y autenticación |

---

## ⚙️ **Configuración del entorno**

### 🧱 **Requisitos previos**

| Herramienta | Versión recomendada |
|--------------|---------------------|
| Java | 17 o superior |
| Maven | 3.9+ |
| MySQL | 8.0+ |
| MongoDB | 6.0+ |
| Cassandra | 4.1+ |

---

## 🚦 **Asegurar que los servicios estén activos**

Iniciá tus motores de base de datos localmente:

```bash
# MySQL
sudo service mysql start

# MongoDB
sudo service mongod start

# Cassandra
sudo service cassandra start
```

---

## 🧮 **Ejecutar el backend**

```bash
mvn spring-boot:run
```

---

## 🧪 **Probar endpoints con Postman**

### Crear un nuevo sensor
```http
POST http://localhost:8080/api/sensors
```
**Body (JSON):**
```json
{
  "name": "Sensor BA-001",
  "type": "TEMPERATURE",
  "city": "Buenos Aires",
  "country": "Argentina"
}
```

### Listar sensores
```http
GET http://localhost:8080/api/sensors
```

### Registrar medición
```http
POST http://localhost:8080/api/measurements
```
**Body (JSON):**
```json
{
  "key": {
    "sensorId": "SENSOR_001",
    "timestamp": "2025-10-13T18:00:00Z"
  },
  "temperature": 21.5,
  "humidity": 65.0
}
```



## 📊 **Modelo de Base de Datos**

### 🗄️ MySQL (Transaccional)

- `users`: información y roles
- `accounts`: cuenta corriente de usuario
- `invoices`: facturas emitidas
- `payments`: pagos asociados
- `account_movements_histories`: historial de movimientos
- `processes`: servicios facturables
- `sessions`: control de sesiones activas

### 🍃 MongoDB (Documental)

Colecciones principales:
- `messages`: mensajes privados o grupales
- `groups`: grupos de usuarios
- `sensors`: dispositivos IoT
- `maintenance_checks`: revisiones
- `alerts`: alertas generadas

### ⚡ Cassandra (Columnar)

Tablas:
- `process_logs`: logs de ejecución de procesos
- `message_audit`: auditoría de mensajes

---

## 🧠 **Justificación del enfoque Políglota**

| Base de datos | Rol | Justificación |
|----------------|-----|----------------|
| **MySQL** | Datos críticos (usuarios, pagos, facturas) | Soporte ACID, relaciones y consistencia. |
| **MongoDB** | Datos no estructurados (mensajería, alertas) | Alta flexibilidad y volumen de escritura. |
| **Cassandra** | Logs y auditoría | Escalabilidad horizontal y consultas por tiempo. |

---

## 🗃️ **Consultas y scripts de creación de BD**

### 🧱 MySQL
```sql
CREATE DATABASE poliglota_db;
USE poliglota_db;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('USER','ADMIN') DEFAULT 'USER',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  current_balance DECIMAL(10,2) DEFAULT 0,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE invoices (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  issue_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(50),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  invoice_id BIGINT NOT NULL,
  payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  amount DECIMAL(10,2) NOT NULL,
  payment_method VARCHAR(100),
  FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);
```

### 🍃 MongoDB
```javascript
use poliglota_mongo;

db.createCollection('messages');
db.createCollection('groups');
db.createCollection('sensors');
db.createCollection('maintenance_checks');
db.createCollection('alerts');

// Ejemplo de inserción inicial
db.sensors.insertOne({
  name: "Sensor BA-001",
  type: "TEMPERATURE",
  city: "Buenos Aires",
  country: "Argentina",
  active: true,
  startDate: new Date()
});
```

### ⚡ Cassandra
```sql
CREATE KEYSPACE IF NOT EXISTS poliglota_cassandra WITH REPLICATION = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

USE poliglota_cassandra;

CREATE TABLE process_logs (
  process_id UUID PRIMARY KEY,
  name TEXT,
  executed_at TIMESTAMP,
  status TEXT
);

CREATE TABLE message_audit (
  message_id UUID PRIMARY KEY,
  sender_id TEXT,
  recipient_id TEXT,
  timestamp TIMESTAMP,
  content TEXT
);
```

---

## 🧱 **Modelo físico y justificación**

### 🗄️ **MySQL – Modelo Relacional**
- **Entidades principales:** `users`, `accounts`, `invoices`, `payments`, `account_movements_histories`, `processes`.  
- **Relaciones:**  
  - Un `user` tiene una `account`.  
  - Una `account` posee múltiples `account_movements_histories`.  
  - Un `user` puede emitir varias `invoices`.  
  - Cada `invoice` puede tener varios `payments`.  
- **Justificación:**  
  Se eligió MySQL por su estructura relacional y soporte ACID. Permite mantener integridad referencial en operaciones financieras críticas (facturación, pagos, saldo de cuentas).

**Queries MySQL de ejemplo:**
```sql
-- Obtener todas las facturas de un usuario
SELECT * FROM invoices WHERE user_id = 1;

-- Consultar saldo actual de la cuenta de un usuario
SELECT u.full_name, a.current_balance FROM users u 
JOIN accounts a ON u.id = a.user_id;

-- Listar pagos realizados con método específico
SELECT * FROM payments WHERE payment_method = 'TRANSFERENCIA';
```

---

### 🍃 **MongoDB – Modelo Documental**
- **Colecciones:** `messages`, `groups`, `sensors`, `alerts`, `maintenance_checks`.  
- **Estructura:**  
  - `messages` guarda documentos con campos dinámicos (texto, tipo, timestamps).  
  - `sensors` y `alerts` representan datos IoT no estructurados.  
- **Justificación:**  
  MongoDB ofrece flexibilidad para almacenar documentos heterogéneos y facilita consultas rápidas sobre grandes volúmenes de datos, como sensores o mensajería.

**Consultas MongoDB de ejemplo:**
```javascript
// Buscar todos los sensores activos
db.sensors.find({ active: true });

// Obtener mensajes enviados por un usuario específico
db.messages.find({ senderId: 1001 });

// Insertar una nueva alerta
db.alerts.insertOne({
  type: "TEMPERATURE_WARNING",
  state: "ACTIVE",
  sensorId: "SENSOR_001",
  datetime: new Date(),
  description: "Temperatura excede el umbral permitido"
});
```

---

### ⚡ **Cassandra – Modelo Columnar**
- **Tablas:** `process_logs`, `message_audit`.  
- **Estructura:**  
  - `process_logs` indexa procesos por ID con timestamp.  
  - `message_audit` almacena auditoría de mensajes enviados/recibidos.  
- **Justificación:**  
  Cassandra es ideal para escenarios de escritura intensiva y series temporales, asegurando alta disponibilidad y escalabilidad horizontal.

**Queries Cassandra de ejemplo:**
```sql
-- Insertar log de proceso
INSERT INTO process_logs (process_id, name, executed_at, status)
VALUES (uuid(), 'SyncService', toTimestamp(now()), 'SUCCESS');

-- Obtener logs por fecha reciente
SELECT * FROM process_logs WHERE executed_at > toTimestamp(now()) - 86400000;

-- Insertar registro de auditoría de mensaje
INSERT INTO message_audit (message_id, sender_id, recipient_id, timestamp, content)
VALUES (uuid(), '101', '202', toTimestamp(now()), 'Mensaje auditado');
```

---

## 👨‍💻 **Autor**

**Maximiliano Lovato**  
EQUIPO 7- TP Persistencia Políglota  
UADE - Universidad Argentina de la Empresa

