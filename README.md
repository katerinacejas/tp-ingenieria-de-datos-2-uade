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

- `User`: información y roles
- `Account`: cuenta corriente de usuario
- `Account_movements_histories`: registra los movimientos financieros (créditos y débitos) de cada cuenta.
- `Invoice`: facturas emitidas
- `Payments`: pagos asociados
- `processes`: servicios facturables
- `sessions`: control de sesiones activas
- `rol / rol_entity`: define los permisos y tipos de rol disponibles en el sistema.
- `Processes`: lista los procesos o servicios facturables que los usuarios pueden ejecutar.
- `ProcessRequest`: detalla las solicitudes específicas de ejecución de procesos vinculadas a facturas.
- `ExecutionHistory`: almacena el historial de ejecución de procesos con fecha, estado y duración.

### 🍃 MongoDB (Documental)

Colecciones principales:
- `Message`: mensajes privados o grupales
- `Group`: grupos de usuarios
- `Sensor`: dispositivos IoT
- `Maintenance_checks`: revisiones
- `Alerts`: alertas generadas

### ⚡ Cassandra (Columnar)

Tablas:
- `Measurement`: Permite consultas rápidas por sensor.
- `MeasurementKey`: Mantiene un historial de acciones sobre cada sensor.

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

-- ===========================================
-- Tabla de Roles
-- ===========================================
CREATE TABLE rol_entity (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(255)
);

-- ===========================================
-- Tabla de Usuarios
-- ===========================================
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  status Bool Not NULL,
  rol_id BIGINT,
  registeredAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (rol_id) REFERENCES rol_entity(id)
);

-- ===========================================
-- Tabla de Cuentas Corrientes
-- ===========================================
CREATE TABLE accounts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  current_balance DECIMAL(10,2) DEFAULT 0,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===========================================
-- Tabla de Movimientos de Cuenta
-- ===========================================
CREATE TABLE account_movements_histories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  movement_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- ===========================================
-- Tabla de Procesos (Servicios Facturables)
-- ===========================================
CREATE TABLE processes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  process_type VARCHAR(100),
  cost DECIMAL(10,2) NOT NULL
);

-- ===========================================
-- Tabla de Solicitudes de Proceso
-- ===========================================
CREATE TABLE process_requests (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  process_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  request_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (process_id) REFERENCES processes(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===========================================
-- Historial de Ejecuciones de Procesos
-- ===========================================
CREATE TABLE execution_history (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  process_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  execution_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(50),
  FOREIGN KEY (process_id) REFERENCES processes(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===========================================
-- Tabla de Facturas
-- ===========================================
CREATE TABLE invoices (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  issue_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  status VARCHAR(50),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ===========================================
-- Tabla de Pagos
-- ===========================================
CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  invoice_id BIGINT NOT NULL,
  payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  amount DECIMAL(10,2) NOT NULL,
  payment_method VARCHAR(100),
  FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);

-- ===========================================
-- Tabla de Sesiones de Usuario
-- ===========================================
CREATE TABLE sessions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  rol_id BIGINT,
  start_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  end_time DATETIME NULL,
  status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (rol_id) REFERENCES rol_entity(id)
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

// ===========================================
// Colección de Sensores (IoT)
// ===========================================
db.createCollection("sensors");
db.sensors.insertOne({
  name: "Sensor BA-001",
  type: "TEMPERATURE",
  city: "Buenos Aires",
  country: "Argentina",
  active: true,
  startDate: new Date()
});

// ===========================================
// Colección de Revisiones de Mantenimiento
// ===========================================
db.createCollection("maintenance_checks");
db.maintenance_checks.insertOne({
  checkId: "CHK-001",
  sensorId: "Sensor BA-001",
  reviewDate: new Date(),
  sensorStatus: "OK",
  notes: "Revisión preventiva completada sin incidentes"
});

// ===========================================
// Colección de Alertas
// ===========================================
db.createCollection("alerts");
db.alerts.insertOne({
  alertId: "ALERT-001",
  type: "TEMPERATURE_WARNING",
  state: "ACTIVE",
  sensorId: "Sensor BA-001",
  datetime: new Date(),
  description: "Temperatura superior al umbral permitido"
});

// ===========================================
// Colección de Mensajes (Privados o Grupales)
// ===========================================
db.createCollection("messages");
db.messages.insertOne({
  senderId: 101,
  recipientId: 202,
  timestamp: new Date(),
  content: "Revisión del sensor completada",
  type: "private"
});

// ===========================================
// Colección de Grupos de Usuarios
// ===========================================
db.createCollection("groups");
db.groups.insertOne({
  name: "Equipo Mantenimiento Zona Sur",
  memberIds: [101, 102, 103]
});
```

### ⚡ Cassandra
-- Crear el keyspace
CREATE KEYSPACE IF NOT EXISTS poliglota_cassandra
WITH REPLICATION = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

USE poliglota_cassandra;

-- ===========================================
-- Tabla de mediciones de sensores (IoT)
-- ===========================================
CREATE TABLE IF NOT EXISTS measurements (
    sensor_id TEXT,
    timestamp TIMESTAMP,
    temperature DOUBLE,
    humidity DOUBLE,
    pressure DOUBLE,
    PRIMARY KEY ((sensor_id), timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);

-- ===========================================
-- Tabla de auditoría o respaldo de mediciones
-- ===========================================
CREATE TABLE IF NOT EXISTS measurements_audit (
    sensor_id TEXT,
    timestamp TIMESTAMP,
    action TEXT,
    user_id TEXT,
    notes TEXT,
    PRIMARY KEY ((sensor_id), timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);
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
- **Tablas:** `Measurement`, `MeasurementKey`.  
- **Estructura:**  
 - `Measurement`: Permite consultas rápidas por sensor.
 - `MeasurementKey`: Mantiene un historial de acciones sobre cada sensor.
- **Justificación:**  
  Cassandra es ideal para escenarios de escritura intensiva y series temporales, asegurando alta disponibilidad y escalabilidad horizontal.

**Queries Cassandra de ejemplo:**
-- Insertar nueva medición
INSERT INTO measurements (sensor_id, timestamp, temperature, humidity, pressure)
VALUES ('SENSOR_001', toTimestamp(now()), 23.5, 58.0, 1013.2);

-- Obtener las últimas mediciones de un sensor
SELECT * FROM measurements WHERE sensor_id = 'SENSOR_001' LIMIT 10;

-- Registrar auditoría
INSERT INTO measurements_audit (sensor_id, timestamp, action, user_id, notes)
VALUES ('SENSOR_001', toTimestamp(now()), 'INSERT', '101', 'Nueva lectura registrada');

-- Consultar auditoría reciente
SELECT * FROM measurements_audit WHERE sensor_id = 'SENSOR_001';


---

## 👨‍💻 **Autor**

EQUIPO 7- TP Persistencia Políglota  
UADE - Universidad Argentina de la Empresa

