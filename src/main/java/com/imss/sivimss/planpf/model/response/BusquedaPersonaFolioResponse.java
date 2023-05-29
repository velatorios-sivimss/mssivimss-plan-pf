package com.imss.sivimss.planpf.model.response;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BusquedaPersonaFolioResponse {
    private String folioConvenio;
    private ContratanteResponse datosContratante;
    private List<BeneficiarioResponse> beneficiarios;
}