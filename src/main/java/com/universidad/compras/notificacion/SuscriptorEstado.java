package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Observer: cada suscriptor reacciona a un cambio de estado sin que
 * el emisor del cambio conozca cuántos suscriptores existen ni qué
 * hace cada uno.
 */
public interface SuscriptorEstado {
    void onCambioEstado(Solicitud solicitud);
}