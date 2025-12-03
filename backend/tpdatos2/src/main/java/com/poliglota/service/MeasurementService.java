package com.poliglota.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.poliglota.DTO.AggregatedResult;
import com.poliglota.DTO.MeasurementDTO;
import com.poliglota.model.cassandra.Measurement;
import com.poliglota.model.cassandra.MeasurementKey;
import com.poliglota.model.mongo.Sensor;
import com.poliglota.repository.cassandra.MeasurementRepository;
import com.poliglota.repository.mongo.SensorRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    public MeasurementDTO save(MeasurementDTO measurementDTO) {
        return this.toDto(measurementRepository.save(this.toEntity(measurementDTO)));
    }

    public List<MeasurementDTO> getBySensor(String sensorId) {
        return measurementRepository.findByKeySensorId(sensorId)
                .stream()
                .map(this::toDto)
                .toList();
    }

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

       
        List<Sensor> sensores = sensorRepository.findByCityAndCountry(city, country);

        if (sensores.isEmpty()) {
            return Collections.emptyList();
        }

        List<Measurement> todasLasMediciones = new ArrayList<>();
        for (Sensor s : sensores) {
            String sensorId = s.getId();
            List<Measurement> medicionesSensor = measurementRepository
                    .findByKeySensorIdAndKeyTimestampBetween(sensorId, startDate, endDate);
            todasLasMediciones.addAll(medicionesSensor);
        }

        if (todasLasMediciones.isEmpty()) {
            return Collections.emptyList();
        }

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

            List<Double> valores = medicionesPeriodo.stream()
                    .map(m -> {
                        if ("HUMEDAD".equals(metricUp)) {
                            return m.getHumidity();
                        } else {
                            return m.getTemperature();
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            if (valores.isEmpty()) {
                continue;
            }

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

        resultados.sort(Comparator.comparing(AggregatedResult::getPeriod));

        return resultados;
    }

    private Measurement toEntity(MeasurementDTO dto) {
        Measurement measurement = new Measurement();
        MeasurementKey key = new MeasurementKey();
        key.setSensorId(dto.sensorId);
        key.setTimestamp(dto.timestamp);
        measurement.setKey(key);
        measurement.setTemperature(dto.temperature);
        measurement.setHumidity(dto.humidity);
        return measurement;
    }

    private MeasurementDTO toDto(Measurement measurement) {
        MeasurementDTO dto = new MeasurementDTO();
        dto.sensorId = measurement.getKey().getSensorId();
        dto.timestamp = measurement.getKey().getTimestamp();
        dto.temperature = measurement.getTemperature();
        dto.humidity = measurement.getHumidity();
        return dto;
    }
}
