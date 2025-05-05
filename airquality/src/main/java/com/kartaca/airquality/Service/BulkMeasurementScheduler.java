/*package com.kartaca.airquality.Service;

import com.kartaca.airquality.model.AirQualityMeasurement;
import com.kartaca.airquality.Repository.AirQualityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.*;

@Service
public class BulkMeasurementScheduler {

    private final AirQualityRepository repository;
    private final Random random = new Random();

    public BulkMeasurementScheduler(AirQualityRepository repository) {
        this.repository = repository;
    }

    // Her 10 dakikada bir çalışır (fixedRate: 10 dakika)
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void addMeasurements() {
        List<AirQualityMeasurement> allLocations = repository.findAll();
        List<AirQualityMeasurement> newMeasurements = new ArrayList<>();

        for (AirQualityMeasurement loc : allLocations) {
            AirQualityMeasurement m = new AirQualityMeasurement();
            m.setLatitude(loc.getLatitude());
            m.setLongitude(loc.getLongitude());
            m.setTimestamp(LocalDateTime.now());
            m.setPm25(5 + random.nextDouble() * 50);
            m.setPm10(10 + random.nextDouble() * 80);
            m.setNo2(5 + random.nextDouble() * 40);
            m.setSo2(2 + random.nextDouble() * 20);
            m.setO3(10 + random.nextDouble() * 60);
            m.setAnomaly(false);
            newMeasurements.add(m);
        }
        repository.saveAll(newMeasurements);
        System.out.println("Her 10 dakikada bir " + newMeasurements.size() + " yeni ölçüm eklendi.");
    }
}*/
