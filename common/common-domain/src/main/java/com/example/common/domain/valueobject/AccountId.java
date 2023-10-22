package com.example.common.domain.valueobject;

import org.bouncycastle.jcajce.provider.symmetric.ARC4;

public class AccountId extends BaseId<Integer> {
    public AccountId(Integer value) {
        super(value);
    }
}
