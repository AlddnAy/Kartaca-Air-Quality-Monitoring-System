package com.kartaca.airquality.Service;

import com.kartaca.airquality.model.AirQualityMeasurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnomalyNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AnomalyNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAnomalyNotification(AirQualityMeasurement measurement) {
        messagingTemplate.convertAndSend("/topic/anomalies", measurement);
    }
}
