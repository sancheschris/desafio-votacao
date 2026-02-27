package org.desafio.backend.integration.messaging;

import lombok.extern.slf4j.Slf4j;
import org.desafio.backend.config.RabbitMqConfig;
import org.desafio.backend.integration.dto.ResultadoVotacaoEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResultadoVotacaoPublisher {
    private final RabbitTemplate rabbitTemplate;

    public ResultadoVotacaoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(ResultadoVotacaoEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE,
                RabbitMqConfig.ROUTING_KEY,
                event
        );
        log.info("ResultadoVotacaoEvent publicado: {}", event);
    }
}
