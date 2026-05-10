package com.raizesdonordeste.api.application.dto.response;

import java.util.List;

public record PagedResponse<T>(
    List<T> content,
    int page,
    int limit,
    long totalElements,
    int totalPages
) {
}

