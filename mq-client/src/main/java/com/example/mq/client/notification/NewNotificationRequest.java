package com.example.mq.client.notification;

public record NewNotificationRequest(
        Integer toCustomerId,
        String toCustomerName,
        String message
) {
}
