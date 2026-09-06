package com.universidad.compras.estado;

/**
 * Resuelve el objeto EstadoSolicitud correspondiente al String
 * guardado en Solicitud.estado, para reconstruir el Context sin
 * duplicar la lógica de transición en un switch disperso.
 */
public final class EstadoFactory {
    private EstadoFactory() {}

    public static EstadoSolicitud desde(String nombreEstado) {
        return switch (nombreEstado) {
            case "PENDIENTE", "EN_APROBACION" -> new EstadoPendiente();
            case "APROBADA" -> new EstadoAprobada();
            case "RECHAZADA" -> new EstadoRechazada();
            case "EJECUTADA" -> new EstadoEjecutada();
            case "CANCELADA" -> new EstadoCancelada();
            default -> new EstadoPendiente();
        };
    }
}