package com.universidad.compras.estado;

public class EstadoPendiente implements EstadoSolicitud {
    public EstadoSolicitud aprobar(ContextoSolicitud contexto) { return new EstadoAprobada(); }
    public EstadoSolicitud rechazar(ContextoSolicitud contexto) { return new EstadoRechazada(); }
    public EstadoSolicitud ejecutar(ContextoSolicitud contexto) { return this; } // inválido, no cambia
    public EstadoSolicitud cancelar(ContextoSolicitud contexto) { return new EstadoCancelada(); }
    public String nombre() { return "PENDIENTE"; }
}