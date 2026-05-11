package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PagedResponse<T>(
    List<T> content,
    @Schema(example = "0")
    int page,
    @Schema(example = "10")
    int limit,
    @Schema(example = "42")
    long totalElements,
    @Schema(example = "5")
    int totalPages
) {
}
