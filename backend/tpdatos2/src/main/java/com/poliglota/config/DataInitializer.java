package com.poliglota.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.poliglota.DTO.MeasurementDTO;
import com.poliglota.model.mongo.Sensor;
import com.poliglota.model.mysql.Process;
import com.poliglota.model.mysql.Rol;
import com.poliglota.model.mysql.RolEntity;
import com.poliglota.repository.mongo.SensorRepository;
import com.poliglota.repository.jpa.ProcessRepository;
import com.poliglota.repository.jpa.RolRepository;
import com.poliglota.service.MeasurementService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(1)   // corre primero
public class DataInitializer implements CommandLineRunner {

    private final SensorRepository sensorRepository;
    private final MeasurementService measurementService;
    private final ProcessRepository processRepository;
    private final RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        if (sensorRepository.count() == 0) {
            crearSensoresPorDefecto();
        }

        crearMedicionesDummy();

        crearRolesPorDefecto();        
        crearProcesosPorDefecto();    
    }

    private void crearRolesPorDefecto() {
        crearRolSiNoExiste(Rol.USUARIO, "Rol de usuario final que solicita procesos");
        crearRolSiNoExiste(Rol.MANTENIMIENTO, "Rol de mantenimiento que administra sensores y ejecuta procesos");
        crearRolSiNoExiste(Rol.ADMIN, "Rol administrador del sistema");
        System.out.println(">>> Roles por defecto creados/actualizados");
    }

    private void crearRolSiNoExiste(Rol code, String descripcion) {
        rolRepository.findByCode(code).orElseGet(() -> {
            RolEntity rol = new RolEntity();
            rol.setCode(code);
            rol.setDescripcion(descripcion);
            return rolRepository.save(rol);
        });
    }

    private void crearSensoresPorDefecto() {
        Sensor ba1 = new Sensor();
        ba1.setId("SENSOR_AR_BA_1");
        ba1.setName("Sensor Buenos Aires 1");
        ba1.setType("TEMP_HUM");
        ba1.setLatitud(-34.6037);
        ba1.setLongitud(-58.3816);
        ba1.setCity("Buenos Aires");
        ba1.setCountry("AR");
        ba1.setEstado("ACTIVO");
        ba1.setStartDate(LocalDateTime.now().minusYears(3));

        Sensor ba2 = new Sensor();
        ba2.setId("SENSOR_AR_BA_2");
        ba2.setName("Sensor Buenos Aires 2");
        ba2.setType("TEMP_HUM");
        ba2.setLatitud(-34.60);
        ba2.setLongitud(-58.40);
        ba2.setCity("Buenos Aires");
        ba2.setCountry("AR");
        ba2.setEstado("ACTIVO");
        ba2.setStartDate(LocalDateTime.now().minusYears(2));

        Sensor cba1 = new Sensor();
        cba1.setId("SENSOR_AR_CBA_1");
        cba1.setName("Sensor Córdoba 1");
        cba1.setType("TEMP_HUM");
        cba1.setLatitud(-31.4167);
        cba1.setLongitud(-64.1833);
        cba1.setCity("Cordoba");
        cba1.setCountry("AR");
        cba1.setEstado("ACTIVO");
        cba1.setStartDate(LocalDateTime.now().minusYears(3));

        Sensor cba2 = new Sensor();
        cba2.setId("SENSOR_AR_CBA_2");
        cba2.setName("Sensor Córdoba 2");
        cba2.setType("TEMP_HUM");
        cba2.setLatitud(-31.42);
        cba2.setLongitud(-64.19);
        cba2.setCity("Cordoba");
        cba2.setCountry("AR");
        cba2.setEstado("ACTIVO");
        cba2.setStartDate(LocalDateTime.now().minusYears(1));

        Sensor ros1 = new Sensor();
        ros1.setId("SENSOR_AR_ROS_1");
        ros1.setName("Sensor Rosario 1");
        ros1.setType("TEMP_HUM");
        ros1.setLatitud(-32.95);
        ros1.setLongitud(-60.66);
        ros1.setCity("Rosario");
        ros1.setCountry("AR");
        ros1.setEstado("ACTIVO");
        ros1.setStartDate(LocalDateTime.now().minusYears(2));

        Sensor rio1 = new Sensor();
        rio1.setId("SENSOR_BR_RIO_1");
        rio1.setName("Sensor Rio de Janeiro 1");
        rio1.setType("TEMP_HUM");
        rio1.setLatitud(-22.9068);
        rio1.setLongitud(-43.1729);
        rio1.setCity("Rio de Janeiro");
        rio1.setCountry("BR");
        rio1.setEstado("ACTIVO");
        rio1.setStartDate(LocalDateTime.now().minusYears(3));

        Sensor rio2 = new Sensor();
        rio2.setId("SENSOR_BR_RIO_2");
        rio2.setName("Sensor Rio de Janeiro 2");
        rio2.setType("TEMP_HUM");
        rio2.setLatitud(-22.90);
        rio2.setLongitud(-43.20);
        rio2.setCity("Rio de Janeiro");
        rio2.setCountry("BR");
        rio2.setEstado("ACTIVO");
        rio2.setStartDate(LocalDateTime.now().minusYears(2));

        Sensor sp1 = new Sensor();
        sp1.setId("SENSOR_BR_SP_1");
        sp1.setName("Sensor Sao Paulo 1");
        sp1.setType("TEMP_HUM");
        sp1.setLatitud(-23.55);
        sp1.setLongitud(-46.63);
        sp1.setCity("Sao Paulo");
        sp1.setCountry("BR");
        sp1.setEstado("ACTIVO");
        sp1.setStartDate(LocalDateTime.now().minusYears(3));

        Sensor mad1 = new Sensor();
        mad1.setId("SENSOR_ES_MAD_1");
        mad1.setName("Sensor Madrid 1");
        mad1.setType("TEMP_HUM");
        mad1.setLatitud(40.4168);
        mad1.setLongitud(-3.7038);
        mad1.setCity("Madrid");
        mad1.setCountry("ES");
        mad1.setEstado("ACTIVO");
        mad1.setStartDate(LocalDateTime.now().minusYears(3));

        sensorRepository.saveAll(List.of(
                ba1, ba2,
                cba1, cba2,
                ros1,
                rio1, rio2,
                sp1,
                mad1
        ));

        System.out.println(">>> Sensores de prueba creados (AR, BR, ES)");
    }

    private void crearMedicionesDummy() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusYears(3);

        List<Sensor> sensores = sensorRepository.findAll();
        if (sensores.isEmpty()) {
            System.out.println(">>> No hay sensores para crear mediciones.");
            return;
        }

        for (Sensor s : sensores) {
            double tempMin;
            double tempMax;
            double humMin;
            double humMax;

            switch (s.getCity() + "|" + s.getCountry()) {
                case "Buenos Aires|AR" -> {
                    tempMin = 8; tempMax = 35;
                    humMin = 40; humMax = 90;
                }
                case "Cordoba|AR" -> {
                    tempMin = 5; tempMax = 32;
                    humMin = 30; humMax = 80;
                }
                case "Rosario|AR" -> {
                    tempMin = 6; tempMax = 33;
                    humMin = 35; humMax = 85;
                }
                case "Rio de Janeiro|BR" -> {
                    tempMin = 20; tempMax = 38;
                    humMin = 60; humMax = 95;
                }
                case "Sao Paulo|BR" -> {
                    tempMin = 18; tempMax = 34;
                    humMin = 55; humMax = 90;
                }
                case "Madrid|ES" -> {
                    tempMin = 0; tempMax = 35;
                    humMin = 20; humMax = 70;
                }
                default -> {
                    tempMin = 10; tempMax = 30;
                    humMin = 30; humMax = 80;
                }
            }

            crearMedicionesParaSensor(s.getId(), start, end, tempMin, tempMax, humMin, humMax);
        }

        System.out.println(">>> Mediciones dummy (3 años, múltiples ciudades/países) creadas en Cassandra");
    }

    private void crearMedicionesParaSensor(
            String sensorId,
            LocalDate start,
            LocalDate end,
            double tempMin,
            double tempMax,
            double humMin,
            double humMax
    ) {
        for (LocalDate fecha = start; !fecha.isAfter(end); fecha = fecha.plusDays(1)) {
            MeasurementDTO dto = new MeasurementDTO();
            dto.sensorId = sensorId;
            dto.timestamp = fecha;
            dto.temperature = randomInRange(tempMin, tempMax);
            dto.humidity = randomInRange(humMin, humMax);
            measurementService.save(dto);
        }
    }

    private double randomInRange(double min, double max) {
        return min + Math.random() * (max - min);
    }

    private void crearProcesosPorDefecto() {
        crearProcesoSiNoExiste("INFORME_HUMEDAD_MAX_ANUAL",
                "Informe de humedad maxima por ciudad, pais y en un rango de fechas, anualizada",
                "MAXIMO", 100);

        crearProcesoSiNoExiste("INFORME_HUMEDAD_MIN_ANUAL",
                "Informe de humedad minima por ciudad, pais y en un rango de fechas, anualizada",
                "MINIMO", 100);

        crearProcesoSiNoExiste("INFORME_HUMEDAD_PROMEDIO_ANUAL",
                "Informe de humedad promedio por ciudad, pais y en un rango de fechas, anualizada",
                "PROMEDIO", 100);

        crearProcesoSiNoExiste("INFORME_HUMEDAD_MAX_MENSUAL",
                "Informe de humedad maxima por ciudad, pais y en un rango de fechas, mensualizada",
                "MAXIMO", 150);

        crearProcesoSiNoExiste("INFORME_HUMEDAD_MIN_MENSUAL",
                "Informe de humedad minima por ciudad, pais y en un rango de fechas, mensualizada",
                "MINIMO", 150);

        crearProcesoSiNoExiste("INFORME_HUMEDAD_PROMEDIO_MENSUAL",
                "Informe de humedad promedio por ciudad, pais y en un rango de fechas, mensualizada",
                "PROMEDIO", 150);

        crearProcesoSiNoExiste("INFORME_TEMPERATURA_MAX_ANUAL",
                "Informe de temperatura maxima por ciudad, pais y en un rango de fechas, anualizada",
                "MAXIMO", 100);

        crearProcesoSiNoExiste("INFORME_TEMPERATURA_MIN_ANUAL",
                "Informe de temperatura minima por ciudad, pais y en un rango de fechas, anualizada",
                "MINIMO", 100);

        crearProcesoSiNoExiste("INFORME_TEMPERATURA_PROMEDIO_ANUAL",
                "Informe de temperatura promedio por ciudad, pais y en un rango de fechas, anualizada",
                "PROMEDIO", 100);

        crearProcesoSiNoExiste("INFORME_TEMPERATURA_MAX_MENSUAL",
                "Informe de temperatura maxima por ciudad, pais y en un rango de fechas, mensualizada",
                "MAXIMO", 150);

        crearProcesoSiNoExiste("INFORME_TEMPERATURA_MIN_MENSUAL",
                "Informe de temperatura minima por ciudad, pais y en un rango de fechas, mensualizada",
                "MINIMO", 150);

        crearProcesoSiNoExiste("INFORME_TEMPERATURA_PROMEDIO_MENSUAL",
                "Informe de temperatura promedio por ciudad, pais y en un rango de fechas, mensualizada",
                "PROMEDIO", 150);

        System.out.println(">>> Procesos por defecto creados/actualizados");
    }

    private void crearProcesoSiNoExiste(String name, String description, String processType, double cost) {
        if (processRepository.existsByName(name)) {
            return;
        }
        Process p = new Process();
        p.setName(name);
        p.setDescription(description);
        p.setProcessType(processType);
        p.setCost(cost);
        processRepository.save(p);
    }
}
