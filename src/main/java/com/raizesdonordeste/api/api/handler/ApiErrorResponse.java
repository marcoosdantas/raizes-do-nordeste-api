package com.raizesdonordeste.api.api.handler;

import java.util.List;

public record ApiErrorResponse(
    String error,
    String message,
    List<ApiErrorDetail> details,
    String timestamp,
    String path,
    String requestId
) {
}

