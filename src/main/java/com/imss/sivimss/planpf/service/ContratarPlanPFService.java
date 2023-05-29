package com.imss.sivimss.planpf.service;

import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface ContratarPlanPFService {
    Response<?> agregarConvenioNuevoPF(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> consultaPromotores(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> validaCurpRfc(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> consultaCP(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> generarPDF(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> busquedaFolioPersona(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> busquedaRfcEmpresa(DatosRequest request, Authentication authentication) throws IOException;
}
