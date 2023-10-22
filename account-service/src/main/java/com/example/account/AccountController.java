package com.example.account;

import com.example.account.model.BalanceResponse;
import com.example.account.model.OpenAccountRequest;
import com.example.account.model.TransferRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(path = "/{customerId}")
    public ResponseEntity<BalanceResponse> openAccount(@PathVariable("customerId") Integer customerId,
                                            @RequestBody OpenAccountRequest openAccountRequest) {
        log.info("new account opening for customer {}", customerId);
        return ResponseEntity.ok(accountService.createAccount(customerId, openAccountRequest));
    }
    @PutMapping(path = "/{customerId}")
    public ResponseEntity<BalanceResponse> transfer(@PathVariable("customerId") Integer customerId,
                                                       @RequestBody TransferRequest transferRequest){
        log.info("transfer to {} account opening for customer {}", transferRequest.toId(), customerId);
        return ResponseEntity.ok().body(accountService.transfer(customerId, transferRequest));
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<BalanceResponse> getBalanceWithCurrencyAndId(@PathVariable("customerId") Integer customerId,
                                                                @RequestParam(name = "currency", required = false ) String currency,
                                                                @RequestParam(name = "id", required = false) Integer id) {
        log.info("get balance by customer {}", customerId);
        if(StringUtils.isBlank(currency) && id != null){
            return ResponseEntity.ok(accountService.getBalanceById(customerId, id));
        }
        if(StringUtils.isNotBlank(currency) && id == null){
            return ResponseEntity.ok(accountService.getBalanceByCurrency(customerId, currency.toUpperCase()));
        }
        if(StringUtils.isNotBlank(currency) && id != null){
            return ResponseEntity.ok(accountService.getBalanceByCurrencyAndId(customerId, currency.toUpperCase(), id));
        }
        return ResponseEntity.ok(accountService.getBalance(customerId));
    }
}
