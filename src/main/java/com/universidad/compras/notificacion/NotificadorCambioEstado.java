package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto de Observer: mantiene la lista de suscriptores y les avisa
 * cuando una Solicitud cambia de estado. El código que modifica el
 * estado solo conoce este notificador, nunca a los suscriptores
 * individuales. Agregar un suscriptor nuevo (por ejemplo, un canal
 * interno del área) solo requiere llamar a suscribir(), sin tocar
 * este mecanismo central.
 */
@Component
public class NotificadorCambioEstado {

    private final List<SuscriptorEstado> suscriptores = new ArrayList<>();

    public NotificadorCambioEstado() {
        suscribir(new SuscriptorCorreo());
        suscribir(new SuscriptorDashboard());
        suscribir(new SuscriptorAuditoria());
    }

    public void suscribir(SuscriptorEstado suscriptor) {
        suscriptores.add(suscriptor);
    }

    public void notificarCambio(Solicitud solicitud) {
        for (SuscriptorEstado suscriptor : suscriptores) {
            suscriptor.onCambioEstado(solicitud);
        }
    }
}