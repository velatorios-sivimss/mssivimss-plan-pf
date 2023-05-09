package com.imss.sivimss.planpf.service;

import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface ContratarPlanPFService {
    Response<?> agregarConvenioNuevoPF(DatosRequest request, Authentication authentication) throws IOException;
}
