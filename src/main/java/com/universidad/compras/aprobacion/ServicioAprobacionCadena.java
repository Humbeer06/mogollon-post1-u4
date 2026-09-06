package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import org.springframework.stereotype.Service;

@Service
public class ServicioAprobacionCadena implements ServicioAprobacion {

    private final NivelAprobacion primerNivel;
    private final NotificadorCambioEstado notificador;

    public ServicioAprobacionCadena(NotificadorCambioEstado notificador) {
        this.notificador = notificador;

        NivelAprobacion cumplimiento = new RevisorCumplimientoNormativo();
        NivelAprobacion supervisor = new SupervisorArea();
        NivelAprobacion gerente = new GerenteArea();
        NivelAprobacion director = new DirectorFinanciero();

        cumplimiento.enlazarCon(supervisor);
        supervisor.enlazarCon(gerente);
        gerente.enlazarCon(director);

        this.primerNivel = cumplimiento;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = primerNivel.procesar(solicitud);
        solicitud.setEstado(resultado.isAprobada() ? "APROBADA" : "RECHAZADA");
        notificador.notificarCambio(solicitud);
        return resultado;
    }
}