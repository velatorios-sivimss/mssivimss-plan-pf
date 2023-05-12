package com.imss.sivimss.planpf.beans;

import com.imss.sivimss.planpf.model.request.ConvenioNuevoPFDto;
import com.imss.sivimss.planpf.model.request.UsuarioDto;
import com.imss.sivimss.planpf.util.AppConstantes;
import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class ConvenioNuevoPF {

    public DatosRequest insertarConvenioPfEmpresa(ConvenioNuevoPFDto convenioNuevo, UsuarioDto usuario) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper querySvtConvenio = new QueryHelper("INSERT INTO SVT_CONVENIO_PF");
        querySvtConvenio.agregarParametroValues("DES_FOLIO", convenioNuevo.getFolioConvenio());
        querySvtConvenio.agregarParametroValues("FEC_INICIO", convenioNuevo.getFechaInicio());
        querySvtConvenio.agregarParametroValues("FEC_VIGENCIA", convenioNuevo.getFechaVigencia());
        querySvtConvenio.agregarParametroValues("TIM_HORA", "DATE_FORMAT(NOW(), '%H:%i' )");
        querySvtConvenio.agregarParametroValues("ID_VELATORIO", convenioNuevo.getIdVelatorio());
        querySvtConvenio.agregarParametroValues("IND_SINIESTROS", "0");
        querySvtConvenio.agregarParametroValues("IND_TIPO_CONTRATACION", String.valueOf(convenioNuevo.getIndTipoContratacion()));
        querySvtConvenio.agregarParametroValues("ID_PROMOTOR", convenioNuevo.getIdPromotor());
        querySvtConvenio.agregarParametroValues("ID_ESTATUS_CONVENIO", "1");
        querySvtConvenio.agregarParametroValues("ID_USUARIO_ALTA", usuario.getCveUsuario());
        String queryFinal = querySvtConvenio.obtenerQueryInsertar();
        String encoded = DatatypeConverter.printBase64Binary(queryFinal.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("separador","$$");
        parametro.put("replace","idTabla");
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest insertDomicilioEmpresa(ConvenioNuevoPFDto convenioNuevo, UsuarioDto usuario){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper queryDomicilio = new QueryHelper("INSERT INTO SVT_DOMICILIO");
        queryDomicilio.agregarParametroValues("DES_CALLE", convenioNuevo.getIndTipoContratacion() == 0 ? convenioNuevo.getEmpresa().getCalle() : convenioNuevo.getPersona().getCalle());
        queryDomicilio.agregarParametroValues("NUM_EXTERIOR", convenioNuevo.getIndTipoContratacion() == 0 ? convenioNuevo.getEmpresa().getNumeroExterior() : convenioNuevo.getPersona().getNumeroExterior());
        queryDomicilio.agregarParametroValues("NUM_INTERIOR", convenioNuevo.getIndTipoContratacion() == 0 ? convenioNuevo.getEmpresa().getNumeroInterior() : convenioNuevo.getPersona().getNumeroInterior());
        queryDomicilio.agregarParametroValues("ID_CP", convenioNuevo.getIndTipoContratacion() == 0 ? convenioNuevo.getEmpresa().getCp() : convenioNuevo.getPersona().getCp());
        queryDomicilio.agregarParametroValues("DES_COLONIA", convenioNuevo.getIndTipoContratacion() == 0 ? convenioNuevo.getEmpresa().getColonia() : convenioNuevo.getPersona().getColonia());
        queryDomicilio.agregarParametroValues("ID_USUARIO_ALTA", usuario.getCveUsuario());
        String queryFinal = queryDomicilio.obtenerQueryInsertar();
        String encoded = DatatypeConverter.printBase64Binary(queryFinal.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public String generarQueryEmpresaConvenioPf(ConvenioNuevoPFDto convenioNuevo, UsuarioDto usuario, String idDomicilio){
        final QueryHelper queryEmpresaConvenio = new QueryHelper("INSERT INTO SVT_EMPRESA_CONVENIO_PF");
        queryEmpresaConvenio.agregarParametroValues("ID_CONVENIO_PF", "idTabla");
        queryEmpresaConvenio.agregarParametroValues("DES_NOMBRE", convenioNuevo.getEmpresa().getNombreEmpresa());
        queryEmpresaConvenio.agregarParametroValues("DES_RAZON_SOCIAL", convenioNuevo.getEmpresa().getRazonSocial());
        queryEmpresaConvenio.agregarParametroValues("DES_RFC", convenioNuevo.getEmpresa().getRfc());
        queryEmpresaConvenio.agregarParametroValues("ID_PAIS", convenioNuevo.getEmpresa().getPais());
        queryEmpresaConvenio.agregarParametroValues("ID_DOMICILIO", idDomicilio);
        queryEmpresaConvenio.agregarParametroValues("DES_TELEFONO", convenioNuevo.getEmpresa().getTelefono());
        queryEmpresaConvenio.agregarParametroValues("DES_CORREO", convenioNuevo.getEmpresa().getCorreoElectronico());
        queryEmpresaConvenio.agregarParametroValues("ID_USUARIO_ALTA", usuario.getCveUsuario());
        return queryEmpresaConvenio.obtenerQueryInsertar();
    }

    public DatosRequest consultarPromotores(){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String query = "SELECT " +
                "SP.ID_PROMOTOR AS idPromotor, " +
                "SP.NUM_EMPLEDO AS numEmpleado, " +
                "CONCAT (SP.NOM_PROMOTOR , " +
                "' ' , " +
                "SP.NOM_PAPELLIDO , " +
                "' ' , " +
                "SP.NOM_SAPELLIDO) AS nombrePromotor " +
                "FROM " +
                "SVT_PROMOTOR SP";
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest consultarCurpRfc(String curp, String rfc){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String query = "SELECT " +
                "SP.CVE_RFC AS rfc, " +
                "SP.CVE_CURP AS curp, " +
                "SP.CVE_NSS AS nss, " +
                "SP.NOM_PERSONA AS nomPersona, " +
                "SP.NOM_PRIMER_APELLIDO AS primerApellido, " +
                "SP.NOM_SEGUNDO_APELLIDO AS segundoApellido, " +
                "SP.NUM_SEXO AS sexo, " +
                "SP.FEC_NAC AS fechaNacimiento, " +
                "SP.ID_PAIS AS idPais, " +
                "SP.ID_ESTADO AS idEstado, " +
                "SP.DES_TELEFONO AS telefono, " +
                "SP.DES_CORREO AS correo, " +
                "SP.TIPO_PERSONA AS tipoPersona " +
                "FROM " +
                "SVC_CONTRATANTE SC " +
                "LEFT JOIN SVC_PERSONA SP ON " +
                "SC.ID_PERSONA = SP.ID_PERSONA " +
                "WHERE " +
                "SP.CVE_RFC = " + rfc + " " +
                "OR SP.CVE_CURP = " + curp + " ";
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
}
