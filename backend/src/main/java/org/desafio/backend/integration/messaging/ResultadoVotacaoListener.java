package org.desafio.backend.integration.messaging;

import org.desafio.backend.config.RabbitMqConfig;
import org.desafio.backend.integration.dto.ResultadoVotacaoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ResultadoVotacaoListener {
    @RabbitListener(queues = RabbitMqConfig.QUEUE_RESULTADO)
    public void onMessage(ResultadoVotacaoEvent event) {
        System.out.println("Resultado recebido: " + event);
    }
}
