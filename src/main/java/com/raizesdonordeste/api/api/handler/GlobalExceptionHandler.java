package com.raizesdonordeste.api.api.handler;

import com.raizesdonordeste.api.domain.exception.ConsentimentoLGPDException;
import com.raizesdonordeste.api.domain.exception.EstoqueInsuficienteException;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.exception.TransicaoStatusInvalidaException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
    List<ApiErrorDetail> details = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(this::toDetail)
        .toList();

    return buildErrorResponse(
        "VALIDATION_ERROR",
        "A requisição contém campos inválidos.",
        details,
        HttpStatus.UNPROCESSABLE_ENTITY,
        request
    );
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                                 HttpServletRequest request) {
    return buildErrorResponse(
        "VALIDATION_ERROR",
        "A requisição contém campos inválidos.",
        List.of(new ApiErrorDetail("body", "formato inválido ou enum não reconhecido")),
        HttpStatus.UNPROCESSABLE_ENTITY,
        request
    );
  }

  @ExceptionHandler(EstoqueInsuficienteException.class)
  public ResponseEntity<ApiErrorResponse> handleEstoqueInsuficienteException(EstoqueInsuficienteException ex,
                                                                              HttpServletRequest request) {
    return buildErrorResponse(
        "ESTOQUE_INSUFICIENTE",
        ex.getMessage(),
        List.of(),
        HttpStatus.CONFLICT,
        request
    );
  }

  @ExceptionHandler(RecursoNaoEncontradoException.class)
  public ResponseEntity<ApiErrorResponse> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex,
                                                                               HttpServletRequest request) {
    return buildErrorResponse(
        "RECURSO_NAO_ENCONTRADO",
        ex.getMessage(),
        List.of(),
        HttpStatus.NOT_FOUND,
        request
    );
  }

  @ExceptionHandler(TransicaoStatusInvalidaException.class)
  public ResponseEntity<ApiErrorResponse> handleTransicaoStatusInvalidaException(TransicaoStatusInvalidaException ex,
                                                                                  HttpServletRequest request) {
    return buildErrorResponse(
        "TRANSICAO_STATUS_INVALIDA",
        ex.getMessage(),
        List.of(),
        HttpStatus.CONFLICT,
        request
    );
  }

  @ExceptionHandler(ConsentimentoLGPDException.class)
  public ResponseEntity<ApiErrorResponse> handleConsentimentoLGPDException(ConsentimentoLGPDException ex,
                                                                            HttpServletRequest request) {
    return buildErrorResponse(
        "CONSENTIMENTO_LGPD",
        ex.getMessage(),
        List.of(),
        HttpStatus.FORBIDDEN,
        request
    );
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
                                                                         HttpServletRequest request) {
    return buildErrorResponse(
        "BAD_CREDENTIALS",
        "Credenciais inválidas.",
        List.of(),
        HttpStatus.UNAUTHORIZED,
        request
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
                                                                       HttpServletRequest request) {
    return buildErrorResponse(
        "ACCESS_DENIED",
        "Você não possui permissão para acessar este recurso.",
        List.of(),
        HttpStatus.FORBIDDEN,
        request
    );
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
                                                                          HttpServletRequest request) {
    return buildErrorResponse(
        "INVALID_REQUEST",
        ex.getMessage(),
        List.of(),
        HttpStatus.BAD_REQUEST,
        request
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
    return buildErrorResponse(
        "INTERNAL_SERVER_ERROR",
        "Ocorreu um erro interno inesperado.",
        List.of(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        request
    );
  }

  private ApiErrorDetail toDetail(FieldError fieldError) {
    String issue = fieldError.getDefaultMessage() == null ? "valor inválido" : fieldError.getDefaultMessage();
    return new ApiErrorDetail(fieldError.getField(), issue);
  }

  private ResponseEntity<ApiErrorResponse> buildErrorResponse(String error,
                                                              String message,
                                                              List<ApiErrorDetail> details,
                                                              HttpStatus httpStatus,
                                                              HttpServletRequest request) {
    ApiErrorResponse body = new ApiErrorResponse(
        error,
        message,
        details,
        Instant.now().toString(),
        request.getRequestURI(),
        UUID.randomUUID().toString()
    );

    return ResponseEntity.status(httpStatus).body(body);
  }
}
