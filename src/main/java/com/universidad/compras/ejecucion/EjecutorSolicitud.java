package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Orquesta la ejecución de una solicitud aprobada: reserva
 * presupuesto y genera orden de compra como Commands independientes,
 * cada uno deshacible por separado, con su historial consultable.
 */
@Service
public class EjecutorSolicitud {

    private final PresupuestoService presupuestoService;
    private final OrdenCompraService ordenCompraService;
    private final HistorialOperaciones historial = new HistorialOperaciones();

    public EjecutorSolicitud(PresupuestoService presupuestoService, OrdenCompraService ordenCompraService) {
        this.presupuestoService = presupuestoService;
        this.ordenCompraService = ordenCompraService;
    }

    public void ejecutar(Solicitud solicitud, String proveedor) {
        OperacionEjecucion reserva = new ReservarPresupuestoCommand(
            presupuestoService, solicitud.getCentroCosto(), solicitud.getMonto());
        reserva.ejecutar();
        historial.registrar(reserva);

        OperacionEjecucion orden = new GenerarOrdenCompraCommand(
            ordenCompraService, solicitud.getId(), proveedor);
        orden.ejecutar();
        historial.registrar(orden);

        solicitud.setEstado("EJECUTADA");
    }

    public void deshacerUltima() {
        List<OperacionEjecucion> ops = historial.consultarHistorial();
        if (!ops.isEmpty()) {
            historial.deshacer(ops.get(ops.size() - 1));
        }
    }

    public HistorialOperaciones getHistorial() {
        return historial;
    }
}