package com.imss.sivimss.planpf.controller;

import com.imss.sivimss.planpf.service.ContratarPlanPFService;
import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.ProviderServiceRestTemplate;
import com.imss.sivimss.planpf.util.Response;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/convenioPf")
public class ContratarPlanPfController {

    @Autowired
    private ContratarPlanPFService servicio;
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;
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

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("validaCurpRfc")
    public CompletableFuture<?> validaCurpORfc(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = servicio.validaCurpRfc(request, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("consulta/promotores")
    public CompletableFuture<?> consultaPromotores(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        Response<?> response = servicio.consultaPromotores(request, authentication) ;
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    /**
     * fallbacks generico
     *
     * @return respuestas
     */
    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  CallNotPermittedException e) throws IOException {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        //logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", CONSULTA, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  RuntimeException e) throws IOException {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        // logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", CONSULTA, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  NumberFormatException e) throws IOException {
        Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
        // logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Resiliencia", CONSULTA, authentication);
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }
}
