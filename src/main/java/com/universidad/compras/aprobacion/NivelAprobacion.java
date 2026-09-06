package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Chain of Responsibility: cada nivel decide si la solicitud está
 * dentro de su autoridad, o la delega al siguiente eslabón de la
 * cadena. Agregar, quitar o reordenar un nivel no requiere tocar
 * ControladorSolicitudes ni los demás niveles.
 */
public abstract class NivelAprobacion {

    protected NivelAprobacion siguiente;

    public NivelAprobacion enlazarCon(NivelAprobacion siguienteNivel) {
        this.siguiente = siguienteNivel;
        return siguienteNivel;
    }

    public ResultadoAprobacion procesar(Solicitud solicitud) {
        if (puedeResolver(solicitud)) {
            return resolver(solicitud);
        }
        if (siguiente != null) {
            return siguiente.procesar(solicitud);
        }
        return new ResultadoAprobacion(false, "Sin resolver",
            "Ningún nivel de aprobación pudo resolver la solicitud");
    }

    protected abstract boolean puedeResolver(Solicitud solicitud);
    protected abstract ResultadoAprobacion resolver(Solicitud solicitud);
}