package com.kartaca.airquality.Service;

import com.kartaca.airquality.config.RabbitMQConfig;
import com.kartaca.airquality.model.AirQualityMeasurement;
import com.kartaca.airquality.Repository.AirQualityRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AirQualityConsumer {

    private final AirQualityRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final AnomalyDetectionService anomalyDetectionService;
    // EKLE:
    private final AnomalyNotificationService anomalyNotificationService;

    // GÜNCELLE:
    @Autowired
    public AirQualityConsumer(
        AirQualityRepository repository,
        RabbitTemplate rabbitTemplate,
        AnomalyDetectionService anomalyDetectionService,
        AnomalyNotificationService anomalyNotificationService // EKLE
    ) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.anomalyDetectionService = anomalyDetectionService;
        this.anomalyNotificationService = anomalyNotificationService; // EKLE
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(AirQualityMeasurement measurement) {
        System.out.println("Received message from queue: " + measurement);

        boolean isAnomaly = anomalyDetectionService.isAnomaly(measurement);
        measurement.setAnomaly(isAnomaly);

        repository.save(measurement);
        System.out.println("Measurement saved to DB: " + measurement);

        if (measurement.isAnomaly()) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.ANOMALY_QUEUE, measurement);
            anomalyNotificationService.sendAnomalyNotification(measurement); // EKLE
            System.out.println("Anomali bildirimi kuyruğa ve WebSocket'e gönderildi: " + measurement);
        }
    }
}