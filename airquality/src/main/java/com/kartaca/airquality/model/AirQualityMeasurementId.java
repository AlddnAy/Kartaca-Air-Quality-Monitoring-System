package com.kartaca.airquality.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class AirQualityMeasurementId implements Serializable {
    private Long id;
    private LocalDateTime timestamp;

    public AirQualityMeasurementId() {}

    public AirQualityMeasurementId(Long id, LocalDateTime timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirQualityMeasurementId that = (AirQualityMeasurementId) o;
        return Objects.equals(id, that.id) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp);
    }
}