package com.kartaca.airquality.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kartaca.airquality.util.WHOThresholds;
import com.kartaca.airquality.Repository.AirQualityRepository;
import com.kartaca.airquality.model.AirQualityMeasurement;

@Service
public class AnomalyDetectionService {

    @Autowired
    private AirQualityRepository repository;

    public boolean isAnomaly(AirQualityMeasurement measurement) {
        if (measurement.getPm25() != null && measurement.getPm25() > WHOThresholds.PM25) return true;
        if (measurement.getPm10() != null && measurement.getPm10() > WHOThresholds.PM10) return true;
        if (measurement.getNo2()  != null && measurement.getNo2()  > WHOThresholds.NO2)  return true;
        if (measurement.getSo2()  != null && measurement.getSo2()  > WHOThresholds.SO2)  return true;
        if (measurement.getO3()   != null && measurement.getO3()   > WHOThresholds.O3)   return true;
        
        LocalDateTime since = measurement.getTimestamp().minusHours(24);
    double lat = measurement.getLatitude();
    double lon = measurement.getLongitude();

    if (measurement.getPm25() != null) {
        Double avg = repository.avgPm25Last24h(lat, lon, since);
        if (avg != null && avg > 0 && measurement.getPm25() > avg * 1.5) return true;
    }
    if (measurement.getPm10() != null) {
        Double avg = repository.avgPm10Last24h(lat, lon, since);
        if (avg != null && avg > 0 && measurement.getPm10() > avg * 1.5) return true;
    }
    if (measurement.getNo2() != null) {
        Double avg = repository.avgNo2Last24h(lat, lon, since);
        if (avg != null && avg > 0 && measurement.getNo2() > avg * 1.5) return true;
    }
    if (measurement.getSo2() != null) {
        Double avg = repository.avgSo2Last24h(lat, lon, since);
        if (avg != null && avg > 0 && measurement.getSo2() > avg * 1.5) return true;
    }
    if (measurement.getO3() != null) {
        Double avg = repository.avgO3Last24h(lat, lon, since);
        if (avg != null && avg > 0 && measurement.getO3() > avg * 1.5) return true;
    }

    return false;
    }
}
