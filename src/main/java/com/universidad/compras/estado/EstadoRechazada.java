package com.universidad.compras.estado;

public class EstadoRechazada implements EstadoSolicitud {
    public EstadoSolicitud aprobar(ContextoSolicitud contexto) { return this; }
    public EstadoSolicitud rechazar(ContextoSolicitud contexto) { return this; }
    public EstadoSolicitud ejecutar(ContextoSolicitud contexto) { return this; } // inválido, no cambia
    public EstadoSolicitud cancelar(ContextoSolicitud contexto) { return this; }
    public String nombre() { return "RECHAZADA"; }
}