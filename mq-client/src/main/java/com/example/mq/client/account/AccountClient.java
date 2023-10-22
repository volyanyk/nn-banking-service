package com.example.mq.client.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "account",
        url = "${clients.account.url}"
)
public interface AccountClient {

    @PostMapping("api/v1/account")
    void newAccount(AccountRequest accountRequest);
}
