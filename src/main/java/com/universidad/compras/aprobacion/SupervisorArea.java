package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class SupervisorArea extends NivelAprobacion {
    private static final double LIMITE = 2_000_000;

    protected boolean puedeResolver(Solicitud solicitud) {
        return solicitud.getMonto() <= LIMITE;
    }

    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setNivelResolutor("Supervisor de Área");
        return new ResultadoAprobacion(true, "Supervisor de Área",
            "Aprobada dentro del límite del Supervisor de Área");
    }
}