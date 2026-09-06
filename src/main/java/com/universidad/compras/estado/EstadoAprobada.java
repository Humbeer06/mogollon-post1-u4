package com.universidad.compras.estado;

public class EstadoAprobada implements EstadoSolicitud {
    public EstadoSolicitud aprobar(ContextoSolicitud contexto) { return this; } // ya está aprobada
    public EstadoSolicitud rechazar(ContextoSolicitud contexto) { return this; } // inválido, no cambia
    public EstadoSolicitud ejecutar(ContextoSolicitud contexto) { return new EstadoEjecutada(); }
    public EstadoSolicitud cancelar(ContextoSolicitud contexto) { return new EstadoCancelada(); }
    public String nombre() { return "APROBADA"; }
}