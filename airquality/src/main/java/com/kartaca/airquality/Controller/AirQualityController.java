package com.kartaca.airquality.Controller;

import com.kartaca.airquality.model.*;
import com.kartaca.airquality.Repository.*;
import com.kartaca.airquality.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AirQualityController {

    @Autowired
    private AirQualityRepository repository;
    
    @Autowired
    private AirQualitySender sender;

    MeasurementService measurementService;

    @Autowired
    public AirQualityController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/air_quality_data")
    public ResponseEntity<String> createMeasurement(@RequestBody AirQualityMeasurement air_quality_data) {
        sender.sendMeasurement(air_quality_data); // Sadece kuyruğa gönder
        return ResponseEntity.ok("Data queued successfully!");
    }

    @PostMapping("/save")
    public AirQualityMeasurement saveMeasurement(@RequestBody AirQualityMeasurement air_quality_data) {
        return repository.save(air_quality_data);
    }

    @GetMapping("/air_quality_data")
    public List<AirQualityMeasurement> getAllMeasurements() {
        return repository.findAll();
    }

    @PostMapping("/queue")
    public String sendToQueue(@RequestBody AirQualityMeasurement air_quality_data) {
        sender.sendMeasurement(air_quality_data);
        return "Data queued successfully!";
    }

    @GetMapping("/air_quality_data/by-location")
    public ResponseEntity<List<AirQualityMeasurement>> getByLocation(@RequestParam double latitude, @RequestParam double longitude) {
        List<AirQualityMeasurement> data = repository.findByLatitudeAndLongitude(latitude, longitude);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<AirQualityMeasurement>> getAnomalies(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);
        List<AirQualityMeasurement> anomalies = repository.findByAnomalyIsTrueAndTimestampBetween(startTime, endTime);
        return ResponseEntity.ok(anomalies);
    }

    @GetMapping("/pollution-density")
    public ResponseEntity<Double> getPollutionDensity(
        @RequestParam double minLat,
        @RequestParam double maxLat,
        @RequestParam double minLon,
        @RequestParam double maxLon) {
        Double avgPm25 = repository.avgPm25InRegion(minLat, maxLat, minLon, maxLon);
        return ResponseEntity.ok(avgPm25);
    }

    @GetMapping("/air_quality_data/timeseries")
    public ResponseEntity<List<AirQualityMeasurement>> getTimeSeries(
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam(defaultValue = "0.01") double tolerance) // opsiyonel
       {
        double minLat = latitude - tolerance;
        double maxLat = latitude + tolerance;
        double minLon = longitude - tolerance;
        double maxLon = longitude + tolerance;
        List<AirQualityMeasurement> data = repository.findByLatitudeBetweenAndLongitudeBetweenOrderByTimestampAsc(
        minLat, maxLat, minLon, maxLon
        );
        return ResponseEntity.ok(data);
    }
}
