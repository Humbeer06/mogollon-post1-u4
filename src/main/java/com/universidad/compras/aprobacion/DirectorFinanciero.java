package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class DirectorFinanciero extends NivelAprobacion {

    protected boolean puedeResolver(Solicitud solicitud) {
        return true; // sin límite superior
    }

    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setNivelResolutor("Director Financiero");
        return new ResultadoAprobacion(true, "Director Financiero",
            "Aprobada sin límite por el Director Financiero");
    }
}