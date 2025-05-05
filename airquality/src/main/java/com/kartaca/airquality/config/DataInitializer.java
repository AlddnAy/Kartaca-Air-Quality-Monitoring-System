/*package com.kartaca.airquality.config;

import com.kartaca.airquality.model.AirQualityMeasurement;
import com.kartaca.airquality.Repository.AirQualityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

import java.util.*;

@Configuration
public class DataInitializer {

    // Dünya kara parçalarının yaklaşık koordinat aralıkları (örnek)
    private static final double[][] LAND_BOUNDS = {
        // {minLat, maxLat, minLon, maxLon}
        {-55, 60, -170, -30},   // Amerika
        {35, 70, -10, 40},      // Avrupa
        {-35, 37, 10, 150},     // Afrika + Asya + Avustralya
        {-55, -10, 110, 180}    // Avustralya + Okyanusya
    };

    private static Random random = new Random();

    private static double[] randomLandLocation() {
        double[] bounds = LAND_BOUNDS[random.nextInt(LAND_BOUNDS.length)];
        double lat = bounds[0] + random.nextDouble() * (bounds[1] - bounds[0]);
        double lon = bounds[2] + random.nextDouble() * (bounds[3] - bounds[2]);
        return new double[]{lat, lon};
    }

    @Bean
    CommandLineRunner initData(AirQualityRepository repository) {
        return args -> {
            if (repository.count() > 0) return; // Zaten veri varsa ekleme

            List<AirQualityMeasurement> measurements = new ArrayList<>();
            for (int i = 0; i < 1000; i++) { // 1000 lokasyon
                double[] loc = randomLandLocation();
                AirQualityMeasurement m = new AirQualityMeasurement();
                m.setLatitude(loc[0]);
                m.setLongitude(loc[1]);
                m.setTimestamp(LocalDateTime.now());
                m.setPm25(5 + random.nextDouble() * 50);
                m.setPm10(10 + random.nextDouble() * 80);
                m.setNo2(5 + random.nextDouble() * 40);
                m.setSo2(2 + random.nextDouble() * 20);
                m.setO3(10 + random.nextDouble() * 60);
                m.setAnomaly(false); // ilk başta anomali yok
                measurements.add(m);
            }
            repository.saveAll(measurements);
            System.out.println("1000 lokasyon ve ölçüm eklendi.");
        };
    }
} */
