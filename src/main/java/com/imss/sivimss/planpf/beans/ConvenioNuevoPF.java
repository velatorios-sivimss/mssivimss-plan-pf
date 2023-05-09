package com.imss.sivimss.planpf.beans;

import com.imss.sivimss.planpf.model.request.ConvenioNuevoPFDto;
import com.imss.sivimss.planpf.model.request.UsuarioDto;
import com.imss.sivimss.planpf.util.DatosRequest;
import com.imss.sivimss.planpf.util.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class ConvenioNuevoPF {

    public DatosRequest insertarConvenioPfEmpresa(ConvenioNuevoPFDto convenioNuevo, UsuarioDto usuario) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper query = new QueryHelper("INSERT INTO SVT_CONVENIO_PF");
    }
}
