package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Nivel adicional para solicitudes INTERNACIONAL. Se agrega antes del
 * nivel correspondiente por monto, sin que eso requiera modificar los
 * demás niveles ni el código que dispara la evaluación.
 */
public class RevisorCumplimientoNormativo extends NivelAprobacion {

    protected boolean puedeResolver(Solicitud solicitud) {
        return "INTERNACIONAL".equals(solicitud.getCategoria());
    }

    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setNivelResolutor("Revisor de Cumplimiento Normativo");
        return new ResultadoAprobacion(true, "Revisor de Cumplimiento Normativo",
            "Requiere revisión de cumplimiento normativo por ser INTERNACIONAL");
    }
}