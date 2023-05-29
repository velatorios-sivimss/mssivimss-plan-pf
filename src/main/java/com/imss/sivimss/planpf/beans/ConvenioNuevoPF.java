package com.imss.sivimss.planpf.beans;

import com.imss.sivimss.planpf.model.request.ConvenioNuevoPFDto;
import com.imss.sivimss.planpf.model.request.PdfDto;
import com.imss.sivimss.planpf.model.request.UsuarioDto;
import com.imss.sivimss.planpf.util.AppConstantes;
import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.QueryHelper;
import com.imss.sivimss.planpf.util.SelectQueryUtil;
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

    public DatosRequest insertarConvenioPfPersona(ConvenioNuevoPFDto convenioNuevo, UsuarioDto usuario) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper querySvtConvenio = new QueryHelper("INSERT INTO SVT_CONVENIO_PF");
        querySvtConvenio.agregarParametroValues("DES_FOLIO", convenioNuevo.getFolioConvenio());
        querySvtConvenio.agregarParametroValues("FEC_INICIO", convenioNuevo.getFechaInicio());
        querySvtConvenio.agregarParametroValues("FEC_VIGENCIA", convenioNuevo.getFechaVigencia());
        querySvtConvenio.agregarParametroValues("TIM_HORA", "DATE_FORMAT(NOW(), '%H:%i' )");
        querySvtConvenio.agregarParametroValues("ID_VELATORIO", convenioNuevo.getIdVelatorio());
        querySvtConvenio.agregarParametroValues("IND_SINIESTROS", "0");
        querySvtConvenio.agregarParametroValues("IND_TIPO_CONTRATACION","1"); // 1.- personas - 0.- empresa
        querySvtConvenio.agregarParametroValues("ID_PROMOTOR", convenioNuevo.getIdPromotor());
        querySvtConvenio.agregarParametroValues("ID_ESTATUS_CONVENIO", "1");
        querySvtConvenio.agregarParametroValues("ID_USUARIO_ALTA", usuario.getCveUsuario());
