package com.imss.sivimss.planpf.service.impl;

import com.imss.sivimss.planpf.service.ContratarPlanPFService;
import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContratarPlanPFServiceImpl implements ContratarPlanPFService {
    @Value("${endpoints.dominio-crear-multiple}")
    private String urlDominioMultiple;
    @Override
    public Response<?> agregarConvenioNuevoPF(DatosRequest request, Authentication authentication) throws IOException {
        return null;
    }
}
