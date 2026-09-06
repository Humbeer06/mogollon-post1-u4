package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

/**
 * Contexto de State: mantiene la Solicitud y el estado actual, y
 * delega cada operación al estado, que decide si es válida y a qué
 * estado transicionar. Una operación inválida para el estado actual
 * se rechaza sin alterar el estado.
 */
public class ContextoSolicitud {

    private final Solicitud solicitud;
    private EstadoSolicitud estadoActual;

    public ContextoSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.estadoActual = EstadoFactory.desde(solicitud.getEstado());
    }

    public void aprobar() {
        transicionarA(estadoActual.aprobar(this));
    }

    public void rechazar() {
        transicionarA(estadoActual.rechazar(this));
    }

    public void ejecutar() {
        transicionarA(estadoActual.ejecutar(this));
    }

    public void cancelar() {
        transicionarA(estadoActual.cancelar(this));
    }

    private void transicionarA(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        solicitud.setEstado(nuevoEstado.nombre());
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }
}