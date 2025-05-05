package com.kartaca.airquality.Service;

import com.kartaca.airquality.Repository.AirQualityRepository;
import com.kartaca.airquality.model.AirQualityMeasurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeasurementService {

    private final AirQualityRepository measurementRepository;

    @Autowired
    public MeasurementService(AirQualityRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Transactional
    public AirQualityMeasurement createMeasurement(AirQualityMeasurement measurement) {
        return measurementRepository.save(measurement);
    }
}