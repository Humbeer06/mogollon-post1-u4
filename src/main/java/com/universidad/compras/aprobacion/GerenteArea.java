package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class GerenteArea extends NivelAprobacion {
    private static final double LIMITE = 10_000_000;

    protected boolean puedeResolver(Solicitud solicitud) {
        return solicitud.getMonto() <= LIMITE;
    }

    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setNivelResolutor("Gerente de Área");
        return new ResultadoAprobacion(true, "Gerente de Área",
            "Aprobada dentro del límite del Gerente de Área");
    }
}