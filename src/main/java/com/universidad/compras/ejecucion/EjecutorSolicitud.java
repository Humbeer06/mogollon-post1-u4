package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EjecutorSolicitud {

    private final PresupuestoService presupuestoService;
    private final OrdenCompraService ordenCompraService;
    private final NotificadorCambioEstado notificador;
    private final HistorialOperaciones historial = new HistorialOperaciones();

    public EjecutorSolicitud(PresupuestoService presupuestoService, OrdenCompraService ordenCompraService,
                             NotificadorCambioEstado notificador) {
        this.presupuestoService = presupuestoService;
        this.ordenCompraService = ordenCompraService;
        this.notificador = notificador;
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
        notificador.notificarCambio(solicitud);
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