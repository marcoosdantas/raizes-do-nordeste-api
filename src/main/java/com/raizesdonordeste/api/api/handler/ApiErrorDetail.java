package com.raizesdonordeste.api.api.handler;

public record ApiErrorDetail(
    String field,
    String issue
) {
}

