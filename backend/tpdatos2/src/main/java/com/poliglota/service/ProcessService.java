package com.poliglota.service;

import com.poliglota.model.mysql.ExecutionHistory;
import com.poliglota.model.mysql.Process;
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.repository.jpa.ExecutionHistoryRepository;
import com.poliglota.repository.jpa.ProcessRepository;
import com.poliglota.repository.jpa.ProcessRequestRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import com.poliglota.DTO.ProcessDTO;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

// tus repos
import com.poliglota.model.mysql.ProcessRequest;
import com.poliglota.model.mysql.ExecutionHistory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final ProcessRepository processRepository;
	private final ProcessRequestRepository processRequestRepository;
	private final MongoTemplate mongoTemplate;
	private final ExecutionHistoryRepository executionHistoryRepository;

    public List<ProcessDTO> getAllProcesses() {
        return processRepository.findAll()
			.stream()
			.map(process -> this.toDto(process))
			.toList();
    }

    public ProcessDTO getProcessById(Long id) {
        Optional<Process> processOpt = processRepository.findById(id);
        return processOpt.map(this::toDto).orElse(null);
    }

    public Optional<ProcessDTO> getProcessByName(String name) {
        return Optional.ofNullable(toDto(processRepository.findByName(name)));
    }

    public List<ProcessDTO> getProcessesByType(String type) {
        return processRepository.findByProcessType(type)
			.stream()
			.map(process -> this.toDto(process))
			.toList();
    }

    public List<ProcessDTO> getProcessesByMaxCost(double maxCost) {
        return processRepository.findByCostLessThan(maxCost)
			.stream()
			.map(process -> this.toDto(process))
			.toList();		
    }

    public ProcessDTO saveProcess(ProcessDTO processDTO) {
        Process proceso = processRepository.save(toEntity(processDTO));
		return toDto(proceso);
    }

    public boolean deleteProcess(Long id) {
        if (processRepository.existsById(id)) {
            processRepository.deleteById(id);
            return true;
        }
        return false;
    }


	 public ExecutionHistory runTemperatureReport(String city, String from, String to, Long requestId) {

        try {
            // 1) Parsear fechas
            Instant f = Instant.parse(from);
            Instant t = Instant.parse(to);

            System.out.println("🔍 DEBUG - Ejecutando agregación MongoDB:");
            System.out.println("   • Ciudad: " + city);
            System.out.println("   • Desde: " + f);
            System.out.println("   • Hasta: " + t);

            // 2) Armar agregación en MongoDB
            Aggregation agg = Aggregation.newAggregation(
                    Aggregation.match(
                            Criteria.where("locationSnapshot.city").is(city)
                                    .and("timestamp").gte(f).lte(t)
                    ),
                    Aggregation.group("locationSnapshot.city")
                            .avg("temperature").as("avgTemp")
                            .min("temperature").as("minTemp")
                            .max("temperature").as("maxTemp")
                            .count().as("count")
            );

            // 3) Ejecutar la agregación
            AggregationResults<org.bson.Document> res =
                    mongoTemplate.aggregate(agg, "mediciones", org.bson.Document.class);

            List<org.bson.Document> results = res.getMappedResults();
            System.out.println("🔍 DEBUG - Resultados de agregación: " + results.size() + " documentos");

            // 4) Armar resultado legible + JSON
            String jsonResult;
            String status;

            if (results.isEmpty()) {
                status = "NO_DATA";
                jsonResult = String.format(
                        "{\"error\": \"No data found\", \"city\": \"%s\", \"from\": \"%s\", \"to\": \"%s\"}",
                        city, from, to
                );

                System.out.println("❌ No se encontraron mediciones de temperatura para " + city +
                        " en el rango de fechas " + from + " a " + to);

            } else {
                org.bson.Document result = results.get(0);
                Double avgTemp = result.getDouble("avgTemp");
                Double minTemp = result.getDouble("minTemp");
                Double maxTemp = result.getDouble("maxTemp");
                Integer count = result.getInteger("count");

                String displayResult = String.format(
                        "📊 REPORTE DE TEMPERATURA - %s\n" +
                                "   📅 Período: %s a %s\n" +
                                "   📏 Mediciones analizadas: %d\n" +
                                "   🌡️  Temperatura mínima: %.1f°C\n" +
                                "   🌡️  Temperatura máxima: %.1f°C\n" +
                                "   🌡️  Temperatura promedio: %.1f°C",
                        city, from.substring(0, 10), to.substring(0, 10), count, minTemp, maxTemp, avgTemp
                );

                System.out.println("\n" + displayResult);

                // Para que el JSON use punto decimal y no coma
                jsonResult = String.format(
                        java.util.Locale.US,
                        "{\"city\": \"%s\", \"avgTemp\": %.2f, \"minTemp\": %.2f, \"maxTemp\": %.2f, \"count\": %d}",
                        city, avgTemp, minTemp, maxTemp, count
                );

                status = "COMPLETED";
            }

            // 5) Buscar la solicitud de proceso (ProcessRequest) en MySQL
            ProcessRequest request = processRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("ProcessRequest no encontrada: " + requestId));

            // 6) Actualizar estado de la solicitud
            request.setStatus(status);
            processRequestRepository.save(request);

            // 7) Crear registro de historial de ejecución
            ExecutionHistory history = new ExecutionHistory(
                    null,
                    request,
                    LocalDateTime.now(),
                    jsonResult,
                    status
            );

            // 8) Guardar historial y devolver
            return executionHistoryRepository.save(history);

        } catch (Exception e) {
            System.out.println("❌ Error ejecutando reporte de temperatura: " + e.getMessage());

            // En caso de error, marcar la solicitud como FAILED y guardar historial
            ProcessRequest request = processRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("ProcessRequest no encontrada: " + requestId));

            request.setStatus("FAILED");
            processRequestRepository.save(request);

            String jsonResult = "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";

            ExecutionHistory history = new ExecutionHistory(
                    null,
                    request,
                    LocalDateTime.now(),
                    jsonResult,
                    "FAILED"
            );

            return executionHistoryRepository.save(history);
        }
    }

	public ExecutionHistory runHumidityReport(String city, String from, String to, Long requestId) {

    try {
        // 1) Parsear fechas
        Instant f = Instant.parse(from);
        Instant t = Instant.parse(to);

        System.out.println("🔍 DEBUG - Ejecutando agregación MongoDB para HUMEDAD:");
        System.out.println("   • Ciudad: " + city);
        System.out.println("   • Desde: " + f);
        System.out.println("   • Hasta: " + t);

        // 2) Agregación en MongoDB para humedad
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("locationSnapshot.city").is(city)
                                .and("timestamp").gte(f).lte(t)
                ),
                Aggregation.group("locationSnapshot.city")
                        .avg("humidity").as("avgHumidity")
                        .min("humidity").as("minHumidity")
                        .max("humidity").as("maxHumidity")
                        .count().as("count")
        );

        // 3) Ejecutar la agregación
        AggregationResults<org.bson.Document> res =
                mongoTemplate.aggregate(agg, "mediciones", org.bson.Document.class);

        List<org.bson.Document> results = res.getMappedResults();
        System.out.println("🔍 DEBUG - Resultados de agregación: " + results.size() + " documentos");

        // 4) Construir JSON + estado
        String jsonResult;
        String status;

        if (results.isEmpty()) {
            status = "NO_DATA";
            System.out.println("❌ No se encontraron mediciones de humedad para " + city +
                    " en el rango de fechas " + from + " a " + to);

            jsonResult = String.format(
                    "{\"error\": \"No data found\", \"city\": \"%s\", \"from\": \"%s\", \"to\": \"%s\"}",
                    city, from, to
            );
        } else {
            org.bson.Document result = results.get(0);
            Double avgHumidity = result.getDouble("avgHumidity");
            Double minHumidity = result.getDouble("minHumidity");
            Double maxHumidity = result.getDouble("maxHumidity");
            Integer count = result.getInteger("count");

            String displayResult = String.format(
                    "💧 REPORTE DE HUMEDAD - %s\n" +
                            "   📅 Período: %s a %s\n" +
                            "   📏 Mediciones analizadas: %d\n" +
                            "   💧 Humedad mínima: %.1f%%\n" +
                            "   💧 Humedad máxima: %.1f%%\n" +
                            "   💧 Humedad promedio: %.1f%%",
                    city, from.substring(0, 10), to.substring(0, 10), count,
                    minHumidity, maxHumidity, avgHumidity
            );

            System.out.println("\n" + displayResult);

            jsonResult = String.format(
                    java.util.Locale.US,
                    "{\"city\": \"%s\", \"avgHumidity\": %.2f, \"minHumidity\": %.2f, \"maxHumidity\": %.2f, \"count\": %d}",
                    city, avgHumidity, minHumidity, maxHumidity, count
            );

            status = "COMPLETED";
        }

        // 5) Buscar la solicitud asociada (ProcessRequest) en MySQL
        ProcessRequest request = processRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("ProcessRequest no encontrada: " + requestId));

        // 6) Actualizar estado de la solicitud
        request.setStatus(status);
        processRequestRepository.save(request);

        // 7) Crear ExecutionHistory
        ExecutionHistory history = new ExecutionHistory(
                null,
                request,
                LocalDateTime.now(),
                jsonResult,
                status
        );

        // 8) Guardar historial y devolver
        return executionHistoryRepository.save(history);

    } catch (Exception e) {
        System.out.println("❌ Error ejecutando reporte de humedad: " + e.getMessage());

        ProcessRequest request = processRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("ProcessRequest no encontrada: " + requestId));

        request.setStatus("FAILED");
        processRequestRepository.save(request);

        String jsonResult = "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";

        ExecutionHistory history = new ExecutionHistory(
                null,
                request,
                LocalDateTime.now(),
                jsonResult,
                "FAILED"
        );

        return executionHistoryRepository.save(history);
    }
}	

	private Process toEntity(ProcessDTO dto) {
		Process process = new Process();
		process.setName(dto.getName());
		process.setDescription(dto.getDescription());
		process.setProcessType(dto.getProcessType());
		process.setCost(dto.getCost());                   
		return process;
	}
    
	private ProcessDTO toDto(Process process) {
		ProcessDTO dto = new ProcessDTO();
		dto.setName(process.getName());
		dto.setDescription(process.getDescription());
		dto.setProcessType(process.getProcessType());
		dto.setCost(process.getCost());
		dto.setId(process.getProcessId());
		return dto;
	}
}
