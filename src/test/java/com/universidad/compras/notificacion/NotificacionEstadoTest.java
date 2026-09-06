package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        NotificadorCambioEstado mecanismo = new NotificadorCambioEstado();

        assertDoesNotThrow(() -> {
            s.setEstado("APROBADA");
            mecanismo.notificarCambio(s);
        });
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 1800000, "MATERIAL_OFICINA", "CC-200");
        NotificadorCambioEstado mecanismo = new NotificadorCambioEstado();

        // Colector de prueba: un cuarto suscriptor que no forma parte
        // de los tres originales, agregado sin tocar NotificadorCambioEstado.
        java.util.List<String> recibidos = new java.util.ArrayList<>();
        SuscriptorEstado colectorDePrueba = solicitud -> recibidos.add(solicitud.getEstado());
        mecanismo.suscribir(colectorDePrueba);

        assertDoesNotThrow(() -> {
            s.setEstado("RECHAZADA");
            mecanismo.notificarCambio(s);
        });

        assertEquals(1, recibidos.size());
        assertEquals("RECHAZADA", recibidos.get(0));
    }
}