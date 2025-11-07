# tp-ingenieria-de-datos-2-uade
Trabajo practico de Ingeniería de Datos 2 de UADE


Asegurar que los servicios estén activos

Iniciá tus motores de base de datos localmente:

# MySQL
sudo service mysql start
# MongoDB
sudo service mongod start
# Cassandra
sudo service cassandra start

para Ejecutar backend 

mvn spring-boot:run


Probar endpoints con Postman:

POST http://localhost:8080/api/sensors

{
  "name": "Sensor BA-001",
  "type": "TEMPERATURE",
  "city": "Buenos Aires",
  "country": "Argentina"
}


GET http://localhost:8080/api/sensors

POST http://localhost:8080/api/measurements

{
  "key": {
    "sensorId": "SENSOR_001",
    "timestamp": "2025-10-13T18:00:00Z"
  },
  "temperature": 21.5,
  "humidity": 65.0
}
