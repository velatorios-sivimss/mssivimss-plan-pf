package com.imss.sivimss.planpf.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.imss.sivimss.planpf.beans.ConvenioNuevoPF;
import com.imss.sivimss.planpf.controller.ContratarPlanPfController;
import com.imss.sivimss.planpf.model.request.PdfDto;
import com.imss.sivimss.planpf.model.response.BeneficiarioResponse;
import com.imss.sivimss.planpf.model.response.BusquedaPersonaFolioResponse;
import com.imss.sivimss.planpf.model.response.ContratanteResponse;
import com.imss.sivimss.planpf.service.ContratarPlanPFService;
import com.imss.sivimss.planpf.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ContratarPlanPFServiceImpl implements ContratarPlanPFService {
    @Value("${endpoints.dominio-consulta}")
    private String urlDominio;
    @Value("${endpoints.ms-reportes}")
    private String urlReportes;
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContratarPlanPFServiceImpl.class);
    JsonParser jsonParser = new JsonParser();
    ConvenioNuevoPF convenioBean = new ConvenioNuevoPF();
    Gson json = new Gson();
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Response<?> agregarConvenioNuevoPF(DatosRequest request, Authentication authentication) throws IOException {
        return null;
    }

    @Override
    public Response<?> consultaPromotores(DatosRequest request, Authentication authentication) throws IOException {
        return providerRestTemplate.consumirServicio(convenioBean.consultarPromotores().getDatos(), urlDominio + "/generico/consulta", authentication);
    }

    @Override
    public Response<?> validaCurpRfc(DatosRequest request, Authentication authentication) throws IOException {
        JsonObject objeto = (JsonObject) jsonParser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String curp = String.valueOf(objeto.get("curp"));
        String rfc = String.valueOf(objeto.get("rfc"));
        return providerRestTemplate.consumirServicio(convenioBean.consultarCurpRfc(curp, rfc).getDatos(), urlDominio + "/generico/consulta", authentication);
    }

    @Override
    public Response<?> consultaCP(DatosRequest request, Authentication authentication) throws IOException {
        JsonObject objeto = (JsonObject) jsonParser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String cp = String.valueOf(objeto.get("cp"));
        return providerRestTemplate.consumirServicio(convenioBean.consultarCP(cp).getDatos(), urlDominio + "/generico/consulta", authentication);
    }

    @Override
    public Response<?> generarPDF(DatosRequest request, Authentication authentication) throws IOException {
        String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        PdfDto pdfDto = json.fromJson(datosJson, PdfDto.class);
        Map<String, Object> envioDatos = new ConvenioNuevoPF().generarReporte(pdfDto);
        return providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,
                authentication);
    }

    @Override
    public Response<?> busquedaFolioPersona(DatosRequest request, Authentication authentication) throws IOException {
            Response<?> response = new Response<>();
            List<BeneficiarioResponse> beneficiariosResponse;
            List<ContratanteResponse> contratanteResponse;
            BusquedaPersonaFolioResponse busquedaFolio = new BusquedaPersonaFolioResponse();
            JsonObject objeto = (JsonObject) jsonParser.parse((String) request.getDatos().get(AppConstantes.DATOS));
            String folioConvenio = String.valueOf(objeto.get("folioConvenio"));
            Response<?> responseContratante = providerRestTemplate.consumirServicio(convenioBean.busquedaFolioPersona(folioConvenio).getDatos(), urlDominio + "/generico/consulta", authentication);
            if(!responseContratante.getDatos().toString().equals("[]")){
                contratanteResponse =  Arrays.asList(modelMapper.map(responseContratante.getDatos(),ContratanteResponse[].class));
                beneficiariosResponse = Arrays.asList(modelMapper.map(providerRestTemplate.consumirServicio(convenioBean.busquedaBeneficiarios(folioConvenio).getDatos(), urlDominio + "/generico/consulta", authentication).getDatos(), BeneficiarioResponse[].class));
                busquedaFolio.setDatosContratante(contratanteResponse.get(0));
                busquedaFolio.setBeneficiarios(beneficiariosResponse);
                busquedaFolio.setFolioConvenio(folioConvenio);
                response.setCodigo(200);
                response.setError(false);
                response.setMensaje("");
                response.setDatos(ConvertirGenerico.convertInstanceOfObject(busquedaFolio));
                return response;
            }
            response.setCodigo(200);
            response.setError(true);
            response.setMensaje("52");
            return response;
    }

    @Override
    public Response<?> busquedaRfcEmpresa(DatosRequest request, Authentication authentication) throws IOException {
        log.info("- Se entra a consulta rfc empresa -");
        JsonObject objeto = (JsonObject) jsonParser.parse((String) request.getDatos().get(AppConstantes.DATOS));
        String rfc = String.valueOf(objeto.get("rfc"));
        return providerRestTemplate.consumirServicio(convenioBean.busquedaRfcEmpresa(rfc).getDatos(), urlDominio + "/generico/consulta", authentication);
    }
}


