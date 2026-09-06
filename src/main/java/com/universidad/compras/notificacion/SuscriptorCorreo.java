package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class SuscriptorCorreo implements SuscriptorEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        ClientesNotificacion.enviarCorreo(
            solicitud.getSolicitanteEmail(),
            "Actualización de tu solicitud " + solicitud.getId(),
            "Tu solicitud cambió al estado: " + solicitud.getEstado()
        );
    }
}