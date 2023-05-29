package com.imss.sivimss.planpf.model.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BeneficiarioResponse {
private String tipoPersona;
private String primerApellido;
private String numSexo;
private String nombreBeneficiario;
private String fechaNacimiento;
private String correo;
private String segundoApellido;
private String telefono;
private String idPersona;
private String rfc;
private String curp;
private String nss;
}