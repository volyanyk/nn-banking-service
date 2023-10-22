package com.example.account;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.account}")
    private String accountQueue;

    @Value("${rabbitmq.routing-keys.internal-account}")
    private String internalAccountRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(this.accountQueue);
    }

    @Bean
    public Binding internalToNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(internalTopicExchange())
                .with(this.internalAccountRoutingKey);
    }

    public String getInternalExchange() {
        return internalExchange;
    }

    public String getAccountQueue() {
        return accountQueue;
    }

    public String getInternalAccountRoutingKey() {
        return internalAccountRoutingKey;
    }
}
