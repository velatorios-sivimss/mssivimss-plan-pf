package com.imss.sivimss.planpf.controller;

import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/")
public class ContratarPlanPfController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContratarPlanPfController.class);

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("agregarConvenioNuevoPF")
    public CompletableFuture<?> agregarConvenioNuevoPF(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = null;
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }
}
