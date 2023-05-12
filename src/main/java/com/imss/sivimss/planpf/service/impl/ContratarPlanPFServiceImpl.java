package com.imss.sivimss.planpf.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imss.sivimss.planpf.beans.ConvenioNuevoPF;
import com.imss.sivimss.planpf.service.ContratarPlanPFService;
import com.imss.sivimss.planpf.util.AppConstantes;
import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.ProviderServiceRestTemplate;
import com.imss.sivimss.planpf.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ContratarPlanPFServiceImpl implements ContratarPlanPFService {
    @Value("${endpoints.dominio-consulta}")
    private String urlDominio;
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;
    JsonParser jsonParser = new JsonParser();
    ConvenioNuevoPF convenioBean = new ConvenioNuevoPF();
    @Override
    public Response<?> agregarConvenioNuevoPF(DatosRequest request, Authentication authentication) throws IOException {
        return null;
    }

    @Override
    public Response<?> consultaPromotores(DatosRequest request, Authentication authentication) throws IOException {
            return providerRestTemplate.consumirServicio(convenioBean.consultarPromotores().getDatos(),urlDominio + "/generico/consulta",authentication);
    }

    @Override
    public Response<?> validaCurpRfc(DatosRequest request, Authentication authentication) throws IOException {
        JsonObject objeto = (JsonObject) jsonParser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String curp = String.valueOf(objeto.get("curp"));
        String rfc = String.valueOf(objeto.get("rfc"));
        return providerRestTemplate.consumirServicio(convenioBean.consultarCurpRfc(curp,rfc).getDatos(),urlDominio + "/generico/consulta",authentication);
    }
}
