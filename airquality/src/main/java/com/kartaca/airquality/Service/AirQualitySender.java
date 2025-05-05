package com.kartaca.airquality.Service;

import com.kartaca.airquality.config.RabbitMQConfig;
import com.kartaca.airquality.model.AirQualityMeasurement;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AirQualitySender {

    private final RabbitTemplate rabbitTemplate;

    public AirQualitySender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMeasurement(AirQualityMeasurement measurement) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, measurement);
    }
}
