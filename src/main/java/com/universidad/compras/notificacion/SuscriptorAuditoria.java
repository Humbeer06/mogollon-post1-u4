package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class SuscriptorAuditoria implements SuscriptorEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        String resolutor = solicitud.getNivelResolutor() != null
            ? solicitud.getNivelResolutor()
            : "Equipo de Compras";
        ClientesNotificacion.registrarAuditoria(
            solicitud.getId(), solicitud.getEstado(),
            "Resuelto por: " + resolutor
        );
    }
}