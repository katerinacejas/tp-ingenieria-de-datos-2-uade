# TP – Persistencia Políglota

### Ingeniería de Datos 2 – UADE

Aplicación de consola (Java / Spring Boot) que implementa **procesos sobre datos de sensores climáticos** usando **persistencia poliglota**:

* **MySQL** para datos relacionales y transaccionales
  (usuarios, procesos, solicitudes, facturas, historial de ejecución, cuentas, pagos, sesiones, roles).
* **MongoDB** para datos documentales y flexibles
  (sensores, mensajes de chat).
* **Cassandra** para datos de serie temporal de alto volumen
  (mediciones de temperatura y humedad).

La aplicación se maneja **100% por consola**:

* Menús distintos según el rol:

  * `USUARIO`
  * `MANTENIMIENTO`
  * `ADMIN`
* Interacción vía `Scanner` y prints en terminal (sin frontend web).

---

## Tabla de contenidos

1. [Objetivo funcional de la aplicación](#1-objetivo-funcional-de-la-aplicación)
2. [Arquitectura de alto nivel](#2-arquitectura-de-alto-nivel)

   * [Tecnologías](#21-tecnologías)
   * [Estructura general de paquetes](#22-estructura-general)
3. [Modelo de dominio (funcional)](#3-modelo-de-dominio-funcional)

   * [Usuarios y roles (MySQL)](#31-usuarios-y-roles-mysql)
   * [Sensores y mediciones](#32-sensores-y-mediciones)
   * [Procesos, solicitudes e historial de ejecución](#33-procesos-solicitudes-e-historial-de-ejecución-mysql)
   * [Facturación, cuenta corriente y pagos](#34-facturación-cuenta-corriente-y-pagos-mysql)
   * [Mensajes (chat) – MongoDB](#35-mensajes-chat-privado-y-grupal--mongodb)
   * [Alertas](#36-alertas)
4. [Distribución de entidades por motor de base de datos](#4-distribución-de-entidades-por-motor-de-base-de-datos)
5. [Flujos funcionales importantes](#5-flujos-funcionales-importantes)

   * [Registro, login y sesiones](#51-registro-login-y-sesiones)
   * [Flujo completo solicitud → ejecución](#52-flujo-completo-de-solicitud-y-ejecución-de-un-proceso)
   * [Cálculo min/max/promedio (`MeasurementService.aggregate`)](#53-cálculo-de-minmaxpromedio-measurementserviceaggregate)
   * [Facturación y cuenta corriente](#54-facturación-y-cuenta-corriente)
   * [Mensajes (chat)](#55-mensajes-chat)
   * [Gestión de sensores](#56-gestión-de-sensores)
6. [DataInitializer: carga de datos de prueba](#6-datainitializer-carga-de-datos-para-pruebas)
7. [Justificación de la persistencia poliglota](#7-justificación-de-la-persistencia-poliglota)
8. [Puntos clave para la defensa](#8-puntos-clave-para-la-defensa)

---

## 1. Objetivo funcional de la aplicación

La aplicación permite que distintos tipos de usuarios interactúen con un sistema de sensores distribuidos geográficamente:

* Dar de alta y administrar **sensores**.
* Recibir y persistir **mediciones** de temperatura y humedad.
* Definir **procesos** que se ejecutan sobre los datos históricos (máximo, mínimo, promedio; anual o mensual).
* Permitir que un **USUARIO** solicite la ejecución de procesos parametrizados:

  * ciudad
  * país
  * rango de fechas
  * tipo de agrupación (anual / mensual)
* Que un perfil de **MANTENIMIENTO** apruebe y ejecute esas solicitudes, generando:

  * **Historial de ejecución**.
  * **Facturas** asociadas a cada solicitud.
* Llevar una **cuenta corriente**, facturas, pagos e historial de movimientos.
* Enviar **mensajes privados y grupales** entre usuarios.
* Gestionar **alertas climáticas y de sensores**.

Todo esto **coordinando** el uso de **MySQL**, **MongoDB** y **Cassandra**.

---

## 2. Arquitectura de alto nivel

### 2.1. Tecnologías

**Backend:**

* Java 17
* Spring Boot 3
* Spring Data JPA (MySQL)
* Spring Data MongoDB
* Spring Data Cassandra
* Spring Security (gestión básica de autenticación y roles)

**Persistencia:**

* **MySQL** (modelo relacional)
* **MongoDB** (modelo documental)
* **Cassandra** (serie temporal)

**CLI (Consola):**

* Clases de vista:

  * `VistaCompartida`
  * `VistaUsuario`
  * `VistaMantenimiento`
  * `VistaAdministrador`
* Menús por consola según rol conectado.

### 2.2. Estructura general

* `com.poliglota.model.mysql`
  Entidades JPA (MySQL).

* `com.poliglota.model.mongo`
  Documentos Mongo (sensores, mensajes de chat).

* `com.poliglota.model.cassandra`
  Tablas Cassandra (mediciones).

* `com.poliglota.service`
  Lógica de negocio (usuarios, procesos, mediciones, autenticación, etc.).

* `com.poliglota.repository.jpa`, `mongo`, `cassandra`
  Repositorios por tecnología de base de datos.

* `com.poliglota.vista`
  Vistas de consola (menús según rol).

* `com.poliglota.config.DataInitializer`
  Carga de datos de prueba (sensores, mediciones, procesos, roles).

---

## 3. Modelo de dominio (funcional)

### 3.1. Usuarios y roles (MySQL)

**User**

* `userId`
* `fullName`
* `email`
* `password`
* `status` (activo / inactivo)
* `registeredAt`
* `rol` (enum `Rol`)
* `rolEntity` (relación con `RolEntity`)

**RolEntity**

* `rolEntityId`
* `code` → `USUARIO`, `MANTENIMIENTO`, `ADMIN`
* `descripcion`

**Session**

* `sessionId`
* `user` (User)
* `rol` (Rol)
* `startTime`
* `endTime`
* `status` (activa / inactiva)

**Flujo:**

* El usuario se registra, se le asigna el rol `USUARIO` y se crea su cuenta corriente.
* Al iniciar sesión se crea una `Session` con estado `activa`.
* Al cerrar sesión se marcan tanto el usuario como la sesión como `inactivos` / `endTime` completado.

---

### 3.2. Sensores y mediciones

#### Sensores (MongoDB)

Colección `sensors`:

* `Sensor`

  * `id` (String) → Ej: `SENSOR_AR_BA_1`
  * `name`
  * `type` (`TEMP_HUM`)
  * `latitud`, `longitud`
  * `city`
  * `country`
  * `estado` (`ACTIVO` / `INACTIVO`)
  * `startDate` (inicio de funcionamiento)

Los sensores se consultan por ciudad y país.
El rol `MANTENIMIENTO` puede cambiar el estado (`ACTIVO`/`INACTIVO`).

#### Mediciones (Cassandra)

**MeasurementKey**

* `sensorId` (partition key)
* `timestamp` (clustered, ordenado DESC)

**Measurement**

* `key` (MeasurementKey)
* `temperature`
* `humidity`

Este diseño permite:

* Filtrar mediciones por sensor y rango de fechas.
* Agrupar y calcular **max**, **min**, **promedio** por **año** o **mes**.

---

### 3.3. Procesos, solicitudes e historial de ejecución (MySQL)

#### Process

* `processId`
* `name` (ejemplos):

  * `INFORME_HUMEDAD_MAX_ANUAL`
  * `INFORME_TEMPERATURA_PROMEDIO_MENSUAL`
* `description`
* `processType` (`MAXIMO`, `MINIMO`, `PROMEDIO`)
* `cost` (costo del proceso)

Los procesos base se crean en `DataInitializer`.
El administrador puede verlos y crear nuevos procesos.

#### ProcessRequest

* `processRequestId`
* `user` (User que la solicita)
* `process` (Process que se quiere ejecutar)
* `requestDate`
* `status` (`PENDIENTE`, `COMPLETADA`, etc.)
* `invoice` (OneToOne con `Invoice`)

**Parámetros funcionales de la solicitud:**

* `city`
* `country`
* `startDate` (LocalDate)
* `endDate` (LocalDate)
* `agrupacionDeDatos` (`ANUAL` / `MENSUAL`)

#### ExecutionHistory

* `executionId`
* `processRequest` (ManyToOne)
* `executionDate`
* `result` (String grande, tabla ASCII con `|` y `---`)
* `status` (`OK`, `ERROR`, etc.)

Cada vez que **MANTENIMIENTO** ejecuta una solicitud, se crea un registro en `ExecutionHistory` con el **resultado formateado**, listo para mostrar en consola.

---

### 3.4. Facturación, cuenta corriente y pagos (MySQL)

**Account**

* `accountId`
* `user` (User)
* `currentBalance`

**Invoice**

* `invoiceId`
* `issueDate`
* `status` (`PENDIENTE` / `PAGADA`)
* `amount`
* `billedProcessRequest` (id de `ProcessRequest` asociada)

**Payment**

* `paymentId`
* `account`
* `invoice`
* `paymentDate`
* `amount`
* `type` (ej: “pago de factura”, “depósito”)

**Desde el USUARIO:**

* Ver facturas pendientes y pagadas.
* Pagar una factura (si tiene saldo suficiente).
* Ver cuenta corriente e historial de movimientos.

**Desde ADMIN:**

* Ver todas las facturas de todos los usuarios.
* Ver total facturado (facturas `PAGADAS`).
* Ver total de deuda (facturas `PENDIENTES`).

---

### 3.5. Mensajes (chat privado y grupal) – MongoDB

Documento `Message`:

* `id`
* `type` (`user` / `group`)
* `senderId`
* `recipientId`

  * `type = user` → id del otro usuario
  * `type = group` → id del grupo
* `content`
* `timestamp`

**Repositorio – conversación directa:**

```java
@Query(value = "{ 'type': 'user', $or: [ " +
        "{ 'senderId': ?0, 'recipientId': ?1 }, " +
        "{ 'senderId': ?2, 'recipientId': ?3 } " +
        "] }")
List<Message> findDirectConversation(Long userA, String userBStr, Long userB, String userAStr, Sort sort);
```

Devuelve toda la **conversación directa** entre dos usuarios (A↔B), ordenada por `timestamp`.

**Repositorio – chat grupal:**

```java
@Query(value = "{ 'type': 'group', 'recipientId': ?0 }")
List<Message> findByGroupId(String groupId, Sort sort);
```

Devuelve todos los mensajes de un grupo.

---

### 3.6. Alertas

Conceptualmente se manejan dos tipos de alertas:

* **Alertas climáticas**
  (por condiciones extremas en una ciudad/país).

* **Alertas de sensores**
  (sensor caído, inactivo, etc.).

* El **USUARIO** puede ver **alertas climáticas activas/resueltas** relacionadas con su zona.

* El **MANTENIMIENTO** es quien:

  * Resuelve alertas.
  * Cambia el estado de los sensores.

---

## 4. Distribución de entidades por motor de base de datos

| Entidad / Tipo              | Motor     | Justificación                                                                     |
| --------------------------- | --------- | --------------------------------------------------------------------------------- |
| User, RolEntity, Session    | MySQL     | Datos fuertemente relacionales, integridad y joins (usuarios, sesiones, roles).   |
| Process, ProcessRequest     | MySQL     | Consistencia en el flujo de negocio, relación con usuarios, facturas e historial. |
| Invoice, Account, Payment   | MySQL     | Lógica financiera y contable, ideal para modelo relacional.                       |
| ExecutionHistory            | MySQL     | Historial asociado a solicitudes, fácil de consultar junto a `ProcessRequest`.    |
| Alert (climática/sensor)    | MySQL     | Parte del modelo transaccional, asociado a usuarios/procesos/sensores.            |
| Sensor                      | MongoDB   | Datos semi-estructurados y flexibles, fácil de ubicar por ciudad/país, escalable. |
| Message (chat user/group)   | MongoDB   | Mensajería documental, estructura flexible y volumen medio/alto.                  |
| Measurement, MeasurementKey | Cassandra | Serie temporal de alto volumen, optimizada para `sensorId + rango de fechas`.     |

Esta división responde al objetivo de usar **persistencia poliglota**, eligiendo para cada tipo de dato el motor más adecuado.

### **Por qué Cassandra**

1: **Son series de tiempo y de alto volumen**

*   Los sensores pueden emitir lecturas cada pocos segundos/minutos.
    
*   A lo largo del tiempo, se acumulan millones de filas.
    
*   Necesitás escritura rápida y lecturas por rangos de tiempo.
    

2: **Patrón de acceso simple y repetitivo**

*   Consultas típicas:
    
    *   “dame las mediciones del sensor X entre fecha A y B”
        
    *   “dame todas las mediciones de los sensores de una zona en un rango de fechas” (normalmente lo resolvés por sensorId+fecha y después agrupás a nivel aplicación/proceso).
        

3: **Cassandra está optimizada para eso**

*   Modelo orientado a particiones (MeasurementKey), por ejemplo:
    
    *   partición por sensorId + “año/mes”,
        
    *   clustering por timestamp.
        
### **Por qué Mongo:**

*   Los sensores no son datos tabulares rígidos, sino **documentos** con estructura que puede evolucionar. En Mongo guardamos un documento por sensor con toda su información descriptiva.
    
*   La estructura puede crecer o cambiar con el tiempo.
    
*   No necesitás muchos joins.
    
*   Podés embebar info del sensor, observaciones, etc.
    
*   Estructura semi-flexible (texto, opcionalmente adjuntos, metadata).
    
*   No necesitás integridad referencial súper estricta (si se borra un usuario, querés mantener el historial igual).
    
*   Posible alto volumen, pero no tan rígido como contabilidad.
    

Conclusion: En Mongo guardamos **documentos** que representan entidades semi-estructuradas: sensores, controles de mantenimiento, alertas y mensajes. Son datos que no requieren joins complejos pero sí flexibilidad en la estructura, por eso elegimos Mongo

### **Por qué MySQL:**

*   Requiere **integridad referencial**:
    
    *   Cada sesión pertenece a un usuario.
        
    *   Cada usuario tiene uno o más roles.
        
*   Consultas típicas:
    
    *   usuarios por rol,
        
    *   sesiones activas,
        
    *   quién está logueado y con qué rol.
        

Todo lo que es **gestión de usuarios, roles y sesiones** lo pusimos en MySQL porque es fuertemente relacional y requiere integridad y transaccionesongo


---

## 5. Flujos funcionales importantes

### 5.1. Registro, login y sesiones

1. **Registro (Vista compartida)**

   * El usuario completa:

     * nombre
     * email
     * password
   * Se le asigna automáticamente el rol `USUARIO`.
   * Se crea una `Account` con saldo 0.

2. **Login**

   * Se valida la contraseña con `PasswordEncoder`.
   * Se crea una `Session` con:

     * `user`
     * `rol`
     * `startTime = now`
     * `status = "activa"`.

3. **Logout**

   * Se marca el `status` del usuario como `inactivo`.
   * Se busca la sesión activa y se actualiza:

     * `endTime = now`
     * `status = "inactiva"`.

---

### 5.2. Flujo completo de solicitud y ejecución de un proceso

#### 5.2.1. Creación de una solicitud de proceso (USUARIO)

Menú `USUARIO` → “Crear solicitud de proceso”:

1. Se muestra el **catálogo de procesos**:

   ```text
   Id: 8
   Nombre: INFORME_TEMPERATURA_MIN_ANUAL
   Descripcion : ...
   Tipo: MINIMO
   Costo: 100.0
   ```

2. El usuario elige un `processId`.

3. La vista pide los parámetros:

   * Ciudad (`city`) → ej: `Cordoba`
   * País (`country`) → ej: `AR`
   * Fecha de inicio (`startDate`) → `yyyy-MM-dd`
   * Fecha de fin (`endDate`) → `yyyy-MM-dd`
   * Agrupación (`agrupacionDeDatos`) → `ANUAL` / `MENSUAL`

4. La vista arma un `ProcessRequestDTO` y llama a `ProcessRequestController.createProcessRequest`.

5. En `ProcessRequestService.createProcessRequest`:

   * Buscar `User` por id.
   * Buscar `Process` por id.
   * Crear una `Invoice`:

     * `issueDate = today`
     * `status = "PENDIENTE"`
     * `amount = process.cost`
   * Persistir la factura.
   * Crear `ProcessRequest` con:

     * `user`, `process`
     * `requestDate = now`
     * `status = "PENDIENTE"`
     * `city`, `country`, `startDate`, `endDate`
     * `agrupacionDeDatos`
     * `invoice` (OneToOne)
   * Guardar la solicitud.
   * Actualizar la factura con `billedProcessRequest = id de la solicitud`.

6. La solicitud queda registrada como **PENDIENTE** para ser tratada por `MANTENIMIENTO`.

---

#### 5.2.2. Aprobación y ejecución de la solicitud (MANTENIMIENTO)

Menú `MANTENIMIENTO` → “Aprobar y ejecutar solicitud de proceso”:

1. Se buscan todas las `ProcessRequest` con `status = "PENDIENTE"`:

   ```java
   List<ProcessRequestDTO> lista = processRequestController
       .getProcessRequestByStatus("PENDIENTE").getBody();
   ```

2. Se muestran en consola:

   * Id de la solicitud
   * Usuario solicitante
   * Nombre y descripción del proceso
   * Tipo de proceso
   * Estado
   * Fecha de solicitud
   * Parámetros (ciudad, país, fechas, agrupación)

3. MANTENIMIENTO elige la solicitud a aprobar.

4. Se actualiza el `status` de la solicitud (ej. `"COMPLETADA"` o `"APROBADA"` + `"EJECUTADA"` según el diseño).

5. **Ejecución del proceso**:

   * Se decide qué métrica y operación usar según el nombre del proceso:

     * Si contiene `"HUMEDAD"` → métrica = `HUMEDAD`, si no → `TEMPERATURA`.
     * Si contiene `"MAX"` → operación = `MAXIMO`.
     * Si contiene `"MIN"` → operación = `MINIMO`.
     * Si contiene `"PROMEDIO"` → operación = `PROMEDIO`.
     * Si contiene `"ANUAL"` → granularidad = `ANUAL`.
     * Si contiene `"MENSUAL"` → granularidad = `MENSUAL`.
   * Se invoca `MeasurementService.aggregate(...)` con:

     * `metric`
     * `operation`
     * `granularity`
     * `city`, `country`
     * `startDate`, `endDate` (de la solicitud)

6. `aggregate` devuelve una lista de `AggregatedResult` (período, valor).
   Se convierte a **tabla ASCII** con `|` y `---` para:

   * mostrar en consola
   * guardar en `ExecutionHistory.result` como un solo String

7. Se crea `ExecutionHistory` con:

   * `processRequest`
   * `executionDate = now`
   * `result` (tabla String)
   * `status = "OK"`

8. El usuario puede ir al menú “Ver resultados de ejecución de mis solicitudes” y ver los `ExecutionHistory` asociados a sus `ProcessRequest`.

---

### 5.3. Cálculo de min/max/promedio: `MeasurementService.aggregate`

Este método es el **núcleo numérico** de los procesos.

```java
public List<AggregatedResult> aggregate(
        String metric,
        String operation,
        String granularity,
        String city,
        String country,
        LocalDate startDate,
        LocalDate endDate
) {
    String metricUp = metric.toUpperCase(Locale.ROOT);
    String opUp = operation.toUpperCase(Locale.ROOT);
    String granUp = granularity.toUpperCase(Locale.ROOT);

    // 1) Buscar sensores en Mongo por ciudad y país
    List<Sensor> sensores = sensorRepository.findByCityAndCountry(city, country);
    if (sensores.isEmpty()) return Collections.emptyList();

    // 2) Traer mediciones de Cassandra para esos sensores en el rango de fechas
    List<Measurement> todasLasMediciones = new ArrayList<>();
    for (Sensor s : sensores) {
        String sensorId = s.getId();
        List<Measurement> medicionesSensor =
            measurementRepository.findByKeySensorIdAndKeyTimestampBetween(sensorId, startDate, endDate);
        todasLasMediciones.addAll(medicionesSensor);
    }
    if (todasLasMediciones.isEmpty()) return Collections.emptyList();

    // 3) Agrupar por periodo (ANUAL o MENSUAL)
    Map<String, List<Measurement>> medicionesPorPeriodo = todasLasMediciones.stream()
            .collect(Collectors.groupingBy(m -> {
                LocalDate fecha = m.getKey().getTimestamp();
                if ("ANUAL".equalsIgnoreCase(granUp)) {
                    return String.valueOf(fecha.getYear());
                } else {
                    return fecha.getYear() + "-" + String.format("%02d", fecha.getMonthValue());
                }
            }));

    List<AggregatedResult> resultados = new ArrayList<>();

    for (Map.Entry<String, List<Measurement>> entry : medicionesPorPeriodo.entrySet()) {
        String periodo = entry.getKey();
        List<Measurement> medicionesPeriodo = entry.getValue();

        // 4) Elegir la métrica (temperatura o humedad)
        List<Double> valores = medicionesPeriodo.stream()
                .map(m -> "HUMEDAD".equals(metricUp) ? m.getHumidity() : m.getTemperature())
                .filter(Objects::nonNull)
                .toList();

        if (valores.isEmpty()) continue;

        // 5) Calcular max/min/promedio
        double valorAgregado;
        switch (opUp) {
            case "MAXIMO" -> valorAgregado = valores.stream().max(Double::compareTo).orElseThrow();
            case "MINIMO" -> valorAgregado = valores.stream().min(Double::compareTo).orElseThrow();
            case "PROMEDIO" -> {
                double suma = valores.stream().mapToDouble(Double::doubleValue).sum();
                valorAgregado = suma / valores.size();
            }
            default -> throw new IllegalArgumentException("Operación no soportada: " + operation);
        }

        resultados.add(new AggregatedResult(periodo, valorAgregado));
    }

    // 6) Ordenar por periodo
    resultados.sort(Comparator.comparing(AggregatedResult::getPeriod));

    return resultados;
}
```

**Resumen para la defensa:**

* Usa **sensores** desde Mongo y **mediciones** desde Cassandra.
* Agrupa por **año** o **año-mes** según la granularidad.
* Aplica la operación (**max**, **min**, **promedio**) sobre la métrica elegida (**temperatura** o **humedad**).
* Devuelve una lista ordenada por período, que luego se transforma en una tabla ASCII.

---

### 5.4. Facturación y cuenta corriente

* Cada `ProcessRequest` genera una `Invoice` en estado `PENDIENTE` con `amount = cost del proceso`.
* El USUARIO puede:

  * Ver sus facturas pendientes / pagadas.
  * Seleccionar una factura y pagarla (si el saldo en `Account.currentBalance` alcanza).
* Al pagar:

  * Se descuenta el importe de la cuenta (`currentBalance`).
  * La `Invoice` pasa a `PAGADA`.
  * Se registra un `Payment` asociado a la `Account` y a la `Invoice`.
* El ADMIN puede:

  * Ver todas las facturas.
  * Ver el **total facturado** (sumatoria de facturas `PAGADAS`).
  * Ver el **total de deuda** (sumatoria de facturas `PENDIENTES`).

---

### 5.5. Mensajes (chat)

* Los mensajes se guardan en Mongo (colección `Message`).

**Chat privado:**

* Se usa `findDirectConversation(...)`, buscando mensajes donde:

  * `(senderId = A AND recipientId = B)`
  * o `(senderId = B AND recipientId = A)`
* Siempre con `type = 'user'`.

**Chat grupal:**

* Se usa `findByGroupId(groupId, sort)` con `type = 'group'`.

**Módulo de chat en el menú:**

* Enviar mensajes privados a otro usuario.
* Participar en grupos (si está implementado).
* Ver historial de mensajes.

---

### 5.6. Gestión de sensores

* El rol `MANTENIMIENTO` puede:

  * Ver todos los sensores:

    * id, ciudad, país, estado, fecha de inicio.
  * Cambiar el estado de un sensor:

    ```java
    public SensorDTO toggleSensorStatus(String id, String estado) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor no encontrado con ID: " + id));
        sensor.setEstado(estado);
        return toDto(sensorRepository.save(sensor));
    }
    ```

Esto se relaciona con el **control de funcionamiento** de los sensores (si se detecta un problema, se pasa a `INACTIVO`).

---

## 6. DataInitializer: carga de datos para pruebas

La clase `DataInitializer` (implementa `CommandLineRunner`) se ejecuta al arrancar la app y:

1. **Crea roles por defecto** si no existen:

   * `USUARIO`
   * `MANTENIMIENTO`
   * `ADMIN`

2. **Crea sensores de prueba** en varias ciudades y países:

   * Buenos Aires, Córdoba, Rosario (AR)
   * Rio de Janeiro, Sao Paulo (BR)
   * Madrid (ES)

3. **Genera mediciones dummy** para los últimos 3 años:

   * Una medición por día y por sensor.
   * Rangos de temperatura y humedad distintos según la ciudad para que los informes sean “realistas”.

4. **Crea procesos por defecto**:

   * Procesos de **HUMEDAD**:

     * max, min, promedio (anual / mensual)
   * Procesos de **TEMPERATURA**:

     * max, min, promedio (anual / mensual)

Gracias a esto, se pueden probar los informes sin cargar datos manualmente.

---

## 7. Justificación de la persistencia poliglota

**MySQL**

* Ideal para:

  * Usuarios, roles, sesiones.
  * Procesos, solicitudes, facturas, pagos, cuentas.
  * Historial de ejecución.
* Necesidad de **integridad referencial** y relaciones claras (OneToOne, ManyToOne, etc.).

**MongoDB**

* Sensores:

  * Estructura flexible (se pueden agregar o cambiar atributos sin romper el esquema).
* Mensajería:

  * Documentos distintos para privado y grupal.
  * Volumen moderado de datos.
* Fácil de consultar por ciudad/país y luego cruzar con Cassandra para las mediciones.

**Cassandra**

* Diseñado para:

  * Grandes volúmenes de mediciones de sensores.
  * Consultas por `sensorId` y rango de fechas.
* El modelo:

  * `MeasurementKey(sensorId, timestamp)`
    permite lecturas eficientes y agregaciones por periodo.

En la defensa es importante remarcar que **cada tipo de dato está en el motor más adecuado** según su patrón de acceso y naturaleza.

---

## 8. Puntos clave para la defensa

* **Roles y permisos:**

  * `USUARIO`

    * Solicita procesos.
    * Ve sus resultados.
    * Maneja cuenta, facturas.
    * Ve alertas climáticas.
    * Usa el chat.
  * `MANTENIMIENTO`

    * Aprueba y ejecuta procesos.
    * Administra sensores.
    * Resuelve alertas.
    * Usa el chat.
  * `ADMIN`

    * Ve todas las facturas.
    * Consulta total facturado y deuda total.
    * Puede crear procesos y usuarios de mantenimiento.

* **Flujo completo solicitud → ejecución → historial → factura:**

  1: Usuario crea una solicitud de proceso con todos los parámetros.
  2: Se genera una factura `PENDIENTE`.
  3: MANTENIMIENTO aprueba y ejecuta la solicitud.
  4: Se calculan agregados con `MeasurementService.aggregate`.
  5: Se guarda un `ExecutionHistory` con el resultado en formato tabla ASCII.
  6: El usuario consulta el resultado y la factura correspondiente para luego pagarla.

* **Uso de Cassandra para mediciones:**

  * Explicar por qué Cassandra es adecuado para:

    * volumen de datos
    * consultas por `sensorId + rango de fechas`
  * Mostrar cómo el diseño del `MeasurementKey` favorece el tipo de query que hace `aggregate`.

* **DataInitializer como herramienta de demo:**

  * Permite arrancar la aplicación, loguearse y **mostrar informes en vivo**.
  * Facilita mostrar:

    * sensores
    * mediciones
    * ejecución de procesos
    * facturación y pagos

* **Resultado del proceso guardado como string (tabla ASCII):**

  * Se guarda exactamente lo que el usuario ve en consola.
  * Sirve como:

    * auditoría del cálculo
    * facilidad de visualización sin necesidad de otra capa de presentación.

---
