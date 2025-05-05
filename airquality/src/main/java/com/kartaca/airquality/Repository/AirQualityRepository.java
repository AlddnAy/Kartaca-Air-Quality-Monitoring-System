package com.kartaca.airquality.Repository;

import com.kartaca.airquality.model.AirQualityMeasurement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirQualityRepository extends JpaRepository<AirQualityMeasurement, Long> {

    List<AirQualityMeasurement> findByLatitudeAndLongitude(double latitude, double longitude);
    List<AirQualityMeasurement> findByAnomalyIsTrueAndTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT AVG(a.pm25) FROM AirQualityMeasurement a WHERE a.latitude BETWEEN :minLat AND :maxLat AND a.longitude BETWEEN :minLon AND :maxLon")
    Double avgPm25InRegion(double minLat, double maxLat, double minLon, double maxLon);

    List<AirQualityMeasurement> findByLatitudeBetweenAndLongitudeBetweenOrderByTimestampAsc(
    double minLat, double maxLat, double minLon, double maxLon);

    @Query("SELECT AVG(a.pm25) FROM AirQualityMeasurement a WHERE a.latitude = :lat AND a.longitude = :lon AND a.timestamp > :since")
    Double avgPm25Last24h(double lat, double lon, LocalDateTime since);

    @Query("SELECT AVG(a.pm10) FROM AirQualityMeasurement a WHERE a.latitude = :lat AND a.longitude = :lon AND a.timestamp > :since")
    Double avgPm10Last24h(double lat, double lon, LocalDateTime since);

    @Query("SELECT AVG(a.no2) FROM AirQualityMeasurement a WHERE a.latitude = :lat AND a.longitude = :lon AND a.timestamp > :since")
    Double avgNo2Last24h(double lat, double lon, LocalDateTime since);

    @Query("SELECT AVG(a.so2) FROM AirQualityMeasurement a WHERE a.latitude = :lat AND a.longitude = :lon AND a.timestamp > :since")
    Double avgSo2Last24h(double lat, double lon, LocalDateTime since);

    @Query("SELECT AVG(a.o3) FROM AirQualityMeasurement a WHERE a.latitude = :lat AND a.longitude = :lon AND a.timestamp > :since")
    Double avgO3Last24h(double lat, double lon, LocalDateTime since);
    
}
