package org.example.corebanking.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ.
 * It uses the Spring's @Configuration annotation to indicate that it's a configuration class.
 * It also uses the @Value annotation to inject values from the application.properties file.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Bean for the DirectExchange.
     *
     * @return a new instance of DirectExchange named "corebanking-exchange"
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("corebanking-exchange");
    }

    /**
     * Bean for the accountQueue.
     *
     * @return a new instance of Queue named "accountQueue", which is durable
     */
    @Bean
    public Queue accountQueue() {
        return new Queue("accountQueue", true);
    }

    /**
     * Bean for the Binding.
     * It binds the accountQueue to the exchange with the routing key "account.key".
     *
     * @param exchange     the DirectExchange bean
     * @param accountQueue the Queue bean
     * @return a new instance of Binding
     */
    @Bean
    public Binding binding(DirectExchange exchange, Queue accountQueue) {
        return BindingBuilder.bind(accountQueue).to(exchange).with("account.key");
    }
}
