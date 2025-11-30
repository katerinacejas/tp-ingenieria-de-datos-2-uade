-- Crear el keyspace (cámbialo si tu app usa otro)
CREATE KEYSPACE IF NOT EXISTS tpdatos2
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE tpdatos2;

-- Crear la tabla measurements (coincide con tu entidad Measurement)
CREATE TABLE IF NOT EXISTS measurements (
    sensor_id  text,
    timestamp  timestamp,
    temperature double,
    humidity    double,
    PRIMARY KEY (sensor_id, timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);

-- Datos de ejemplo para temperatura en "Buenos Aires" (sensor BA_TEMP_1 y BA_TEMP_2)
INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('BA_TEMP_1', '2025-11-01T10:00:00Z', 22.5, 55.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('BA_TEMP_1', '2025-11-05T14:30:00Z', 25.1, 60.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('BA_TEMP_1', '2025-11-10T09:15:00Z', 19.8, 70.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('BA_TEMP_1', '2025-11-20T18:45:00Z', 27.3, 58.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('BA_TEMP_2', '2025-11-03T08:00:00Z', 21.0, 52.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('BA_TEMP_2', '2025-11-15T16:20:00Z', 26.4, 63.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('BA_TEMP_2', '2025-11-28T22:10:00Z', 23.7, 68.0);

-- Datos de ejemplo para humedad en "Rosario" (sensor ROS_HUM_1 y ROS_HUM_2)
INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('ROS_HUM_1', '2025-10-01T06:30:00Z', 17.0, 80.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('ROS_HUM_1', '2025-10-05T12:00:00Z', 20.5, 75.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('ROS_HUM_1', '2025-10-10T19:45:00Z', 22.2, 85.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('ROS_HUM_1', '2025-10-15T23:50:00Z', 18.9, 78.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('ROS_HUM_2', '2025-10-02T09:00:00Z', 16.5, 72.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('ROS_HUM_2', '2025-10-08T15:30:00Z', 21.3, 88.0);

INSERT INTO measurements (sensor_id, timestamp, temperature, humidity)
VALUES ('ROS_HUM_2', '2025-10-14T20:10:00Z', 19.7, 81.0);
