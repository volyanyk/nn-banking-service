package com.example.account;

import com.example.mq.client.account.AccountRequest;
import com.example.mq.client.notification.NewNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class AccountConsumer {

    private final AccountService accountService;

    @RabbitListener(queues = "${rabbitmq.queues.account}")
    public void consumer(AccountRequest accountRequest) {
        log.info("Consumed {} from queue", accountRequest);
        accountService.createInitialAccount(accountRequest);
    }
}
