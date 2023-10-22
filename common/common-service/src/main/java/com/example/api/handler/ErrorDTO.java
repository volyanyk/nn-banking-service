package com.example.api.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


public record ErrorDTO(String code, String message) {
}
