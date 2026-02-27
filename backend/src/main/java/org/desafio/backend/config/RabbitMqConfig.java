package org.desafio.backend.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE = "votacao.exchange";
    public static final String QUEUE_RESULTADO = "votacao.resultado.queue";
    public static final String ROUTING_KEY = "votacao.resultado";

    @Bean
    public TopicExchange votacaoExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue resultadoQueue() {
        return new Queue(QUEUE_RESULTADO, true); // durable
    }

    @Bean
    public Binding resultadoBinding(Queue resultadoQueue, TopicExchange votacaoExchange) {
        return BindingBuilder.bind(resultadoQueue).to(votacaoExchange).with(ROUTING_KEY);
    }
}
