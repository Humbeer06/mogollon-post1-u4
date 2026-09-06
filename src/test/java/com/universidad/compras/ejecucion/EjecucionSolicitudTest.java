package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(new PresupuestoService(), new OrdenCompraService());

        ejecutor.ejecutar(s, "Proveedor XYZ");

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(new PresupuestoService(), new OrdenCompraService());

        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(s, "Proveedor ABC");
            int tamanoAntes = ejecutor.getHistorial().size();
            ejecutor.deshacerUltima();
            int tamanoDespues = ejecutor.getHistorial().size();
            assertEquals(tamanoAntes - 1, tamanoDespues);
        });
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "ana@udes.edu.co", 2000000, "SOFTWARE", "CC-300");
        s.setEstado("APROBADA");
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(new PresupuestoService(), new OrdenCompraService());

        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(s, "Proveedor DEF");
            assertEquals(2, ejecutor.getHistorial().size());
        });
    }
}