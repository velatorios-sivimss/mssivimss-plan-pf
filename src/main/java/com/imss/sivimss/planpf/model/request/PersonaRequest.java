package com.imss.sivimss.planpf.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonaRequest {
    @JsonProperty
    private String matricula;
    @JsonProperty
    private String rfc;
    @JsonProperty
    private String curp;
    @JsonProperty
    private String nombre;
    @JsonProperty
    private String primerApellido;
    @JsonProperty
    private String segundoApellido;
    @JsonProperty
    private String calle;
    @JsonProperty
    private String numeroExterior;
    @JsonProperty
    private String numeroInterior;
    @JsonProperty
    private String cp;
    @JsonProperty
    private String colonia;
    @JsonProperty
    private String municipio;
    @JsonProperty
    private String estado;
    @JsonProperty
    private String pais;
    @JsonProperty
    private String correoElectronico;
    @JsonProperty
    private String telefono;
    @JsonProperty
    private String enfermedadPreexistente;
    @JsonProperty
    private String otraEnfermedad;
    @JsonProperty
    private String paquete;
    @JsonProperty
    private PersonaRequest[] beneficiarios;
}
