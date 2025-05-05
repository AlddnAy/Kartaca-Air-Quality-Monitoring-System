package com.kartaca.airquality.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;
@Entity
//@IdClass(AirQualityMeasurementId.class)
@Table(name = "air_quality_data")
public class AirQualityMeasurement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "pm25", nullable = false)
    private Double pm25;

    @Column(name = "pm10", nullable = false)
    private Double pm10;

    @Column(name = "no2", nullable = false)
    private Double no2;

    @Column(name = "so2", nullable = false)
    private Double so2;

    @Column(name = "o3", nullable = false)
    private Double o3;

    @Column(name = "anomaly", nullable = false)
    private boolean anomaly;


    public AirQualityMeasurement() {
    }

    public AirQualityMeasurement( LocalDateTime timestamp, Double latitude, Double longitude, Double pm25, Double pm10, Double no2, Double so2, Double o3) {
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.no2 = no2;
        this.so2 = so2;
        this.o3 = o3;
        this.anomaly = false;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }       

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getPm25() {
        return pm25;
    }

    public Double getPm10() {
        return pm10;
    }   

    public Double getNo2() {
        return no2;
    }

    public Double getSo2() {
        return so2;
    }

    public Double getO3() {
        return o3;
    }


    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }   

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public void setNo2(Double no2) {
        this.no2 = no2;
    }

    public void setSo2(Double so2) {
        this.so2 = so2;
    }

    public void setO3(Double o3) {
        this.o3 = o3;
    }

    public boolean isAnomaly() {
        return anomaly;
    }
    
    public void setAnomaly(boolean anomaly) {
        this.anomaly = anomaly;
    }


}