//        String queryFinal = querySvtConvenio.obtenerQueryInsertar() + "$$" + contratanteInsert + generarQueryContratantePaqueteConvenioPf(
//                convenioNuevo.getPersona().getEnfermedadPreexistente(),convenioNuevo.getPersona().getOtraEnfermedad(),usuario.getCveUsuario(),
//                convenioNuevo.getPersona().getPaquete());
       // String encoded = DatatypeConverter.printBase64Binary(queryFinal.getBytes());
       // parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("separador", "$$");
        parametro.put("replace", "idConvenioPf");
        dr.setDatos(parametro);
        return dr;

    }

    public String generarQueryContratante(ConvenioNuevoPFDto convenioNuevo, String usuario){
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper queryContratante = new QueryHelper("INSERT INTO SVT_CONVENIO_PF");
        queryContratante.agregarParametroValues("ID_PERSONA", "idPersona"); // agregar despues de insertar en persona
        queryContratante.agregarParametroValues("CVE_MATRICULA",convenioNuevo.getPersona().getMatricula());
        queryContratante.agregarParametroValues("ID_DOMICILIO","idDomicilio"); // agregar despues de insertar a domicilio
        queryContratante.agregarParametroValues("ID_USUARIO_ALTA",usuario);
        String query = queryContratante.obtenerQueryInsertar();
        return query;
    }

    public String generarQueryContratantePaqueteConvenioPf(String enfermedadPreexistente,String otraEnfermedad,String usuarioAlta, String idPaquete){
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper queryContratantePaquete = new QueryHelper("INSERT INTO SVT_CONVENIO_PF");
        queryContratantePaquete.agregarParametroValues("ID_CONTRATANTE", ""); // agregar despues de insertar en contratante
        queryContratantePaquete.agregarParametroValues("ID_CONVENIO_PF","idConvenioPf");
        queryContratantePaquete.agregarParametroValues("ID_ENFERMEDAD_PREXISTENTE",enfermedadPreexistente);
        queryContratantePaquete.agregarParametroValues("DES_OTRA_ENFERMEDAD",otraEnfermedad);
        queryContratantePaquete.agregarParametroValues("ID_PAQUETE",idPaquete);
        queryContratantePaquete.agregarParametroValues("ID_USUARIO_ALTA",usuarioAlta);
        String query = queryContratantePaquete.obtenerQueryInsertar();
        return query;
    }

    public DatosRequest insertarDomicilioEmpresa(ConvenioNuevoPFDto convenioNuevo, UsuarioDto usuario) {
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

    public String generarQueryEmpresaConvenioPf(ConvenioNuevoPFDto convenioNuevo, UsuarioDto usuario, String idDomicilio) {
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

    public DatosRequest consultarPromotores() {
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

    public DatosRequest consultarCurpRfc(String curp, String rfc) {
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

    public DatosRequest consultarCP(String cp) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String query = "SELECT " +
                "SC.CVE_CODIGO_POSTAL AS codigoPostal, " +
                "SC.DES_COLONIA AS colonia, " +
                "SC.DES_MNPIO AS municipio, " +
                "SC.DES_ESTADO AS estado " +
                "FROM " +
                "SVC_CP SC " +
                "WHERE " +
                "SC.CVE_CODIGO_POSTAL = " + cp;
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest busquedaFolioPersona(String folioConvenio){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        SelectQueryUtil querySelect = new SelectQueryUtil();
        querySelect.select("SCP.ID_CONVENIO_PF AS idConvenioPf", "SCP.DES_FOLIO AS folioConvenioPf","SCP.ID_VELATORIO AS idVelatorio","sv.NOM_VELATORIO as nombreVelatorio",
                "SCP.ID_PROMOTOR AS idPromotor","PROM.NUM_EMPLEDO AS numeroEmpleado", "PROM.NOM_PROMOTOR AS nombrePromotor","PROM.NOM_PAPELLIDO AS primerApellido",
                "PROM.NOM_SAPELLIDO AS segundoApellido", "CPF.ID_CONTRATANTE_PAQUETE_CONVENIO_PF AS idContratanteConvenioPf","CPF.ID_CONTRATANTE AS idContratante",
                "SC.CVE_MATRICULA AS cveMatricula","SC.ID_PERSONA AS idPersona","SP.CVE_RFC AS rfc","SP.CVE_CURP AS curp","SP.CVE_NSS AS nss","SP.NOM_PERSONA AS nombrePersona",
                "SP.NOM_PRIMER_APELLIDO AS primerApellido", "SP.NOM_SEGUNDO_APELLIDO AS segundoApellido", "SP.NUM_SEXO AS numSexo","SP.ID_PAIS AS idPais","SP.ID_ESTADO AS idEstado",
                "SP.DES_TELEFONO AS telefono", "SP.DES_CORREO AS correo","SP.TIPO_PERSONA AS tipoPersona","SP.NUM_INE AS numIne",
                        "CPF.ID_PAQUETE AS idPaquete","PAQ.NOM_PAQUETE AS nombrePaquete","DATE_FORMAT(SP.FEC_NAC,'%Y-%m-%d') AS fechaNacimiento")
                .from("SVT_CONVENIO_PF SCP")
                .leftJoin("SVC_VELATORIO SV","SCP.ID_VELATORIO = SV.ID_VELATORIO")
                .leftJoin("SVT_PROMOTOR PROM", "SCP.ID_PROMOTOR = PROM.ID_PROMOTOR")
                .leftJoin("SVT_CONTRATANTE_PAQUETE_CONVENIO_PF CPF", "SCP.ID_CONVENIO_PF = CPF.ID_CONVENIO_PF")
                .leftJoin("SVT_PAQUETE PAQ", "CPF.ID_PAQUETE = PAQ.ID_PAQUETE")
                .leftJoin("SVC_CONTRATANTE SC", "CPF.ID_CONTRATANTE = SC.ID_CONTRATANTE")
                .leftJoin("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
                .leftJoin("SVT_CONTRATANTE_BENEFICIARIOS SCB", "CPF.ID_CONTRATANTE_PAQUETE_CONVENIO_PF = SCB.ID_CONTRATANTE_PAQUETE_CONVENIO_PF")
                .where("SCP.DES_FOLIO = " + folioConvenio)
                .groupBy("SCP.DES_FOLIO");
        String consulta = querySelect.build();
        String encoded = DatatypeConverter.printBase64Binary(consulta.getBytes());
        parametro.put(AppConstantes.QUERY,encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest busquedaBeneficiarios(String folioConvenio){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        SelectQueryUtil querySelect = new SelectQueryUtil();
        querySelect.select("SP2.ID_PERSONA AS idPersona","SP2.NOM_PERSONA AS nombreBeneficiario","SP2.NOM_PRIMER_APELLIDO AS primerApellido",
                        "SP2.NOM_SEGUNDO_APELLIDO AS segundoApellido", "DATE_FORMAT(SP2.FEC_NAC,'%Y-%m-%d') AS fechaNacimiento",
                        "SP2.CVE_RFC AS rfc","SP2.CVE_CURP AS curp", "SP2.CVE_NSS  AS nss", "SP2.NUM_SEXO AS numSexo",
                        "SP2.DES_TELEFONO AS telefono","SP2.DES_CORREO AS correo","SP2.TIPO_PERSONA AS tipoPersona", "SP2.NUM_INE AS numIne")
                .from("SVT_CONVENIO_PF SCP")
                .leftJoin("SVC_VELATORIO SV","SCP.ID_VELATORIO = SV.ID_VELATORIO")
                .leftJoin("SVT_PROMOTOR PROM", "SCP.ID_PROMOTOR = PROM.ID_PROMOTOR")
                .leftJoin("SVT_CONTRATANTE_PAQUETE_CONVENIO_PF CPF", "SCP.ID_CONVENIO_PF = CPF.ID_CONVENIO_PF")
                .leftJoin("SVT_PAQUETE PAQ", "CPF.ID_PAQUETE = PAQ.ID_PAQUETE")
                .leftJoin("SVC_CONTRATANTE SC", "CPF.ID_CONTRATANTE = SC.ID_CONTRATANTE")
                .leftJoin("SVC_PERSONA SP", "SC.ID_PERSONA = SP.ID_PERSONA")
                .leftJoin("SVT_CONTRATANTE_BENEFICIARIOS SCB", "CPF.ID_CONTRATANTE_PAQUETE_CONVENIO_PF = SCB.ID_CONTRATANTE_PAQUETE_CONVENIO_PF")
                .leftJoin("SVC_PERSONA SP2", "SCB.ID_PERSONA = SP2.ID_PERSONA")
                .where("SCP.DES_FOLIO = " + folioConvenio);
        String consulta = querySelect.build();
        String encoded = DatatypeConverter.printBase64Binary(consulta.getBytes());
        parametro.put(AppConstantes.QUERY,encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest busquedaRfcEmpresa(String rfc){
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        SelectQueryUtil querySelect = new SelectQueryUtil();
        querySelect.select("EC.DES_NOMBRE AS nombreEmpresa", "EC.DES_RAZON_SOCIAL AS razonSocial","EC.DES_RFC AS rfc",
                "EC.ID_PAIS AS idPais", "SP.DES_PAIS AS desPais","EC.ID_DOMICILIO AS idDomicilio","SD.DES_CALLE AS calle","SD.NUM_EXTERIOR AS numExterior",
                "SD.NUM_INTERIOR AS numInterior","SD.DES_CP AS cp","SD.DES_COLONIA AS desColonia","SD.DES_MUNICIPIO AS desMunicipio",
                "SD.DES_ESTADO AS desEstado","EC.DES_TELEFONO AS telefono","EC.DES_CORREO AS correo")
                .from("SVT_EMPRESA_CONVENIO_PF EC")
                .leftJoin("SVC_PAIS SP", "EC.ID_PAIS = SP.ID_PAIS")
                .leftJoin("SVT_DOMICILIO SD", "EC.ID_DOMICILIO = SD.ID_DOMICILIO")
                .where("EC.DES_RFC = " + rfc);
        String consulta = querySelect.build();
        String encoded = DatatypeConverter.printBase64Binary(consulta.getBytes());
        parametro.put(AppConstantes.QUERY,encoded);
        dr.setDatos(parametro);
        return dr;
    }
    public Map<String, Object> generarReporte(PdfDto pdfDto) {
        Map<String, Object> datosPdf = new HashMap<>();
        datosPdf.put("rutaNombreReporte", pdfDto.getRutaNombreReporte());
        datosPdf.put("tipoReporte", "pdf");
        datosPdf.put("nombreAfiliado", "Eduardo Orlando Flores Fernandez"); // sacar datos de query
        datosPdf.put("numeroINE", "9876543210");// sacar datos de query
        datosPdf.put("paqueteContratado", "Cremacion");// sacar datos de query
        datosPdf.put("serviciosIncluidos", "sala de cremacion y translado del cuerpo");// sacar datos de query
        datosPdf.put("costoPaquete", "$10,000");// sacar datos de query
        datosPdf.put("nombreTitular", "Eduardo Orlando Flores Fernandez");// sacar datos de query
        datosPdf.put("rfc", "FOFE971205JXA");// sacar datos de query
        datosPdf.put("folioConvenio", pdfDto.getFolioConvenio());// sacar datos de query
        datosPdf.put("ciudadExpedicion", pdfDto.getCiudadExpedicion());// sacar datos de query
        datosPdf.put("fechaExpedicion", pdfDto.getFechaExpedicion());// sacar datos de query
        return datosPdf;
    }
}
