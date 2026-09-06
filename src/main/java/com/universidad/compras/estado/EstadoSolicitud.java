package com.universidad.compras.estado;

/**
 * State: cada estado decide qué operaciones son válidas y a qué
 * estado siguiente transiciona la solicitud. Reemplaza los if/else
 * dispersos sobre getEstado() por un objeto que encapsula ese
 * comportamiento y esas transiciones.
 */
public interface EstadoSolicitud {
    EstadoSolicitud aprobar(ContextoSolicitud contexto);
    EstadoSolicitud rechazar(ContextoSolicitud contexto);
    EstadoSolicitud ejecutar(ContextoSolicitud contexto);
    EstadoSolicitud cancelar(ContextoSolicitud contexto);
    String nombre();
}